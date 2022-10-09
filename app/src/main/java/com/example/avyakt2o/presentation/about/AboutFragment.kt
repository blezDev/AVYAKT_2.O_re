package com.example.avyakt2o.presentation.about

import android.content.Intent
import android.graphics.drawable.GradientDrawable.Orientation
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.avyakt2o.Adapter.AboutAdapter
import com.example.avyakt2o.R
import com.example.avyakt2o.data.PersonDetails
import com.example.avyakt2o.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private lateinit var binding:FragmentAboutBinding
    private lateinit var aboutAdapterAndroid : AboutAdapter
    private lateinit var aboutAdapterBackend : AboutAdapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_about,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val details = listOf<PersonDetails>( PersonDetails(R.drawable.pawn,"Pawn Sharma\n Core Team","!false\n" +"(It’s funny because it’s true.)","https://github.com/Pawansharma08","https://instagram.com/mr.sharma__ji_?igshid=YmMyMTA2M2Y=","https://www.linkedin.com/in/pawan-sharma-978ba3204/"),
            PersonDetails(R.drawable.sai,"M Saikrishna Pattnaik \n Core Team","“Debugging” is like being the detective in a crime drama where you are also the murderer.","https://github.com/blezDev","https://www.instagram.com/kaiju837/?r=nametag","https://www.linkedin.com/in/m-saikrishna-pattnaik-261013211"))
        val backendDetails = listOf(PersonDetails(R.drawable.ayush,"Ayush Sharma \n Backend","//be nice to the CPU\n" +
                "Thread_sleep(1);","https://github.com/This-is-Ayush-Sharma","https://www.instagram.com/_ayush.aj/","https://www.linkedin.com/in/ayush-sharma-551133213/"))

        aboutAdapterAndroid = AboutAdapter(details)
        aboutAdapterBackend = AboutAdapter(backendDetails)

        binding.apply {
            AndroidRecyclerView.adapter = aboutAdapterAndroid
            AndroidRecyclerView.layoutManager = GridLayoutManager(requireContext(),1,GridLayoutManager.VERTICAL,false)
            aboutAdapterAndroid.apply {
                onItemClickGithub = {
                    val intent = Intent(Intent.ACTION_VIEW,Uri.parse(it.github))
                    startActivity(intent)
                }
                onItemClickInstagram = {
                    val intent = Intent(Intent.ACTION_VIEW,Uri.parse(it.instagram))
                    startActivity(intent)
                }
                onItemClickLinkedin = {
                    val intent = Intent(Intent.ACTION_VIEW,Uri.parse(it.linkedin))
                    startActivity(intent)
                }
            }





            BackendRecyclerView.adapter = aboutAdapterBackend
            BackendRecyclerView.layoutManager = GridLayoutManager(requireContext(),1,GridLayoutManager.HORIZONTAL,false)
            aboutAdapterBackend.apply {
                onItemClickGithub = {
                    val intent = Intent(Intent.ACTION_VIEW,Uri.parse(it.github))
                    startActivity(intent)
                }
                onItemClickInstagram = {
                    val intent = Intent(Intent.ACTION_VIEW,Uri.parse(it.instagram))
                    startActivity(intent)
                }
                onItemClickLinkedin = {
                    val intent = Intent(Intent.ACTION_VIEW,Uri.parse(it.linkedin))
                    startActivity(intent)
                }
            }

        }
    }

}