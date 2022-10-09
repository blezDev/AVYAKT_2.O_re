package com.example.avyakt2o.presentation.galery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.avyakt2o.Adapter.GaleryAdapter
import com.example.avyakt2o.R
import com.example.avyakt2o.data.Galery
import com.example.avyakt2o.databinding.FragmentGaleryBinding


class GaleryFragment : Fragment() {
    private lateinit var binding: FragmentGaleryBinding
    private lateinit var GaleryAdapter : GaleryAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_galery, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GaleryAdapter = GaleryAdapter(listOf(Galery(R.drawable.csgo,"CSGO")))
        binding.recyclerView.adapter = GaleryAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
    }
}