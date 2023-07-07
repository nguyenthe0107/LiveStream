package olmo.wellness.android.ui.helpers

import android.annotation.SuppressLint
import android.content.Context
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.picker.time.ui.AMPMHours
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs


object DateTimeHelper {
    const val FORMAT_DATETIME_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val FORMAT_TIME_PICKER="dd/MM/yyyy HH:mm:ss"
    const val FORMAT_TIME_BIRTHDAY = "dd/MM/yyyy"
    const val FORMAT_DATETIME_CLIENT = "dd-MMMM-yyyy"
    const val FORMAT_DATE_SCHEDULE= "MM, dd yyyy      hh:mm a"
    const val hhMM = "h:mm a"
    const val EMPTY = ""
    const val FORMAT_SCHEDULE_LINE= "dd MMMM yyyy"

    const val dd_MM_yyyy_HH_mm_a = "dd-MM-yyyy | hh:mm a"
    const val MMddyyyy = "MM, dd yyyy"
    const val ddMMMyy = "dd MMM yyyy"

    private const val ONE_SECOND: Long = 1000
    private const val ONE_MINUTE: Long = ONE_SECOND * 60
    private const val ONE_HOUR: Long = ONE_MINUTE * 60
    private const val ONE_DAY: Long = ONE_HOUR * 24
    private const val ONE_WEEK: Long = (ONE_DAY * 7) - 1
    private const val ONE_MONTH: Long = (ONE_DAY * 30) - 1
    private const val ONE_YEAR: Long = 31556952000

    fun validate(dateString: String?, format: String?): Boolean {
        return try {
            val sdf = SimpleDateFormat(format)
            val date = sdf.parse(dateString)
            dateString.equals(sdf.format(date))
        } catch (e: Exception) {
            false
        }
    }

    private fun simpleDateToString(date: Date, format: String? = FORMAT_DATETIME_SERVER): String {
        return try {
            SimpleDateFormat(format).format(date)
        } catch (e: Exception) {
            EMPTY
        }
    }

    private fun simpleStringToDate(
        dateString: String?,
        format: String? = FORMAT_DATETIME_SERVER
    ): Date {
        return try {
            SimpleDateFormat(format).parse(dateString)
        } catch (e: Exception) {
            Date()
        }
    }

    /**
     * Convert server UTC time to device Local time
     */
    /*private fun convertUTCToLocalDate(dateString: String?, format: String? = FORMAT_DATETIME_SERVER): Date {
        return try {
            SimpleDateFormat(format).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.parse(dateString)
        } catch (e: Exception) {
            Date()
        }
    }*/

    private fun convertServerTimeToLocalDate(
        dateString: String?,
        format: String = FORMAT_DATETIME_CLIENT
    ): Date {
        return try {
            SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
        } catch (e: Exception) {
            Date()
        }
    }

    /**
     * Convert server UTC time to device Local time
     */

    private fun convertServerTimeToLocalMillis(dateString: String?): Long {
        return convertServerTimeToLocalDate(dateString).time
    }

    fun getTimeUnixFromDate(dateString: String?): String {
        return (convertServerTimeToLocalMillis(dateString) / 1000).toString()
    }

    private fun convertServerTimeToLocalHours(dateString: String?): Long {
        return convertServerTimeToLocalMillis(dateString) / (3600000)
    }

    fun getTimeCreatedBy(dateString: String?): String {
        val currentLocalTime = System.currentTimeMillis()
        val fromServerTime = convertServerTimeToLocalMillis(dateString)
        val diff = abs(currentLocalTime - fromServerTime)
        val hours = diff / (ONE_HOUR)
        val space = " "
        val emptySpace = ""
        return when {
            hours < 1 -> {
                val minutes = diff / (ONE_MINUTE)
                if (minutes < 1) {
                    val second = diff / ONE_SECOND
                    second.toString().plus(space + "giây trước")
                } else {
                    minutes.toString().plus(space + "phút trước")
                }
            }
            hours in 1..23 -> {
                hours.toString().plus(space + "giờ trước")
            }
            hours in 24..47 -> {
                emptySpace.plus("ngày trước")
            }
            hours > 48 -> {
                try {
                    val arrDate = dateString?.split(" ")?.toTypedArray()
                    if (arrDate?.get(0) != null) {
                        arrDate[0]
                    } else {
                        dateString.toString()
                    }
                } catch (ex: Exception) {
                    dateString.toString()
                }
            }

            else -> dateString.toString()
        }
    }

    /**
     * Convert device Local time to server UTC time
     */
    private fun convertLocalToUTCDate(date: Date): Date {
        return try {
            Calendar.getInstance().apply {
                time = Date(date.time - TimeZone.getDefault().rawOffset)
            }.time
        } catch (e: Exception) {
            Date()
        }
    }

    /**
     * Convert device Local time to server UTC time
     */
    private fun convertLocalToUTCDate(
        dateString: String?,
        format: String? = FORMAT_DATETIME_SERVER
    ): Date {
        return convertLocalToUTCDate(simpleStringToDate(dateString, format))
    }

    /**
     * Convert device Local time to server UTC time
     */
    private fun convertLocalToUTCString(
        date: Date,
        format: String? = FORMAT_DATETIME_SERVER
    ): String {
        return simpleDateToString(convertLocalToUTCDate(date), format)
    }

    /**
     * Convert device Local time to server UTC time
     */
    fun convertLocalToUTCString(
        dateString: String?,
        format: String? = FORMAT_DATETIME_SERVER
    ): String {
        return simpleDateToString(convertLocalToUTCDate(dateString), format)
    }

    /**
     * Current UTC time on server
     */
    private fun currentUTCDate(): Date {
        return convertLocalToUTCDate(Date())
    }

    /**
     * Current UTC time on server
     */
    fun currentUTCString(): String {
        return convertLocalToUTCString(Date())
    }

    fun getMinutesNoSuffixes(diff: Long): String {
        return diff.div(ONE_MINUTE).toString()
    }

    fun Date.plus(numberDates: Int): Date {
        val it = this
        val calendar = Calendar.getInstance().apply {
            time = it
            add(Calendar.DATE, numberDates)
        }
        return calendar.time

    }

    fun currentDateTime(format: String): String {
        return SimpleDateFormat(format).format(Date())
    }

    fun Long.abs(): Long {
        return kotlin.math.abs(this)
    }

    fun Int.abs(): Int {
        return kotlin.math.abs(this)
    }

    fun Float.abs(): Float {
        return kotlin.math.abs(this)
    }

    fun Double.abs(): Double {
        return kotlin.math.abs(this)
    }

    fun getCurrentTime(): Long {
        return (System.currentTimeMillis() / ONE_SECOND)
    }

    fun getCurrentTimeToString(): String {
        return (System.currentTimeMillis() / ONE_SECOND).toString()
    }

    fun getCreatedTimeToString(): String {
        return ((System.currentTimeMillis() - ONE_YEAR) / ONE_SECOND).toString()
    }

    fun getCreatedTime(): Long {
        return (System.currentTimeMillis() - ONE_YEAR) / ONE_SECOND
    }

    fun getCreatedTimeOneMonthToString(): String {
        return ((System.currentTimeMillis() - ONE_MONTH) / ONE_SECOND).toString()
    }

    fun getCreatedTimeOneMonth(): Long {
        return (System.currentTimeMillis() - ONE_MONTH) / ONE_SECOND
    }

    @SuppressLint("SimpleDateFormat")
    fun convertTimeStampToString(dateTime: Long): String {
        dateTime.let {
            val stamp = Timestamp(dateTime)
            val date = Date(stamp.time)
            val sdf = SimpleDateFormat(FORMAT_DATETIME_CLIENT)
            return try {
                sdf.format(date).replace("-", " ")
            } catch (ex: Exception) {
                ""
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun hmsTimeFormatter(milliSeconds: Long): kotlin.String? {
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(milliSeconds),
            TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    milliSeconds
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    milliSeconds
                )
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun convertToStringSchedule(dateLong: Long?): String{
        if (dateLong != null) {
            val date = convertLongToDate(dateLong)
            return SimpleDateFormat(FORMAT_DATE_SCHEDULE).format(date)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    fun convertToStringHour(dateLong: Long?): String {
        if (dateLong != null) {
            val date = convertLongToDate(dateLong)
            return SimpleDateFormat(hhMM).format(date)
        }
        return ""
    }

    fun convertLongToDate(dateLong: Long?): Date {
        if (dateLong != null) {
            val date = Date()
            date.time = dateLong
            return date
        }
        return Date()
    }


    fun checkSameDate(date1: Long?, date2: Long?): Boolean {
        val cal1: Calendar = Calendar.getInstance()
        val cal2: Calendar = Calendar.getInstance()
        cal1.time = convertLongToDate(date1)
        cal2.time = convertLongToDate(date2)
        return cal1.get(Calendar.DAY_OF_YEAR) === cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) === cal2.get(Calendar.YEAR)
    }

    fun checkSameDate(date1: Date?, date2: Date?): Boolean {
        val cal1: Calendar = Calendar.getInstance()
        val cal2: Calendar = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        return cal1.get(Calendar.DAY_OF_YEAR) === cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) === cal2.get(Calendar.YEAR)
    }

    fun checkToday(date: Long?): Boolean {
        val cal1: Calendar = Calendar.getInstance()
        cal1.time = convertLongToDate(date)
        val calNow: Calendar = Calendar.getInstance()
        return cal1.get(Calendar.DAY_OF_YEAR) === calNow.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) === calNow.get(Calendar.YEAR)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertToStringDate(dateLong: Long?): String {
        if (dateLong != null) {
            val date = convertLongToDate(dateLong)
            return SimpleDateFormat(MMddyyyy).format(date)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    fun convertToStringDate(dateLong: Long?, format: String?): String{
        if (dateLong != null) {
            val date = convertLongToDate(dateLong)
            return SimpleDateFormat(format).format(date)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDayMonthYearTimeToString(date: Date?): String {
        return try {
            val dateFormat = SimpleDateFormat(ddMMMyy)
            dateFormat.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDataToString(date : Long, format: String?): String{
        return try {
            val dateFormat = SimpleDateFormat(format)
            dateFormat.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun getSessionDay() : String{
        val c= Calendar.getInstance()
        return when (c.get(Calendar.HOUR_OF_DAY)) {
          in 5..11 -> {
              "Good Morning"
          }
          in 12..17 -> {
              "Good Afternoon"
          }
          else -> {
              "Good Evening"
          }
      }

    }

    fun showDateConversationToString(dateAt: Long?, context: Context?): String? {
        val date: Date = if (dateAt == null) Date() else Date(dateAt)
        date.let {
            val dateCurrent = Date()
            val deltaTime = dateCurrent.time - date.time
            val second = 1.0 * deltaTime / 1000
            val minus = second / 60
            val hours = minus / 60
            val day = hours / 24
            if (day >= 2) {
                return convertDayMonthYearTimeToString(date)
            }
            if (day >= 1) {
                return "" + day.toInt() + " " + context?.getString(R.string.dayAgo)
            }
            if (hours >= 1) {
                return "" + hours.toInt() + " " + context
                    ?.getString(R.string.hourAgo)
            }
            if (minus >= 1) {
                return "" + minus.toInt() + " " + context
                    ?.getString(R.string.minuteAgo)
            }
            return context?.getString(R.string.fewSeconds)
        }
    }

    @SuppressLint("NewApi")
    fun getDateMonday(localDate: LocalDate): LocalDate {
       return localDate.with(DayOfWeek.MONDAY)
    }


    fun convertToDateHourMinute(hour : Int, minute : Int,date : Date): Long{
        val cal = Calendar.getInstance()
        cal.time= date
        cal.set(Calendar.HOUR_OF_DAY,hour)
        cal.set(Calendar.MINUTE,minute)
        return cal.time.time
    }

    fun getHour(data : Long): Int{
        var c= Calendar.getInstance()
        c.time= Date(data)
        return c.get(Calendar.HOUR_OF_DAY)
    }

    fun getMinute(data : Long): Int{
        var c= Calendar.getInstance()
        c.time=Date(data)
        return c.get(Calendar.MINUTE)
    }

    // max 12h
    fun getHourCurrent() : Int{
        val c= Calendar.getInstance()
        return c.get(Calendar.HOUR)
    }

    fun getMinuteCurrent() : Int{
        val c= Calendar.getInstance()
        return c.get(Calendar.MINUTE)
    }

    fun getAmPmCurrent() : Int{
        val c= Calendar.getInstance()
        return c.get(Calendar.AM_PM)
    }

    fun getAmPmTimePicker() : AMPMHours.DayTime{
        if (getAmPmCurrent() == Calendar.AM) return AMPMHours.DayTime.AM
        return AMPMHours.DayTime.PM
    }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1


}