package com.example.wordmaster.screens.about

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordmaster.database.WordDatabaseDao
import com.example.wordmaster.screens.game.GameViewModel

class AboutViewModelFactory (private val dataSource: WordDatabaseDao,
                             private val application: Application
):
    ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AboutViewModel::class.java)){
            return AboutViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}