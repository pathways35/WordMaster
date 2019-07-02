package com.example.wordmaster

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.wordmaster.database.WordDatabaseDao
import com.example.wordmaster.database.WordMasterDatabase
import com.example.wordmaster.databinding.ActivityMainBinding
import com.example.wordmaster.databinding.FragmentAboutBindingImpl
import com.example.wordmaster.screens.about.AboutViewModel
import com.example.wordmaster.screens.about.AboutViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var db: WordDatabaseDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //First time app launch routine
        val sharedPref = this.getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
        val startState = sharedPref.getBoolean(getString(R.string.saved_launch_state_key), true)


        if(startState){

            //show the about screen
            val aboutBinding: FragmentAboutBindingImpl = DataBindingUtil.setContentView(this, R.layout.fragment_about)
            val application = requireNotNull(this).application
            db = WordMasterDatabase.getInstance(application).wordDatabaseDao
            val viewModelFactory = AboutViewModelFactory(db,application)

            //get a reference to the game view model from the provider. lifecycle library creates the view model
            val viewModel = ViewModelProviders.of(this, viewModelFactory).get(AboutViewModel::class.java)

            aboutBinding.aboutViewModel = viewModel

            //the binding can now observer LiveData updates
            aboutBinding.setLifecycleOwner(this)

            viewModel.onSkipClicked.observe(this, Observer { isClicked->
                if(isClicked) {
                    goToGame()
                   //viewModel.onSkipComplete()
                }
            })


            with (sharedPref.edit()) {
                putBoolean(getString(R.string.saved_launch_state_key), false)
                commit()
            }

        } else {

            goToGame()

        }


    }

    private fun goToGame() {
        //using Databinding for accessing the layout UI

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.myNavHostFragment)

        //hookup NavigationUI to ActionBar
        NavigationUI.setupActionBarWithNavController(this, navController)
    }


    override fun onSupportNavigateUp(): Boolean {

        val navController = Navigation.findNavController(this, R.id.myNavHostFragment)

        return navController.navigateUp()
    }
}
