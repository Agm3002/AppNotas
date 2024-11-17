package com.unison.appnotas.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object ListaNotas

@Serializable
object CrearNota

@Serializable
data class EditarNota(val notaId: Int)