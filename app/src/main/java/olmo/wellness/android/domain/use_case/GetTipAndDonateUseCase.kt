package olmo.wellness.android.domain.use_case

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.domain.repository.ApiRepository
import olmo.wellness.android.domain.tips.TipsPackageOptionInfo
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.tips.PackageOptionInfo
import javax.inject.Inject

class GetTipAndDonateUseCase @Inject constructor(val repository: ApiRepository) {

    suspend fun getTipsPackage(query: String?=null,
                               projection: String?=null,
                               page: Int?=null,
                               limit: Int?=null
        ): Flow<Result<List<TipsPackageOptionInfo>>> = repository.getTipPackagesOptions(query = query, projection = projection,
        page=page, limit = limit)

    suspend fun getPackageOptions(query: String?=null,
                               projection: String?=null,
                               page: Int?=null,
                               limit: Int?=null
    ): Flow<Result<List<PackageOptionInfo>>> = repository.getPackagesOptions(query = query, projection = projection,
        page=page, limit = limit)
}