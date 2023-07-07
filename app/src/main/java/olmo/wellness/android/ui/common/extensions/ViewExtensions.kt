package olmo.wellness.android.ui.common.extensions

import android.content.Context
import android.widget.Toast

fun showMessageResource(context: Context, messageId: Int) {
    Toast.makeText(context, context.getText(messageId), Toast.LENGTH_SHORT).show()
}

fun showMessage(context: Context, messageContent: String) {
    Toast.makeText(context, messageContent, Toast.LENGTH_SHORT).show()
}
