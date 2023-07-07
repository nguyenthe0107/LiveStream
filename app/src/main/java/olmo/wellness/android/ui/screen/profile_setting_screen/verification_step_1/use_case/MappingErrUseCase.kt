package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.use_case

import olmo.wellness.android.data.model.verification_1.response.VerificationDetailDTOResponse

class MappingErrUseCase{
    fun getErrList(verificationDetail: List<VerificationDetailDTOResponse>?) : List<VerificationDetailDTOResponse>?{
        if(verificationDetail?.isNullOrEmpty() == true){
           return emptyList()
        }
        return verificationDetail
    }
}