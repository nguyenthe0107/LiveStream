package olmo.wellness.android.di.interceptors;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

public class HttpCustomLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final String TAG = "OkHttp";

    private volatile okhttp3.logging.HttpLoggingInterceptor.Level level = okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public HttpCustomLoggingInterceptor setLevel(okhttp3.logging.HttpLoggingInterceptor.Level level) {
        if (level == null)
            throw new NullPointerException("level == null. Use Level.EMPTY instead.");
        this.level = level;
        return this;
    }

    public okhttp3.logging.HttpLoggingInterceptor.Level getLevel() {
        return level;
    }

    @NotNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        okhttp3.logging.HttpLoggingInterceptor.Level level = this.level;

        Request request = chain.request().newBuilder()
                .build();

        if (level == okhttp3.logging.HttpLoggingInterceptor.Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

        long id = System.nanoTime();
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("--> Id: ").append(id).append("\n");
        logBuilder.append("--> ").append(request.method()).append(' ').append(request.url()).append(' ').append(protocol);
        if (!logHeaders && hasRequestBody) {
            logBuilder.append(" (").append(requestBody.contentLength()).append("-byte body)");
        }
        logBuilder.append("\n");
        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logBuilder.append("--> Content-Type: ").append(requestBody.contentType()).append("\n");
                }
                if (requestBody.contentLength() != -1) {
                    logBuilder.append("--> Content-Length: ").append(requestBody.contentLength()).append("\n");
                }
            }

            Headers headers = request.headers();
            JsonObject reqHeaders = new JsonObject();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    reqHeaders.addProperty(name, headers.value(i));
                }
            }
            logBuilder.append("--> Headers: ").append(reqHeaders).append("\n");

            if (!logBody || !hasRequestBody) {
                logBuilder.append("--> END ").append(request.method()).append("\n");
            } else if (bodyEncoded(request.headers())) {
                logBuilder.append("--> END ").append(request.method()).append(" (encoded body omitted)").append("\n");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (isPlaintext(buffer)) {
                    logBuilder
                            .append("--> Body: ")
                            .append(charset != null ? buffer.readString(charset) : "empty")
                            .append("\n");
                    logBuilder
                            .append("--> END ").append(request.method()).append(" (")
                            .append(requestBody.contentLength())
                            .append("-byte body)")
                            .append("\n");
                } else {
                    logBuilder
                            .append("--> END ").append(request.method())
                            .append("(binary ")
                            .append(requestBody.contentLength())
                            .append("-byte body omitted)")
                            .append("\n");
                }
            }
        }

        Log.i(TAG, logBuilder.toString());
        logBuilder = new StringBuilder().append("<-- Id: ").append(id).append("\n");
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logBuilder.append("<-- HTTP FAILED: ").append(e).append("\n");
            Log.i(TAG, logBuilder.toString());
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody != null ? responseBody.contentLength() : -1;
        String bodySize = contentLength == -1 ? "unknown-length" : contentLength + "-byte";
        String logHeader = logHeaders ? "" : ", " + bodySize + " body";
        Log.v("=====", String.valueOf(response.request().url()));
        logBuilder.append("<-- Code ").append(response.code()).append(' ').append(response.message())
                .append(' ').append(response.request().url()).append(" (").append(tookMs)
                .append("ms").append(logHeader).append(')').append("\n");
        if (logHeaders) {
            Headers headers = response.headers();

            JsonObject resHeaders = new JsonObject();
            for (int i = 0, count = headers.size(); i < count; i++) {
                resHeaders.addProperty(headers.name(i), headers.value(i));
            }
            logBuilder.append("<-- Headers: ").append(resHeaders).append("\n");

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logBuilder.append("<-- END HTTP").append("\n");
            } else if (bodyEncoded(response.headers())) {
                logBuilder.append("<-- END HTTP (encoded body omitted)").append("\n");
            } else {
                if (responseBody != null) {
                    BufferedSource source = responseBody.source();

                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.getBuffer();

                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }

                    if (!isPlaintext(buffer)) {
                        logBuilder.append("<-- END HTTP (binary ").append(buffer.size()).append("-byte body omitted)").append("\n");
                        Log.i(TAG, logBuilder.toString());
                        return response;
                    }

                    if (contentLength != 0) {
                        logBuilder.append("<-- Body: ").append(charset != null ? buffer.clone().readString(charset) : "empty").append("\n");
                    }

                    logBuilder.append("<-- END HTTP (").append(buffer.size()).append("-byte body)").append("\n");
                } else {
                    logBuilder.append("<-- END HTTP (").append("-1").append("-byte body)").append("\n");
                }
            }
        }

        Log.i(TAG, logBuilder.toString());
        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
