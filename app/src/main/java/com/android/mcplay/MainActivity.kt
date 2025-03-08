package com.android.mcplay

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.mcplay.controler.DBClient
import com.android.mcplay.controler.SharedViewModel
import com.android.mcplay.databinding.ActivityMainBinding
import com.android.mcplay.view.AddClient
import com.android.mcplay.view.Config
import com.android.mcplay.view.Home
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pranavpandey.android.dynamic.toasts.DynamicToast

/**
 * Está classe está com o sistema de navegação do app
 *
 * @author Marco Augusto
 */

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: SharedViewModel
    private val dbClient = DBClient(this)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dbClient.updateRestDaysForAllClients()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Home(dbClient.getAllClientsByRestDays())).commit()
        setupNavigation()
        binding.navigationView.setOnNavigationItemSelectedListener(this)

        binding.btnSearch.setOnClickListener {
            val search = binding.fieldSearch.text.toString()
            navigation(Home(dbClient.getAllClientsBySearch(search, this)))
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                navigation(Home(dbClient.getAllClientsByRestDays()))
                binding.btnSearch.visibility = View.VISIBLE
                binding.fieldSearch.visibility = View.VISIBLE
                binding.txtTitle.visibility = View.GONE
                binding.fieldSearch.text.clear()
            }

            R.id.nav_add -> {
                navigation(AddClient())
                binding.btnSearch.visibility = View.GONE
                binding.fieldSearch.visibility = View.GONE
                binding.txtTitle.visibility = View.VISIBLE
            }

            R.id.nav_config -> {
                navigation(Config())
                binding.btnSearch.visibility = View.GONE
                binding.fieldSearch.visibility = View.GONE
                binding.txtTitle.visibility = View.VISIBLE
            }
        }
        return true
    }

    // Função responsável por outros fuxos de fragment
    private fun setupNavigation() {
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.getCurrentFragment().observe(this) {

            if (it.toString().substring(0, 4) == "Home") {
                binding.btnSearch.visibility = View.VISIBLE
                binding.fieldSearch.visibility = View.VISIBLE
                binding.txtTitle.visibility = View.GONE
                binding.fieldSearch.text.clear()
                navigation(it)
            } else {
                navigation(it)
                binding.txtTitle.visibility = View.VISIBLE
                binding.fieldSearch.visibility = View.GONE
                binding.btnSearch.visibility = View.GONE
            }

        }
    }

    private fun navigation(fragment: Fragment) {
        Log.d("Log - McPlay", "Fragment: $fragment")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
