package olmo.wellness.android.data

import androidx.room.Database
import androidx.room.RoomDatabase
import olmo.wellness.android.data.model.category.dao.SubCategoryDAO
import olmo.wellness.android.data.model.category.dao.SubCategoryEntity
import olmo.wellness.android.data.model.user_info.UserInfoDAO
import olmo.wellness.android.data.model.user_info.UserInfoEntity

@Database(entities = [SubCategoryEntity::class, UserInfoEntity::class], version = 3, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun subCategoryDAO(): SubCategoryDAO

    abstract fun userInfoDAO(): UserInfoDAO
}