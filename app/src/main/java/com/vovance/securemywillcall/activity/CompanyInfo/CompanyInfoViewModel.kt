package com.vovance.securemywillcall.activity.CompanyInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovance.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val companyInfoRepository: CompanyInfoRepository,
) : ViewModel() {

    val companyResponseLiveData: LiveData<NetworkResult<CompanyInfoResponse>>
        get() = companyInfoRepository.companyResponseLiveData

    fun addCompany(addCompanyRequest: CompanyData, token: String) {
        viewModelScope.launch { companyInfoRepository.addCompany(addCompanyRequest, token) }
    }
}
