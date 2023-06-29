package com.vovance.securemywillcall.activity.EditProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovance.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val updateProfileRepository: UpdateProfileRepository,
) : ViewModel() {

    val updateProfileResponseLiveData: LiveData<NetworkResult<EditProfileResponse>>
        get() = updateProfileRepository.updateProfileResponseLiveData

    fun updateProfile(addCompanyRequest: UpdateProfileData, image: MultipartBody.Part, token: String) {
        viewModelScope.launch {
            updateProfileRepository.updateProfile(
                addCompanyRequest,
                token,
                image
            )
        }
    }
}
