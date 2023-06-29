package com.vovance.securemywillcall.fragments.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovance.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    val homeResponseLiveData: LiveData<NetworkResult<HomeResponse>>
        get() = homeRepository.homeResponseLiveData

    fun home(token: String) {
        viewModelScope.launch { homeRepository.home(token) }
    }
}