package com.app.ssn.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.smwc.Activity.LoginActivity.LoginResponse
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = loginRepository.userResponseLiveData


//    fun login(loginRequest: LoginData) {
//        viewModelScope.launch { loginRepository.loginUser(loginRequest) }
//    }

}
