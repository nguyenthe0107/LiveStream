package olmo.wellness.android.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


@SuppressLint("NewApi")
fun LocalDate.toDate(): Date =
     Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())


@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDate() : LocalDate {
    val cal = GregorianCalendar()
    cal.time = this
    val zdt = cal.toZonedDateTime()
    return zdt.toLocalDateTime().toLocalDate()
}
