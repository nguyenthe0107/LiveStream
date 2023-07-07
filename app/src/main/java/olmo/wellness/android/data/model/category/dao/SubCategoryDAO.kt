package olmo.wellness.android.data.model.category.dao

import androidx.room.*

@Dao
interface SubCategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSubCategory(subCategoryEntity: SubCategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategories(subCategories: List<SubCategoryEntity?>)

    @Query("select * from sub_category_table")
    suspend fun getAllSubCategories(): List<SubCategoryEntity>

    @Query("delete from sub_category_table")
    fun deleteAllSubCategories()

    @Delete
    fun deleteSubCategory(subCategoryEntity: SubCategoryEntity);

    @Query("select * from sub_category_table where id = :id")
    fun getSubCategory(id: Int): SubCategoryEntity
}