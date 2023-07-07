package olmo.wellness.android.ui.livestream.info_livestream

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.marginMinimum

@Composable
fun WhoWatchLivestreamDialog(onDismiss: () -> Unit) {
    val radioGroup = listOf("Everyone", "Follower Only")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioGroup[0]) }


    AlertDialog(
        title = {
            Text(
                text = "Who can watch livestream",
                style = MaterialTheme.typography.subtitle1
            )
        },
        text = {
            Column() {
                Column(modifier = Modifier) {
                    radioGroup.forEach { txt ->
                        Row(modifier = Modifier
                            .selectable(
                                selected = (txt == selectedOption), onClick = {
                                    onOptionSelected(txt)
                                }
                            )
                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (txt == selectedOption),
                                modifier = Modifier.padding(marginMinimum),
                                onClick = {
                                    onOptionSelected(txt)
                                })
                            Text(text = txt, modifier = Modifier, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Text(text = "Confirm")
            }
        },
        onDismissRequest = onDismiss
    )
}