package com.unison.appnotas.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unison.appnotas.model.Nota
import com.unison.appnotas.room.NotasDatabaseDao
import com.unison.appnotas.state.NotaState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotaViewModel(
    private val dao: NotasDatabaseDao
) : ViewModel() {

    var state by mutableStateOf(NotaState())
        private set

    init {
        viewModelScope.launch {
            // Obtener la lista de notas en la base de datos
            dao.getNotas().collectLatest {

                // Guardar en el estado la lista de notas
                state = state.copy (
                    notaList = it
                )
            }
        }
    }

    fun getNotasById(id: Int): Nota? {
        return state.notaList.find {
            id == it.id
        }
    }

    fun addNota(nota: Nota) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addNota(nota)
        }
    }

    fun updateNota(nota: Nota) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateNota(nota)
        }
    }

    fun deleteNota(nota: Nota) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteNota(nota)
        }
    }
}