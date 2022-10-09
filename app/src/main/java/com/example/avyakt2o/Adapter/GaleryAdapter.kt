package com.example.avyakt2o.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.avyakt2o.data.Galery
import com.example.avyakt2o.databinding.GaleryViewBinding

class GaleryAdapter(val galeries: List<Galery>) : RecyclerView.Adapter<GaleryAdapter.ItemView>() {
    private lateinit var binding : GaleryViewBinding
    inner class ItemView(binding: GaleryViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        binding = GaleryViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemView(binding)
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
      binding.galeryImage.setImageResource(galeries[position].Image)
        binding.galeryInfoText.text = galeries[position].info
    }

    override fun getItemCount(): Int {
       return galeries.size
    }

}