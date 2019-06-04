package com.example.wordmaster.screens.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordmaster.database.WordDatabaseDao
import com.example.wordmaster.screens.score.ScoreViewModel

class GameViewModelFactory(private val dataSource: WordDatabaseDao,
                           private val application: Application):
                            ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java)){
            return GameViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}