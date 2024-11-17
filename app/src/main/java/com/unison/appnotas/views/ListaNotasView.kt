package com.unison.appnotas.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unison.appnotas.R
import com.unison.appnotas.dialogs.SimpleDialog
import com.unison.appnotas.model.Nota
import com.unison.appnotas.navigation.CrearNota
import com.unison.appnotas.navigation.EditarNota
import com.unison.appnotas.viewmodels.NotaViewModel

@Composable
fun ListaNotasView(viewModel: NotaViewModel, navController: NavHostController) {
    Scaffold(
        floatingActionButton = {
            BotonAgregarNota(navController = navController)
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BotonAtras(
                navController = navController,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
            )
            // Titulo
            Text(
                text = "Notas",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp)
            )

            // Estado del viewModel
            val state = viewModel.state

            var notaToDelete: Nota by remember { mutableStateOf(Nota()) }
            var openDeleteDialog by remember { mutableStateOf(false) }

            // Carga.
            if (state.notaList.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.no_notas_available))
                }
            } else {
                // Mostrar las notas.
                LazyColumn {

                    // Definición de los registros.
                    items(state.notaList) {
                        NotaItemCard(
                            nota = it,
                            navController = navController,
                            onEditClick = {
                                navController.navigate(
                                    EditarNota(it.id)
                                )
                            },
                            onDeleteClick = {
//                                viewModel.deleteNota(it)
                                openDeleteDialog = true
                                notaToDelete = it
                            }
                        )
//                        HorizontalDivider()
                    }
                }
                if (openDeleteDialog) {
                    OpenDeleteDialog(
                        onDismissRequest = {
                            openDeleteDialog = false
                            notaToDelete = Nota()
                        },
                        onConfirmation = {
                            try {
                                viewModel.deleteNota(notaToDelete)
                            } catch (e: Exception) {
                                println(e)
                            } finally {
                                openDeleteDialog = false
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotaItemCard(
    nota: Nota,
    navController: NavHostController,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val backgroundColor = try {
        Color(android.graphics.Color.parseColor(nota.colorFondo))
    } catch (e: Exception) {
        Color(android.graphics.Color.parseColor("#e7e0ec"))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = { onEditClick() }), // Permite hacer clic para editar
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
//                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = nota.titulo,
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = "Última actualiazción: " + nota.fecha,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
//                IconButton(onClick = {
//                    onEditClick()
//                }) {
//                    Icon(
//                        imageVector = Icons.Filled.Edit,
//                        contentDescription = "Edit",
//                        tint = Color.Black
//                    )
//                }
                IconButton(onClick = {
                    onDeleteClick()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun BotonAgregarNota(navController: NavHostController, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { navController.navigate(CrearNota) },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.inversePrimary
//        containerColor = colorResource(id = R.color.azul_verde),
//        contentColor = colorResource(id = R.color.verde_claro)
    ) {
        // Icono para el botón de agregar
        Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Nota")
    }
}

@Composable
fun BotonAtras(navController: NavHostController, modifier: Modifier = Modifier) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = Modifier
            .padding(top = 22.dp, start = 10.dp)
    ) {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = "Icono Atras",
            tint = MaterialTheme.colorScheme.primary,
//            tint = colorResource(id = R.color.azul_verde),
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun OpenDeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    SimpleDialog(
        onDismissRequest = { onDismissRequest() },
        onConfirmation = { onConfirmation() },
        dialogTitle = stringResource(id = R.string.Delete),
        dialogText = stringResource(id = R.string.Action_Undone)
    )
}