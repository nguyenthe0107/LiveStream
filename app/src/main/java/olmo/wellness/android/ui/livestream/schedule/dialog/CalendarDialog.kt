package olmo.wellness.android.ui.livestream.schedule.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.himanshoe.kalendar.common.KalendarSelector
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.Kalendar
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.KalendarType
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.KalendarStyle
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarShape
import olmo.wellness.android.ui.theme.*
import java.time.LocalDate

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CalendarDialog(
    dateSelect : LocalDate,
    showDialog: Boolean,
    eventList: List<KalendarEvent>,
    setShowDialog: (Boolean) -> Unit,
    onDateSelect: (LocalDate) -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { setShowDialog(false) },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Transparent
            ) {
                Column(modifier = Modifier) {
                    val style = KalendarStyle(
                        kalendarBackgroundColor = Color_LiveStream_Main_Color,
                        kalendarColor = White,
                        kalendarSelector = KalendarSelector.Circle(
                            defaultColor = White,
                            selectedColor = Color_BLUE_7F4, todayColor = White
                        ),
                        hasRadius = false,
                        shape = KalendarShape.DefaultRectangle
                    )
                    Kalendar(
                        kalendarType = KalendarType.Firey(),
                        kalendarStyle = style,
                        onCurrentDayClick = { localDate, kalendarEvent, kalendarMoney ->
                            onDateSelect.invoke(localDate)
                            setShowDialog.invoke(false)
                        },
                        onBack = {
                            setShowDialog.invoke(false)
                        },
                        onDrop = {
                            setShowDialog.invoke(false)
                        },
                        kalendarEvents = eventList,
                        selectedDay = dateSelect
                    )
                }
            }
        }
    }
}