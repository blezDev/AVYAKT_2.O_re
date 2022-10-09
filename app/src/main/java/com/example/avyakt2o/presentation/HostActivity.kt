package com.example.avyakt2o.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.avyakt2o.R
import com.example.avyakt2o.databinding.ActivityHostBinding
import com.example.avyakt2o.presentation.login.Login
import com.example.avyakt2o.utils.TokenManager

class HostActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHostBinding
    private lateinit var actionBarDrawerToggle :ActionBarDrawerToggle
    private lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_host)
        actionBarDrawerToggle = ActionBarDrawerToggle(this,binding.HostdrawerLayout,R.string.open,R.string.close)
        binding.HostdrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tokenManager = TokenManager(applicationContext)
        val navController = supportFragmentManager.findFragmentById(R.id.hostFragment)
            ?.findNavController()
        if (navController != null) {
            binding.navView.setupWithNavController(navController)
        }
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.nav_logout ->{
                    tokenManager.deteleCredit()
                    val intent = Intent(this@HostActivity,Login::class.java)
                    startActivity(intent)
                    finish()
                }
              R.id.homeFragment ->
              {
                 navController?.navigate(R.id.homeFragment)
              }
                R.id.galeryFragment ->
                {
                    navController?.navigate(R.id.galeryFragment)
                }

                R.id.aboutFragment ->
                {
                    navController?.navigate(R.id.aboutFragment)
                }


            }
            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}