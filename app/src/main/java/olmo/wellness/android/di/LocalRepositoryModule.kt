package olmo.wellness.android.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import olmo.wellness.android.data.LocalDatabase
import olmo.wellness.android.data.model.category.dao.SubCategoryDAO
import olmo.wellness.android.data.model.user_info.UserInfoDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalRepositoryModule {

    // local module
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(context, LocalDatabase::class.java, "olmo_wellness.database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun subCategoryDao(database: LocalDatabase): SubCategoryDAO {
        return database.subCategoryDAO()
    }

    @Singleton
    @Provides
    fun userInfoDao(database: LocalDatabase): UserInfoDAO {
        return database.userInfoDAO()
    }
}