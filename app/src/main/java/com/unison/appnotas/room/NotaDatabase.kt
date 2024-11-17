package com.unison.appnotas.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unison.appnotas.model.Nota


@Database(
    entities = [Nota::class],
    version = 1,
    exportSchema = false)

abstract class NotaDatabase : RoomDatabase() {
    abstract fun notasDao(): NotasDatabaseDao
}