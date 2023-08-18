package olmo.wellness.android.ui.screen.category_screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.use_case.GetProductCategoriesFromLocalUseCase
import olmo.wellness.android.domain.use_case.GetProductCategoriesFromServiceUseCase
import olmo.wellness.android.domain.use_case.SetProductCategoriesUseCase
import javax.inject.Inject

@HiltViewModel
class CategoryScreenViewModel @Inject constructor(
    private val getProductCategoriesFromServiceUseCase: GetProductCategoriesFromServiceUseCase,
    private val setProductCategoriesUseCase: SetProductCategoriesUseCase,
    private val getProductCategoriesUseCase: GetProductCategoriesFromLocalUseCase,
) : ViewModel() {

    private val _expandedIdsList = MutableStateFlow(listOf<Int>())
    val expandedIdsList: StateFlow<List<Int>> = _expandedIdsList

    private val _categoryList = MutableStateFlow(listOf<LiveCategory>())
    val categoryList: StateFlow<List<LiveCategory>> = _categoryList

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasCategoryChoose = MutableStateFlow(false)
    val hasCategoryChoose: StateFlow<Boolean> = _hasCategoryChoose

    private val page = mutableStateOf(1)
    private val isLoadMore = mutableStateOf(false)

    init {
        getCategoryList()
    }

    fun isLoadMore(index: Int): Boolean {
        return ((index + 1) >= _categoryList.value.size) && isLoadMore.value
    }

    fun onLoadMore() {
        page.value = page.value + 1
        getCategoryList()
    }

    fun onRefresh() {
        _categoryList.value = emptyList()
        getCategoryList()
    }

    private fun getCategoryList() {
        Log.e("WTF", "  getCategoryList() ")
        viewModelScope.launch {
            getProductCategoriesFromServiceUseCase(
                GetProductCategoriesFromServiceUseCase.Params(
                    page.value
                )
            ).collectLatest {
                when (it) {
                    is Result.Success -> {
                        _isLoading.value = false
                        _error.value = ""

                        isLoadMore.value = !it.data.isNullOrEmpty()

                        it.data?.let { lists ->
                            val currentCategories = ArrayList(_categoryList.value)
                            currentCategories.addAll(lists)
                            _categoryList.value =
                                currentCategories.filterNot { cate ->
                                    cate?.categories?.isEmpty() == true }
                        }
                    }
                    is Result.Error -> {
                        Log.e("WTF", " error ${it?.message}")
                        it.message?.let { errorMessage ->
                            _error.value = errorMessage
                        }
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    fun onCategoryExpandClicked(categoryId: Int) {
        _expandedIdsList.value = _expandedIdsList.value.toMutableList().also { list ->
            if (list.contains(categoryId))
                list.remove(categoryId)
            else
                list.add(categoryId)
        }
    }

    fun requestExpandCategory(categoryId: Int) {
        _expandedIdsList.value = _expandedIdsList.value.toMutableList().also { list ->
            if (!list.contains(categoryId))
                list.add(categoryId)
        }
    }

    fun onSubCategoryClicked(categoryId: Int, subCateGoryId: Int) {
        _categoryList.value = _categoryList.value.toMutableList().also { categories ->
            categories.first { it.id == categoryId }.also { category ->
                val subCategoryList = category.categories?.toMutableList()
                subCategoryList?.indexOf(subCategoryList.firstOrNull { sub -> sub.id == subCateGoryId })
                    ?.let { index ->
                        if ( subCategoryList.isNotEmpty() ) {
                            subCategoryList[index] =
                                subCategoryList[index].copy(isSelected = !subCategoryList[index].isSelected!!)
                        }
                    }

                categories[categories.indexOf(category)] =
                    categories[categories.indexOf(category)].copy(categories = subCategoryList)
            }
        }
        checkSelect()
    }

    private fun checkSelect() {
        var flag = false
        _categoryList.value.forEach { liveCategory ->
            liveCategory.categories?.forEach {
                if (it.isSelected) {
                    flag = true
                }
            }
        }
        _hasCategoryChoose.value = flag
    }


    fun onNextButtonClicked() {
        viewModelScope.launch {
            setProductCategoriesUseCase(SetProductCategoriesUseCase.Params( getListSelected()))
        }
    }

    fun getListSelected()  : MutableList<LiveCategory>{
        val temp = mutableListOf<LiveCategory>()
        _categoryList.value.forEach { liveCategory ->
            liveCategory.categories?.forEach {
                if (it.isSelected) {
                    temp.add(it)
                }
            }
        }
        return temp
    }
}