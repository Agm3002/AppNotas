package com.unison.appnotas.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unison.appnotas.R
import com.unison.appnotas.dialogs.SimpleDialog
import com.unison.appnotas.model.Nota
import com.unison.appnotas.viewmodels.NotaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditarNotaView(notaId: Int, viewModel: NotaViewModel, navController: NavHostController , modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(bottom = 40.dp)
    ) {
        BotonAtras(
            navController = navController,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp)
        )
        EspacioEscribirConId(nota = viewModel.getNotasById(notaId), viewModel, navController, modifier)
    }
}

@Composable
fun EspacioEscribirConId(nota: Nota?, viewModel: NotaViewModel, navController: NavHostController, modifier: Modifier) {

    // Almacenar el valor de mis inputs aqui para validarlos en los botones
    var id by remember { mutableStateOf(nota?.id ?: "") }
    var titulo by remember { mutableStateOf(nota?.titulo ?: "") }
    var contenido by remember { mutableStateOf(nota?.contenido ?: "") }
    fun obtenerFechaActual(): String {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatoFecha.format(Date())
    }
    var fecha = obtenerFechaActual()
    var colorFondo by remember { mutableStateOf(nota?.colorFondo ?: "") }
    var imgFondo by remember { mutableStateOf(nota?.imgFondo ?: "") }

    var colorDefault = "#e7e0ec"

    fun obtenerColorFondo(colorFondo: String): Color {
        return try {
            if (colorFondo.isBlank()) {
                Color(android.graphics.Color.parseColor(colorDefault)) // Color por defecto
            } else {
                Color(android.graphics.Color.parseColor(colorFondo))
            }
        } catch (e: IllegalArgumentException) {
            Color(android.graphics.Color.parseColor(colorDefault)) // Si ocurre un error al parsear, usar color por defecto
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .padding(16.dp)
            .background(obtenerColorFondo(colorFondo))
    ) {
        // Inputs en la parte superior
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Inputs(
                titulo = titulo,
                onTituloChange = { titulo = it },
                contenido = contenido,
                onContenidoChange = { contenido = it}
            )
        }

        // Boton en la parte inferior
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            BotonesNotaEditar(
                nota = nota,
                navController = navController,
                viewModel,
                titulo = titulo,
                contenido = contenido,
                fecha = fecha,
                colorFondo = colorFondo,
                onColorFondoChange = { colorFondo = it },
                imgFondo = imgFondo,
                onImgFondoChange = { imgFondo = it }
            )
        }
    }
}

@Composable
fun BotonesNotaEditar(
    nota: Nota?,
    navController: NavHostController,
    viewModel: NotaViewModel,
    titulo: String,
    contenido: String,
    fecha: String,
    colorFondo: String,
    onColorFondoChange: (String) -> Unit,
    imgFondo: String,
    onImgFondoChange: (String) -> Unit,
    ) {
        var errorMsg by remember { mutableStateOf("") }
        var showErrorDialog by remember { mutableStateOf(false) }

        var notaToUpdate: Nota by remember { mutableStateOf(Nota()) }
        var openUpdateDialog by remember { mutableStateOf(false) }

        // Estado para controlar el color de fondo
        var colorDefault = Color(android.graphics.Color.parseColor("#e7e0ec"))
        fun obtenerColorFondo(colorFondo: String): Color {
            return try {
                if (colorFondo.isBlank()) {
                    colorDefault // Color por defecto
                } else {
                    Color(android.graphics.Color.parseColor(colorFondo))
                }
            } catch (e: IllegalArgumentException) {
                colorDefault // Si ocurre un error al parsear, usar color por defecto
            }
        }

        var backgroundColor by remember { mutableStateOf(obtenerColorFondo(colorFondo)) }

        // Estado para mostrar u ocultar el menú de colores
        var showColorMenu by remember { mutableStateOf(false) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
//            .padding(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Botón Circular para Selección de Color
                Box(
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    ColorCircle(
                        color = backgroundColor,
                        onClick = { showColorMenu = true }
                    )
                }
                Button(onClick = {
                    println("Titulo: $titulo, Contenido: $contenido, Fecha: $fecha")

                    try {
                        if (titulo.isBlank() || contenido.isBlank()) {
                            errorMsg = "Debes escribir un titulo y dar contenido a la nota"
                            showErrorDialog = true
                        }
                        else {
                            val notaModificada = Nota(
                                id = nota?.id!!,
                                titulo = titulo,
                                contenido = contenido,
                                fecha = fecha,
                                colorFondo = colorFondo,
                                imgFondo = imgFondo
                            )
                            notaToUpdate = notaModificada
                            openUpdateDialog = true
                        }
                    } catch (e: Exception) {
                        errorMsg = "Algo salio mal: " + e.message
                    }
                },
                    enabled = titulo.isNotBlank() && contenido.isNotBlank(),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.inversePrimary,
                        disabledContentColor = Color.LightGray,
                        disabledContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                ) {
                    Text(text = "Guardar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            }

            // Menú de colores
            DropdownMenu(
                expanded = showColorMenu,
                onDismissRequest = { showColorMenu = false }
            ) {
                val colores = listOf(
                    Color(0xFFF28B82) to "#F28B82", // Rojo pastel
                    Color(0xFF81C995) to "#81C995", // Verde pastel
                    Color(0xFFAECBFA) to "#AECBFA", // Azul pastel
                    Color(0xFFFFF475) to "#FFF475"  // Amarillo pastel
                )
                colores.forEach { (colorObj, colorHex) ->
                    DropdownMenuItem(
                        text = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(colorObj)
                                    .border(1.dp, Color.DarkGray, CircleShape) // Para bordes en los círculos del menú
                            )
                        },
                        onClick = {
                            backgroundColor = colorObj
                            onColorFondoChange(colorHex)
                            showColorMenu = false
                        }
                    )
                }
            }
            Alerta(
                dialogTitle = "Error",
                dialogText = errorMsg,
                onDismissRequest = {
                    showErrorDialog = false
                },
                onConfirmation = {
                    showErrorDialog = false
                },
                show = showErrorDialog
            )
        }
        if (openUpdateDialog) {
            OpenUpdateDialog(
                onDismissRequest = {
                    notaToUpdate = Nota()
                    openUpdateDialog = false
                },
                onConfirmation = {
                    try {
                        viewModel.addNota(notaToUpdate)
                    } catch (e: Exception) {
                        println("Error al agregar nota: " + e)
                    } finally {
                        openUpdateDialog = false
                        navController.popBackStack()
                    }
                }
            )
        }
    }


@Composable
fun OpenUpdateDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    SimpleDialog(
        onDismissRequest = { onDismissRequest() },
        onConfirmation = { onConfirmation() },
        dialogTitle = stringResource(id = R.string.Update),
        dialogText = stringResource(id = R.string.Action_update)
    )
}