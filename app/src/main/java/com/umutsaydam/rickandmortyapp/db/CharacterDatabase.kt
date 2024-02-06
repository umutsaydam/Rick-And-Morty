package com.umutsaydam.rickandmortyapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.umutsaydam.rickandmortyapp.models.Result

@Database(
    entities = [Result::class],
    version = 12,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao

    companion object {
        @Volatile
        private var instance: CharacterDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CharacterDatabase::class.java,
            "characters.db"
        ).fallbackToDestructiveMigration().build()
    }
}