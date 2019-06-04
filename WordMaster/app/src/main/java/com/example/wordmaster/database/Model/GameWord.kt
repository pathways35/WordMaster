package com.example.wordmaster.database.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_words_table")
data class GameWord(
    @PrimaryKey @ColumnInfo(name = "game_word")
    var gameWord: String = "",
    var synonym: String = ""
)