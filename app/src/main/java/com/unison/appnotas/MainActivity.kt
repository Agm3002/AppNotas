package com.unison.appnotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.unison.appnotas.navigation.NavManager
import com.unison.appnotas.room.NotaDatabase
import com.unison.appnotas.ui.theme.AppNotasTheme
import com.unison.appnotas.viewmodels.NotaViewModel

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            context = applicationContext,
            klass = NotaDatabase::class.java,
            name = "notas.db"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Instancia del viewModel
        val viewModel: NotaViewModel = NotaViewModel(db.build().notasDao())

        // Establecer el contenido de la aplicación.
        setContent {
            AppNotasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavManager(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}