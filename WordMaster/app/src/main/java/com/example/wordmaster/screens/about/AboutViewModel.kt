package com.example.wordmaster.screens.about

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wordmaster.database.Model.Word
import com.example.wordmaster.database.WordDatabaseDao
import com.example.wordmaster.repository.WordsRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.io.File
import java.io.InputStream

class AboutViewModel(private val db: WordDatabaseDao,  application: Application): AndroidViewModel(application)  {

    private val _onSkipClicked = MutableLiveData<Boolean>()
    val onSkipClicked: LiveData<Boolean>
        get() = _onSkipClicked

    val repository: WordsRepository = WordsRepository(db,application)

    /*** For Coroutine ****/

    //Create a new job
    private var aboutViewModelJob = Job()

    //create the scope of the coroutine
    private val uiscope = CoroutineScope(Dispatchers.Main + aboutViewModelJob)

    init{
        _onSkipClicked.value = false
        uiscope.launch {
            repository.loadWordsInDB()
        }
    }

    val completed = repository.oneWord


    fun onSkip(){
        _onSkipClicked.value = true
    }

    fun onSkipCompleted(){
        _onSkipClicked.value = false
    }
    override fun onCleared() {
        super.onCleared()

        Log.i("AboutViewModel", "AboutViewModel destroyed!")
        aboutViewModelJob.cancel()
    }
}