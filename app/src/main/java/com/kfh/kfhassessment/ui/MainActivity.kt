package com.kfh.kfhassessment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.kfh.kfhassessment.R
import com.kfh.kfhassessment.data.models.SymbolList
import com.kfh.kfhassessment.data.models.Symbols
import com.kfh.kfhassessment.data.network.ApiClient
import com.kfh.kfhassessment.data.repositories.MainRepository
import com.kfh.kfhassessment.data.viewmodels.CommonViewModel
import com.kfh.kfhassessment.data.viewmodels.MyViewModelFactory
import com.kfh.kfhassessment.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: CommonViewModel
    var currencies : ArrayList<SymbolList> = arrayListOf()
    var fromKey = ""
    var toKey = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = ApiClient.getInstance()
        val mainRepository = MainRepository(retrofitService)
        viewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository)).get(CommonViewModel::class.java)

        onClickListeners()

        apiObservers()


    }

    private fun apiObservers() {

        viewModel.getCurrenciesList()


        viewModel.apply {
            ibanValidationLiveData.observe(this@MainActivity){
                hideProgressBar()
                if(it.get("errors").asJsonNull!=null){
                    binding.tilIBAN.error = it.get("errors").asJsonObject?.get("iban_number")?.asJsonArray?.get(0)?.asString
                }else {
                    if (it?.get("valid")?.asBoolean == true) {
                        binding.tilIBAN.helperText =
                            getString(R.string.great_entered_iban_number_is_valid)
                    } else {
                        binding.tilIBAN.error = getString(R.string.invalid_iban_number)
                    }
                }
            }

            errorMessage.observe(this@MainActivity){
                hideProgressBar()

                binding.tilIBAN.error = errorMessage.value
                }


            getCurrenciesLiveData.observe(this@MainActivity){

                if((it.get("success").asBoolean == true) ){
                    it.getAsJsonObject("symbols").keySet().forEachIndexed { index, key ->
                        val req = SymbolList()
                        req.key = key
                        req.value = it.getAsJsonObject("symbols").get(key).asString
                        currencies.add(req)

                    }
                }

            }

            convertCurrenciesLiveData.observe(this@MainActivity){
                hideProgressBar(true)
                if(it.success == true){
                    binding.tvResult.apply {
                        setText("Result: ${binding.etAmount.text.toString()} ${fromKey} = ${it.result} ${toKey}")
                        visibility = View.VISIBLE
                    }
                }
            }

        }
    }

    private fun onClickListeners() {
        binding.apply {
            btnValidate.setOnClickListener {
                tilIBAN.isErrorEnabled = false
                if(tilIBAN.editText?.text.toString().trim().isEmpty()){
                    tilIBAN.error = getString(R.string.this_field_is_required)
                }else{
                    showProgressBar()
                    viewModel.ibanValidation(tilIBAN.editText?.text.toString())
                }

            }

            etFromCurrency.setOnClickListener {
                val bottomSheet = CurrenciesBottomSheet.newInstance(currencies,object: CurrenciesBottomSheet.OnItemClickLister{
                    override fun onClick(selectedValue: SymbolList) {
                        etFromCurrency.setText("${selectedValue.value} (${selectedValue.key})")
                        fromKey = selectedValue.key!!


                    }

                })
                bottomSheet.show(supportFragmentManager,"")

            }
            etToCurrency.setOnClickListener {
                val bottomSheet = CurrenciesBottomSheet.newInstance(currencies,object: CurrenciesBottomSheet.OnItemClickLister{
                    override fun onClick(selectedValue: SymbolList) {
                        etToCurrency.setText("${selectedValue.value} (${selectedValue.key})")
                        toKey = selectedValue.key!!


                    }

                })
                bottomSheet.show(supportFragmentManager,"")

            }

            btnConvert.setOnClickListener {
                tilFrom.isErrorEnabled = false
                tilTO.isErrorEnabled = false
                tilAmount.isErrorEnabled = false
                if(fromKey.isEmpty()){
                    tilFrom.error = getString(R.string.this_field_is_required)
                }else if(toKey.isEmpty()){
                    tilTO.error = getString(R.string.this_field_is_required)

                }else if(etAmount.text.toString().trim().isEmpty()){
                    tilAmount.error = getString(R.string.this_field_is_required)

                }else{
                    showProgressBar(true)
                    viewModel.convertCurrency(amount =etAmount.text.toString().trim(),
                        to = toKey, from = fromKey)
                }
            }



        }

    }

    private fun showProgressBar(isConvertProgress:Boolean?=false) {
        if(isConvertProgress == true){
            binding.apply {
                convertProgressBar.visibility = View.VISIBLE
                btnConvert.visibility = View.GONE
            }
        }else{
            binding.apply {
                progressBar.visibility = View.VISIBLE
                btnValidate.visibility = View.GONE
            }
        }

    }
    private fun hideProgressBar(isConvertProgress:Boolean?=false) {
        if(isConvertProgress == true){
            binding.apply {
                convertProgressBar.visibility = View.GONE
                btnConvert.visibility = View.VISIBLE
            }
        }else{
            binding.apply {
                progressBar.visibility = View.GONE
                btnValidate.visibility = View.VISIBLE
            }
        }

    }



}