package olmo.wellness.android.domain.model.wrapper

data class NumberCustomerWrapper(
    var numberOfAdult : Int?=null,
    var numberOfChild: Int?=null,
    var numberOfOptionalAdult: Int?=null,
    val numberOfOptionalChild: Int?=null,
    val timeStamp: Long ?= System.currentTimeMillis()
)
