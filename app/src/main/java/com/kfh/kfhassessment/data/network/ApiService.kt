package com.kfh.kfhassessment.data.network


import com.google.gson.JsonObject
import com.kfh.kfhassessment.data.models.ConvertCurrenciesDO
import com.kfh.kfhassessment.data.models.CurrenciesDO
import com.kfh.kfhassessment.data.models.IBANValidationDO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-Type:application/json")
    @GET("bank_data/iban_validate")
    suspend fun ibanValidation(@Query("iban_number") iban_number:String): Response<JsonObject>

    @Headers("Content-Type:application/json")
    @GET("fixer/symbols")
    suspend fun getCurrenciesList(): Response<JsonObject>

    @Headers("Content-Type:application/json")
    @GET("fixer/convert")
    suspend fun convertCurrency(@Query("amount") amount:String,@Query("to") to:String,
                                @Query("from") from:String): Response<ConvertCurrenciesDO>

}