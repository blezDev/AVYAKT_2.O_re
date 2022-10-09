package com.example.avyakt2o.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.avyakt2o.R
import com.example.avyakt2o.data.EventList
import com.example.avyakt2o.databinding.RegModelBinding

class RegistrationReAdapter(private val formList: List<EventList>, private val context: Context):RecyclerView.Adapter<RegistrationReAdapter.RegViewHolder>() {
    private lateinit var binding : RegModelBinding
      var onItemClick : ((EventList)-> Unit) ?= null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.reg_model, parent, false)
        return RegViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegViewHolder, position: Int) {

        val Reg = formList[position]
        Glide.with(context).load(formList[position].PosterUrl).into(binding.regImage)

        binding.ProgramName.text = Reg.EventName
        binding.coordinatorName.text = Reg.CoordinatorName[0]
        binding.phoneNumber.text = Reg.Number[0]
        binding.btnRegForm.setOnClickListener {
        onItemClick?.invoke(Reg)

        }
    }

    override fun getItemCount(): Int {
        return formList.size
    }


    inner class RegViewHolder(binding : RegModelBinding) : RecyclerView.ViewHolder(binding.root)

}