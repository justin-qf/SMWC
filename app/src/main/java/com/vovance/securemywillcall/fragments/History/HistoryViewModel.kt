package com.vovance.securemywillcall.fragments.History

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovance.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    val historyResponseLiveData: LiveData<NetworkResult<HistoryResponse>>
        get() = historyRepository.historyResponseLiveData

    fun history(token: String) {
        viewModelScope.launch { historyRepository.history(token) }
    }
}
