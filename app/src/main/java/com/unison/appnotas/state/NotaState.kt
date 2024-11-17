package com.unison.appnotas.state

import com.unison.appnotas.model.Nota

data class NotaState(
    val notaList: List<Nota> = emptyList()
)