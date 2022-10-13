package com.example.avyakt2o.presentation.sars

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.avyakt2o.R


class SarsHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sars_host)
        val opSarsType = findViewById<Spinner>(R.id.op_sarsType)

        var options = arrayOf("Robo Soccer","Taskmaster","AquaRobo","Line Follower")

        opSarsType.adapter =  ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)
        val navController = supportFragmentManager.findFragmentById(R.id.sars_host)
            ?.findNavController()

        val bundle = bundleOf("key" to "ROBO_SOCCER")
        navController?.navigate(R.id.teamForm_GamingFragment)
        opSarsType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               when(position){
                   0->{
                            val bundle = bundleOf("key" to "ROBO_SOCCER")
                            navController?.navigate(R.id.teamForm_GamingFragment,bundle)
                   }
                   1->{
                       val bundle = bundleOf("key" to "TASKMASTER")
                       navController?.navigate(R.id.teamForm_GamingFragment,bundle)
                   }
                   2->{
                       val bundle = bundleOf("key" to "AQUAROBO")
                       navController?.navigate(R.id.teamForm_GamingFragment,bundle)
                   }
                   3->{
                       val bundle = bundleOf("key" to "LINE_FOLLOWER")
                       navController?.navigate(R.id.teamForm_GamingFragment,bundle)
                   }
               }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val bundle = bundleOf("key" to "ROBO_SOCCER")
                navController?.navigate(R.id.teamForm_GamingFragment)
            }
        }
    }
}