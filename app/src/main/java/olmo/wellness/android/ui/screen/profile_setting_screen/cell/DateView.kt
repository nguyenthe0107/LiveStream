package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.err_resubmit.ErrorTitleReSubmitCompose
import olmo.wellness.android.ui.theme.*
import java.util.*
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@Composable
fun TimeView(
    titleText: String? = null,
    hint: String = "",
    defaultValue: String? = null,
    isError: Boolean = false,
    isErrorResubmit: Boolean? = false,
    paddingHorizontal: Dp = marginDouble,
    paddingVertical: Dp = marginDouble,
    onChange: (String) -> Unit
) {
    val context = LocalContext.current
    var timeValue by remember { mutableStateOf(defaultValue) }
    var mCalendar by remember { mutableStateOf(Calendar.getInstance()) }
    var mHour by remember { mutableStateOf(mCalendar.get(Calendar.HOUR_OF_DAY)) }
    var mMinute by remember { mutableStateOf(mCalendar.get(Calendar.MINUTE)) }
    val mTimePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            timeValue = "$hour:$minute"
            onChange.invoke(hour.toString())
        }, mHour, mMinute, false
    )

    mTimePickerDialog.setOnDismissListener {
        if (!isError)
            onChange(timeValue.orEmpty())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            titleText?.let {
                Text(
                    text = it,
                    color = Neutral_Gray_9,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = marginMinimum)
                )
            }
            if (isErrorResubmit == true) {
                ErrorTitleReSubmitCompose()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightTextField_36)
                .border(
                    BorderStroke(
                        width = defaultBorderWidth,
                        color = if (isError) Error_500 else Neutral_Gray_4
                    ),
                    RoundedCornerShape(marginStandard)
                )
                .noRippleClickable {
                    mTimePickerDialog.show()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = padding_12),
                maxLines = 1,
                text = if (timeValue?.isEmpty() == true) hint else timeValue.toString(),
                style = MaterialTheme.typography.body2,
                color = if (timeValue.isNullOrEmpty()) Neutral_Gray_5 else Neutral_Gray_9,
            )
        }

        if (isError) {
            Text(
                text = stringResource(id = R.string.error_validate_expire_date),
                color = Error_500,
                style = MaterialTheme.typography.overline,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = marginMinimum)
            )
        }
    }
}

@Composable
fun DateView(
    titleText: String? = null,
    hint: String = "",
    defaultValue: String? = null,
    isError: Boolean = false,
    isErrorResubmit: Boolean? = false,
    isExpireDate: Boolean = false,
    paddingHorizontal: Dp = marginDouble,
    paddingVertical: Dp = marginStandard,
    onChange: (String) -> Unit
) {
    val context = LocalContext.current
    var dateValue by remember { mutableStateOf(defaultValue) }
    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    var isErrorYear by remember { mutableStateOf(false) }
    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            isErrorYear = if (isExpireDate) {
                month <= mMonth && year <= mYear
            } else (mYear - year) < 10

            var monthString: String = (month + 1).toString()
            if (month + 1 < 10) {
                monthString = "0$monthString"
            }

            var dayOfMonthString: String = dayOfMonth.toString()
            if (dayOfMonth < 10) {
                dayOfMonthString = "0$dayOfMonth"
            }
            var date = ""
            if (dayOfMonthString.isNotEmpty() && monthString.isNotEmpty() && year != 0) {
                date = "$dayOfMonthString/${monthString}/$year"
                dateValue = date
            }
            if (!isErrorYear)
                onChange(date)
        }, mYear, mMonth, mDay
    )

    mDatePickerDialog.setOnDismissListener {
        if (!isError && !isErrorYear)
            onChange(dateValue.orEmpty())
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            titleText?.let {
                Text(
                    text = it,
                    color = Neutral_Gray_9,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = marginMinimum)
                )
            }
            if (isErrorResubmit == true) {
                ErrorTitleReSubmitCompose()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightTextField_36)
                .border(
                    BorderStroke(
                        width = defaultBorderWidth,
                        color = if (isError) Error_500 else Neutral_Gray_4
                    ),
                    RoundedCornerShape(marginStandard)
                )
                .noRippleClickable {
                    mDatePickerDialog.show()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = padding_12),
                maxLines = 1,
                text = if (dateValue?.isEmpty() == true) hint else dateValue.toString(),
                style = MaterialTheme.typography.body2,
                color = if (dateValue.isNullOrEmpty()) Neutral_Gray_5 else Neutral_Gray_9,
            )
        }

        if (isError || isErrorYear) {
            Text(
                text = stringResource(id = if (isError) R.string.error_empty_textfield else if (isExpireDate) R.string.error_validate_expire_date else R.string.error_validate_year),
                color = Error_500,
                style = MaterialTheme.typography.overline,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = marginMinimum)
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun rememberDatePickerDialog(
    @StringRes title: Int,
    select: Date? = null,
    bounds: CalendarConstraints? = null,
    onDateSelected: (Date) -> Unit = {},
): MaterialDatePicker<Long> {
    val datePicker = remember {
        MaterialDatePicker.Builder.datePicker()
            .setSelection(
                (select?.time
                    ?: Date().time) + 24.hours.toLong(DurationUnit.MILLISECONDS)
            )
            .setCalendarConstraints(bounds)
            .setTitleText(title)
            .build()
    }

    DisposableEffect(datePicker) {
        val listener = MaterialPickerOnPositiveButtonClickListener<Long> {
            if (it != null) onDateSelected(Date(it))
        }
        datePicker.addOnPositiveButtonClickListener(listener)
        onDispose {
            datePicker.removeOnPositiveButtonClickListener(listener)
        }
    }
    return datePicker
}

@Composable
fun DatePicker() {
    val datesList = listOf<String>("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    var calendar = Calendar.getInstance()
//    calendar.add(Calendar.MONTH,1)
    if (calendar.get(Calendar.MONTH) == calendar.getActualMinimum(Calendar.MONTH)) {
        calendar.set(calendar.get(Calendar.YEAR) - 1, calendar.getActualMaximum(Calendar.MONTH), 1)
    } else {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
    }

    val dateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    var initWeekday = getFirstDayOfMoth(calendar) - 1 // position 0 if Sun

    var dayCounter = 1
    var week = 1
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            datesList.forEach {
                Box(
                    Modifier
                        .weight(1f)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = it.substring(0, 3), style = MaterialTheme.typography.subtitle1)
                }
            }
        }
        while (dayCounter <= dateOfMonth) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
            ) {
                if (initWeekday > 0) {
                    repeat(initWeekday) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                for (i in week..(7 - initWeekday)) {
                    if (dayCounter <= dateOfMonth) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .padding(10.dp)
                        ) {
                            if (dayCounter < 10) {
                                Text(
                                    text = dayCounter++.toString(),
                                    color = Color_DOT_SLIDE_DISABLE
                                )
                                Divider(
                                    color = Green_474,
                                    thickness = 1.dp,
                                    modifier = Modifier.rotate(45f)
                                )
                            } else {
                                Text(text = dayCounter++.toString())
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                initWeekday = 0
            }
        }
    }
}

private fun getFirstDayOfMoth(calendar: Calendar): Int {
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1)
    return calendar.get(Calendar.DAY_OF_WEEK)
}

fun showTimePicker(
    context: Context,
    hour: Int,
    minute: Int,
    onChange: (Int, Int) -> Unit   // hour, minute
) {
    var mHour = hour
    var mMinute = minute
    val mTimePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            mHour = hour
            mMinute = minute
            onChange.invoke(hour, minute)
        }, mHour, mMinute, true
    )
    mTimePickerDialog.setOnDismissListener {
//        onChange(mHour, mMinute)
    }
    mTimePickerDialog.show()
}

@Preview
@Composable
fun launch() {
    DatePicker()
}