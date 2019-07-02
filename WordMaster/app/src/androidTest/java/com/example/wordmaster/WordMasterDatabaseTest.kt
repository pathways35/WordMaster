package com.example.wordmaster

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.wordmaster.database.GameWordDao
import com.example.wordmaster.database.Model.Word
import com.example.wordmaster.database.WordDatabaseDao
import com.example.wordmaster.database.WordMasterDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class WordMasterDatabaseTest {

    private lateinit var wordDao: WordDatabaseDao
    private lateinit var gameWordDao: GameWordDao

    private lateinit var db: WordMasterDatabase

    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, WordMasterDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        wordDao = db.wordDatabaseDao
        gameWordDao = db.gameWordDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

//    @Test
//    @Throws(Exception::class)
//    fun insertAndGetWord(){
//        val word = Word(
//            "Horrid",
//            {"bad"}
//        )
//        wordDao.insert(word)
//        val theWord = wordDao.getWord(1)
//        //  assertEquals(theWord?.getSynonyms(), "")
//    }

}