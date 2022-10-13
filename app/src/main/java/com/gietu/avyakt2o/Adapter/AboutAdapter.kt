package com.gietu.avyakt2o.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.gietu.avyakt2o.data.PersonDetails
import com.gietu.avyakt2o.databinding.AboutViewBinding

class AboutAdapter(private val personList : List<PersonDetails>) : RecyclerView.Adapter<AboutAdapter.ItemView>() {
    var onItemClickGithub : ((PersonDetails) -> Unit)? = null
    var onItemClickInstagram : ((PersonDetails) -> Unit)? = null
    var onItemClickLinkedin : ((PersonDetails) -> Unit)? = null
    private lateinit var binding : AboutViewBinding
    inner class ItemView(binding: AboutViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        binding = AboutViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemView(binding)
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        binding.NameText.text = "${personList[position].name}"
        binding.descriptionText.text = "${personList[position].description}"
        binding.profileImage.setImageResource(personList[position].image)
        val animation = AlphaAnimation(0.5f,0f)
        animation
            .apply {
                duration = 700
                interpolator = LinearInterpolator()
                repeatCount = Animation.INFINITE
                repeatMode = Animation.REVERSE
            }
        binding.githubImg.setOnClickListener {
            onItemClickGithub?.invoke(personList[position])
        }
        binding.instagramImg.setOnClickListener {
            onItemClickInstagram?.invoke(personList[position])
        }
        binding.linkedinImg.setOnClickListener {
            onItemClickLinkedin?.invoke(personList[position])
        }
    }

    override fun getItemCount(): Int {
       return personList.size
    }

}