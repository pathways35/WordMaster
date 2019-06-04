package com.example.wordmaster.database.Model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "words_table")
class Word {

    //autogenerate the id, this is useful when we have to select 10 random words from a list of 500 words
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Long = 0L

    var word: String? = null

    //synonyms is a list of words as there could be more than one synonym for a word
    var synonyms: List<String>? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)

        if (other == null)
            return false

        if (javaClass != other.javaClass)
            return false

        val mOther = other as Word

        return id == mOther.id
                && word == mOther.word
                && synonyms == mOther.synonyms
    }

    override fun hashCode(): Int {
        return Objects.hash(id, word, synonyms)
    }
}