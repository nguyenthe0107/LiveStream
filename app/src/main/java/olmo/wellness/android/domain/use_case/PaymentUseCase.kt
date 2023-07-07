package olmo.wellness.android.domain.use_case

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.order.OrderRequestBody
import olmo.wellness.android.domain.model.bank.BankInfo
import olmo.wellness.android.domain.model.order.OrderPaymentResponse
import olmo.wellness.android.domain.repository.ApiRepository
import olmo.wellness.android.domain.tips.CoinInfo
import olmo.wellness.android.domain.tips.PricePackageInfo
import javax.inject.Inject

class PaymentUseCase @Inject constructor(private val repository: ApiRepository) {
    fun getTotalCoin() : Flow<Result<CoinInfo>> = repository.getTotalCoin()
    fun getUserBankAccounts(): Flow<Result<List<BankInfo>>> = repository.getUserBankAccount()
    fun getPricePackageInfo(idPackage: Int) : Flow<Result<PricePackageInfo>> = repository.getPricePackage(idPackage = idPackage)
    fun createOrder(bodyRequest: OrderRequestBody) : Flow<Result<OrderPaymentResponse>> = repository.createOrder(bodyRequest)
}