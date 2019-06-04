package com.example.wordmaster.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int): ViewModel() {


    private val _eventTryAgain = MutableLiveData<Boolean>()
    val eventTryAgain: LiveData<Boolean> get() = _eventTryAgain

    private val _score =  MutableLiveData<Int>()
    val score : LiveData<Int> get() = _score

    init {
        Log.i("ScoreViewModel", "Final score is : $finalScore")
        _score.value = finalScore
        _eventTryAgain.value = false
    }

    fun onTryAgain(){
        _eventTryAgain.value = true
    }

    fun onTryAgainComplete(){
        _eventTryAgain.value = false
    }
}
