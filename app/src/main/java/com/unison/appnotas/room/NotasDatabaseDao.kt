package com.unison.appnotas.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unison.appnotas.model.Nota
import kotlinx.coroutines.flow.Flow

@Dao
interface NotasDatabaseDao {

    @Query("SELECT * FROM notas")
    fun getNotas(): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE id = :id")
    fun getNotaById(id: Int): Flow<Nota>

    @Insert(entity = Nota::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNota(nota: Nota)

    @Update(entity = Nota::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNota(nota: Nota)

    @Delete
    suspend fun deleteNota(nota: Nota)
}