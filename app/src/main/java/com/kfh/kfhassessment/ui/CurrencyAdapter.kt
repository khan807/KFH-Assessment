package com.kfh.kfhassessment.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kfh.kfhassessment.data.models.SymbolList
import com.kfh.kfhassessment.databinding.CurrenciesRvItemBinding

class CurrencyAdapter(var listdata: ArrayList<SymbolList>, var onItemClickListener: onClickListener) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    var checkedPosition = -1

    fun addItems(list:ArrayList<SymbolList>){
        checkedPosition = -1
        listdata = list
        notifyDataSetChanged()
    }


    interface onClickListener{
        fun onClick(itemData:SymbolList)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            CurrenciesRvItemBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = listdata[position]

        /*  Picasso.get().load(itemData.icon!!).placeholder(R.drawable.ic_english)
              .into(holder.binding.icon)*/
        holder.binding.tvValue.text = "${itemData.value} (${itemData.key})"

        if(position == checkedPosition){
            itemChecked(holder)
        }else{
            itemUnCheck(holder)
        }

        holder.itemView.setOnClickListener {
            itemChecked(holder)
            notifyItemChanged(checkedPosition)
            checkedPosition = holder.adapterPosition
            onItemClickListener.onClick(itemData)



        }


    }

    private fun itemUnCheck(holder: ViewHolder) {
        holder.binding.ivChecked.visibility = View.GONE


    }

    private fun itemChecked(holder: ViewHolder) {
        holder.binding.ivChecked.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    class ViewHolder(var binding: CurrenciesRvItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}