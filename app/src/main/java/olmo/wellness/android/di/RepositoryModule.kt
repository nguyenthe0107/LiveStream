package olmo.wellness.android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import olmo.wellness.android.data.ApiChatService
import olmo.wellness.android.data.ApiIDTypeService
import olmo.wellness.android.data.ApiService
import olmo.wellness.android.data.ApiUploadService
import olmo.wellness.android.data.model.category.dao.SubCategoryDAO
import olmo.wellness.android.data.model.user_info.UserInfoDAO
import olmo.wellness.android.data.repository.*
import olmo.wellness.android.di.wrapper.AccessTokenWrapper
import olmo.wellness.android.domain.repository.*
import olmo.wellness.android.domain.use_case.GetSessionUseCase
import olmo.wellness.android.domain.use_case.GetUserInfoUseCase
import olmo.wellness.android.domain.use_case.SetUserInfoUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideApiRepository(
        @ApplicationContext appContext: Context,
        apiService: ApiService
    ): ApiRepository = ApiRepositoryImpl(appContext, apiService)

    @Provides
    @Singleton
    fun provideApiChatRepository(
        @ApplicationContext appContext: Context,
        apiService: ApiChatService
    ): ApiChatRepository = ApiChatRepositoryImpl(appContext, apiService)

    @Provides
    @Singleton
    fun provideApiIDRepository(
        @ApplicationContext appContext: Context,
        apiService: ApiIDTypeService
    ): ApiIDTypeRepository = ApiIDTypeRepositoryImpl(appContext, apiService)

    @Provides
    @Singleton
    fun provideApiUploadRepository(
        @ApplicationContext appContext: Context,
        apiService: ApiUploadService
    ): ApiUploadRepository = ApiIUploadRepositoryImpl(appContext, apiService)

    @Provides
    @Singleton
    fun provideLocalRepository(
        @ApplicationContext appContext: Context,
        subCategoryDAO: SubCategoryDAO,
        userInfoDAO: UserInfoDAO
    ): LocalRepository = LocalRepositoryImpl(appContext, subCategoryDAO, userInfoDAO)

    @Provides
    @Singleton
    fun provideAccessTokenWrapper(
        getUserInfoUseCase: GetUserInfoUseCase,
        setUserInfoUseCase: SetUserInfoUseCase,
        getSessionUseCase: GetSessionUseCase,
    ): AccessTokenWrapper {
        return AccessTokenWrapper(
            getUserInfoUseCase,
            setUserInfoUseCase,
            getSessionUseCase
        )
    }

    @Provides
    fun provideLiveStreamRepository(repository: RTCLiveStreamRepositoryImpl): RTCLiveStreamRepository = repository

    @Provides
    fun provideSessionRepository(repository: RTCConnectionRepositoryImpl) : RTCConnectionRepository = repository

    @Provides
    fun providePrivateChatRepository(repository: RTCPrivateChatRepositoryImpl) : RTCPrivateChatRepository = repository

    @Provides
    fun provideBookingServiceRepository(repository: RTCBookingServiceRepositoryImpl) : RTCBookingServiceRepository = repository
}
