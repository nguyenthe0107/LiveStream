package olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data

import java.time.LocalDate

data class KalendarMoney (val date: LocalDate,
                          val money: Float?,
                          val timeStamp: Long?=null,
                          val typeSession: String?=null,
                          val id: Double?=null
)