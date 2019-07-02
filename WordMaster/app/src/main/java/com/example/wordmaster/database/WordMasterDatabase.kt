package com.example.wordmaster.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wordmaster.database.Model.GameWord
import com.example.wordmaster.database.Model.Word

@Database(entities = [Word::class , GameWord::class], version = 1, exportSchema = false)
@TypeConverters(JsonListConverter::class)
abstract class WordMasterDatabase: RoomDatabase() {

    abstract val wordDatabaseDao: WordDatabaseDao
    abstract val gameWordDao: GameWordDao

    companion object{

        @Volatile
        private var DB: WordMasterDatabase? = null

        fun getInstance(context: Context) : WordMasterDatabase{
            synchronized(this){
                var db = DB

                if(db == null){
                    db = Room.databaseBuilder(context.applicationContext,
                            WordMasterDatabase::class.java,"word_master_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    DB = db
                }

                return db
            }
        }
    }
}