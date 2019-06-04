package com.example.wordmaster.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wordmaster.database.Model.Word
import com.example.wordmaster.database.WordDatabaseDao
import com.example.wordmaster.database.WordMasterDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordsRepository(private val database:WordDatabaseDao, val application: Application) {

    val oneWord: LiveData<Word>
        get() = database.getOneWord()

    //initilize the database with data
    suspend fun loadWordsInDB(){
        withContext(Dispatchers.IO){
            database.insertAll(getWords())
        }
    }

    //get the words from the asset
    private fun getWords(): List<Word>{

        val assets = application.getAssets()
        val gson = Gson()

        val input = assets.open("data.txt")
        val json = input.bufferedReader().use { it.readText() }

        val listType = object : TypeToken<List<Word>>() {}.type
        val result: List<Word> = gson.fromJson(json, listType)

        return result
    }

    suspend fun refreshWordList(): MutableList<Word> {
       lateinit var tenWords: MutableList<Word>
        withContext(Dispatchers.IO){
            tenWords = database.getTenWords()
        }
        return tenWords
    }

    suspend fun getRadioValues(): MutableList<String>{
        lateinit var twoWords: MutableList<String>
        withContext(Dispatchers.IO){
            twoWords = database.getTwoWords()
        }
        return twoWords
    }
}