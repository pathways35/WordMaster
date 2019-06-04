package com.example.wordmaster.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.wordmaster.database.Model.Word

@Dao
interface WordDatabaseDao {

    @Insert
    fun insert(aWord: Word)

    //Insert all the words at once
    @Insert
    fun insertAll(words: List<Word>)


    @Query("SELECT * from words_table where id = :key")
    fun getWord(key: Long): Word

    @Query("SELECT * from words_table ORDER BY RANDOM() LIMIT 10")
    fun getTenWords():MutableList<Word>

    @Query("SELECT word from words_table ORDER BY RANDOM() LIMIT 2")
    fun getTwoWords():MutableList<String>

    @Query("SELECT * from words_table ORDER BY RANDOM() LIMIT 1")
    fun getOneWord():LiveData<Word>

    //Delete all rows in the words_table and returns the number of rows deleted
    @Delete
    fun clearWordsTable(words: List<Word>): Int

    //Alternate: Delete all rows in the words_table
    @Query("DELETE FROM words_table")
    fun clear()
}