package com.kfh.kfhassessment.data.repositories

import com.kfh.kfhassessment.data.network.ApiService


class MainRepository constructor(private val retrofitService: ApiService) {

    suspend fun ibanValidation(ibanNumber:String) = retrofitService.ibanValidation(iban_number = ibanNumber)
    suspend fun getCurrenciesList() = retrofitService.getCurrenciesList()
    suspend fun convertCurrency(amount:String,to:String,from:String) =
        retrofitService.convertCurrency(amount = amount, to = to, from = from)

}