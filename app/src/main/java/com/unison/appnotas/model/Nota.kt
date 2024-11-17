package com.unison.appnotas.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notas")
data class Nota(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "titulo")
    val titulo: String = "",

    @ColumnInfo(name = "contenido")
    val contenido: String = "",

    @ColumnInfo(name = "fecha")
    val fecha: String = "",

    @ColumnInfo(name = "colorFondo")
    val colorFondo: String = "",

    @ColumnInfo(name = "imgFondo")
    val imgFondo: String = ""
)