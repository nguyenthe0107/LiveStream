package olmo.wellness.android.data.model.business

data class StoreDetailRequest(
    val businessId : Int?=null,
    val name : String ?= null,
    val ownerSeller: String ?= null,
    val productSubCategories: List<ProductSubCategoryModel> ?= null
)
