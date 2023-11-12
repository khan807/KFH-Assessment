package com.kfh.kfhassessment.data.models

import com.google.gson.annotations.SerializedName


data class IBANValidationDO (

  @SerializedName("valid"                ) var valid              : Boolean?  = null,
  @SerializedName("message"              ) var message            : String?   = null,
  @SerializedName("iban"                 ) var iban               : String?   = null,
  @SerializedName("iban_data"            ) var ibanData           : IbanData? = null,
  @SerializedName("bank_data"            ) var bankData           : BankData? = null,
  @SerializedName("country_iban_example" ) var countryIbanExample : String?   = null,
  @SerializedName("errors"  ) var errors  : Errors? = null

)
data class Errors (

  @SerializedName("iban_number" ) var ibanNumber : ArrayList<String> = arrayListOf()

)
data class IbanData (

  @SerializedName("country"                      ) var country                  : String?  = null,
  @SerializedName("country_code"                 ) var countryCode              : String?  = null,
  @SerializedName("sepa_country"                 ) var sepaCountry              : Boolean? = null,
  @SerializedName("checksum"                     ) var checksum                 : String?  = null,
  @SerializedName("BBAN"                         ) var BBAN                     : String?  = null,
  @SerializedName("bank_code"                    ) var bankCode                 : String?  = null,
  @SerializedName("account_number"               ) var accountNumber            : String?  = null,
  @SerializedName("branch"                       ) var branch                   : String?  = null,
  @SerializedName("national_checksum"            ) var nationalChecksum         : String?  = null,
  @SerializedName("country_iban_format_as_swift" ) var countryIbanFormatAsSwift : String?  = null,
  @SerializedName("country_iban_format_as_regex" ) var countryIbanFormatAsRegex : String?  = null

)

data class BankData (

  @SerializedName("bank_code" ) var bankCode : String? = null,
  @SerializedName("name"      ) var name     : String? = null,
  @SerializedName("zip"       ) var zip      : String? = null,
  @SerializedName("city"      ) var city     : String? = null,
  @SerializedName("bic"       ) var bic      : String? = null

)