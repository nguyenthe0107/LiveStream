package olmo.wellness.android.ui.common.callback_sdk

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import olmo.wellness.android.ui.screen.signin_screen.SignInViewModel

fun handleSignInResult(
    completedTask: Task<GoogleSignInAccount>,
    context: Context, viewModel: SignInViewModel,
    coroutineScope: CoroutineScope
) {
    val account = try {
        completedTask.getResult(ApiException::class.java)
    } catch (e: ApiException) {
        return
    }
}