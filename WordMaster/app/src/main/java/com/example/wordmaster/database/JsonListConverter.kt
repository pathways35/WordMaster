package com.example.wordmaster.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonListConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromList(synonyms: List<String>): String {
            val gson = Gson()
            val json:String = gson.toJson(synonyms)
            return json

        }

        @TypeConverter
        @JvmStatic
        fun toList(input: String): List<String> {
            val listType = object : TypeToken<List<String>>() {}.type
            val gson = Gson()
            return gson.fromJson(input, listType)
        }
    }
}