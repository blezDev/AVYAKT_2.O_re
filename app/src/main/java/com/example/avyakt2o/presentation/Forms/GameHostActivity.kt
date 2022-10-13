package com.example.avyakt2o.presentation.Forms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.avyakt2o.R
import com.example.avyakt2o.databinding.ActivityGameHostBinding
import com.example.avyakt2o.presentation.MainActivity

class GameHostActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGameHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_game_host)
        var options = arrayOf("FIFA 19","NFS","Valorant","COD","Modern Warfare","PUBG")

        binding.opGameType.adapter =  ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)
        val navController = supportFragmentManager.findFragmentById(R.id.formHost)
            ?.findNavController()


        binding.opGameType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position)
                {
                    0 ->
                    {
                        val bundle = bundleOf("key" to "FIFA")
                        navController?.navigate(R.id.soloForm_Gaming_Fragment,bundle)

                    }
                    1->{

                        val bundle = bundleOf("key" to "NFS")
                        navController?.navigate(R.id.soloForm_Gaming_Fragment,bundle)


                    }
                    2->{
                        val bundle = bundleOf("key" to "VALORANT")
                        navController?.navigate(R.id.teamForm_GamingFragment,bundle)
                    }
                    3->{
                        val bundle = bundleOf("key" to "COD")
                        navController?.navigate(R.id.teamForm_GamingFragment,bundle)
                    }
                  4->{
                        val bundle = bundleOf("key" to "MODERN_WARFARE")
                        navController?.navigate(R.id.teamForm_GamingFragment,bundle)
                    }
                   5->{
                        val bundle = bundleOf("key" to "PUBG")
                        navController?.navigate(R.id.teamForm_GamingFragment,bundle)
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


    }
}