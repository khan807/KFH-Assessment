package com.kfh.kfhassessment.data.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.kfh.kfhassessment.data.models.ConvertCurrenciesDO
import com.kfh.kfhassessment.data.models.CurrenciesDO
import com.kfh.kfhassessment.data.models.IBANValidationDO
import com.kfh.kfhassessment.data.repositories.MainRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommonViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val ibanValidationLiveData = MutableLiveData<JsonObject>()
    val getCurrenciesLiveData = MutableLiveData<JsonObject>()
    val convertCurrenciesLiveData = MutableLiveData<ConvertCurrenciesDO>()

    var job: Job? = null
    
    val loading = MutableLiveData<Boolean>()

    fun ibanValidation(ibanNumber:String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepository.ibanValidation(ibanNumber)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    ibanValidationLiveData.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Invalid IBAN Number")
                }
            }
        }

    }
    fun getCurrenciesList() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepository.getCurrenciesList()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                            getCurrenciesLiveData.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }
    fun convertCurrency(amount:String,to:String,from:String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepository.convertCurrency(amount,to,from)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    convertCurrenciesLiveData.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }


    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}