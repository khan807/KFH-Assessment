package com.kfh.kfhassessment.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kfh.kfhassessment.data.models.SymbolList
import com.kfh.kfhassessment.databinding.FragmentCurrenciesBottomSheetBinding


class CurrenciesBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: FragmentCurrenciesBottomSheetBinding
    lateinit var currencyAdapter : CurrencyAdapter


    interface OnItemClickLister {
        fun onClick(selectedValue:SymbolList)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrenciesBottomSheetBinding.inflate(layoutInflater, container, false)

        currencyAdapter = CurrencyAdapter(symbolList,object:CurrencyAdapter.onClickListener{
            override fun onClick(itemData: SymbolList) {
                onItemClickLister.onClick(itemData)
                dismiss()
            }

        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                performSearch()
            }
            true
        }


        binding.rvCurrencies.adapter = currencyAdapter




        return binding.root
    }

    private fun performSearch() {
        val searchList :ArrayList<SymbolList> = arrayListOf()

        symbolList?.forEachIndexed { index, listData ->
            if(listData.value.toString().contains(binding.etSearch.text.toString(),true)){
                searchList.add(listData)
            }
        }
        currencyAdapter.addItems(searchList)

        if(searchList.size == 0){
            Snackbar.make(binding.root,"No record found", Snackbar.LENGTH_LONG).show()
        }

    }


    companion object {

        lateinit var symbolList :ArrayList<SymbolList>
        lateinit var onItemClickLister: OnItemClickLister

        @JvmStatic
        fun newInstance(listData: ArrayList<SymbolList>,onClickListener:OnItemClickLister) =
            CurrenciesBottomSheet().apply {
                symbolList = listData
                onItemClickLister= onClickListener

            }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {

        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}