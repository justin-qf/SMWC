package com.app.smwc.fragments.Profile.ProfileFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.smwc.Activity.LoginActivity.LoginData
import com.app.smwc.Activity.LoginActivity.LoginResponse
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    val profileResponseLiveData: LiveData<NetworkResult<ProfileResponse>>
        get() = profileRepository.profileResponseLiveData

    fun profile(token: String) {
        viewModelScope.launch { profileRepository.userProfile(token) }
    }

}
