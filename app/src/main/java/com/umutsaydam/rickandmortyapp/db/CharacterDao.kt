package com.umutsaydam.rickandmortyapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umutsaydam.rickandmortyapp.models.Result

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(character: Result): Long

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): LiveData<List<Result>>

    @Query("SELECT id FROM characters WHERE id = :id")
    fun getSingleCharacter(id: Int): LiveData<Int>

    @Delete
    suspend fun deleteCharacter(character: Result)
}