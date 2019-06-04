package com.example.wordmaster.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wordmaster.database.Model.GameWord

@Dao
interface GameWordDao {

    @Insert
    fun insert(gameWord: GameWord)

    @Query("SELECT * FROM game_words_table WHERE game_word = :aWord")
    fun getGameWord(aWord: String): GameWord

    @Query("SELECT * FROM game_words_table")
    fun getAllGameWords() : List<GameWord>

    @Query("DELETE FROM game_words_table")
    fun clear()
}