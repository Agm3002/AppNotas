package com.unison.appnotas.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unison.appnotas.R
import com.unison.appnotas.dialogs.SimpleDialog
import com.unison.appnotas.model.Nota
import com.unison.appnotas.ui.theme.BluePastel
import com.unison.appnotas.ui.theme.GreenPastel
import com.unison.appnotas.ui.theme.RedPastel
import com.unison.appnotas.ui.theme.YellowPastel
import com.unison.appnotas.viewmodels.NotaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotaView(viewModel: NotaViewModel, navController: NavHostController) {
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
        EspacioEscribir(navController, viewModel)
    }
}

@Composable
fun EspacioEscribir(navController: NavHostController, viewModel: NotaViewModel) {

    // Almacenar el valor de mis inputs aqui para validarlos en los botones
    var id by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    fun obtenerFechaActual(): String {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatoFecha.format(Date())
    }
    var fecha = obtenerFechaActual()
    var colorFondo by remember { mutableStateOf("") }
    var imgFondo = ""

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
            BotonesNota(
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
fun Inputs(
    titulo: String,
    onTituloChange: (String) -> Unit,
    contenido: String,
    onContenidoChange: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Placeholder text
            if (titulo.isEmpty()) {
                Text(
                    text = "Titulo aquí...",
                    fontSize = 40.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
            BasicTextField(
                value = titulo,
                onValueChange = onTituloChange,
                textStyle = TextStyle(fontSize = 40.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            // Placeholder text
            if (contenido.isEmpty()) {
                Text(
                    text = "Escribe tu nota aquí...",
                    fontSize = 20.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
            BasicTextField(
                value = contenido,
                onValueChange = onContenidoChange,
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun BotonesNota(
    navController: NavHostController,
    viewModel: NotaViewModel,
    titulo: String,
    contenido: String,
    fecha: String,
    colorFondo: String,
    onColorFondoChange: (String) -> Unit,
    imgFondo: String,
    onImgFondoChange: (String) -> Unit
) {
    var errorMsg by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    var notaToAdd: Nota by remember { mutableStateOf(Nota()) }
    var openAddDialog by remember { mutableStateOf(false) }

    // Estado para controlar el color de fondo
    var colorDefault = Color(android.graphics.Color.parseColor("#e7e0ec"))
    var backgroundColor by remember { mutableStateOf(colorDefault) }

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
                        val notaNueva = Nota(
                            titulo = titulo,
                            contenido = contenido,
                            fecha = fecha,
                            colorFondo = colorFondo,
                            imgFondo = imgFondo
                        )
                        notaToAdd = notaNueva
                        openAddDialog = true
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
    if (openAddDialog) {
        OpenAddDialog(
            onDismissRequest = {
                notaToAdd = Nota()
                openAddDialog = false
            },
            onConfirmation = {
                try {
                    viewModel.addNota(notaToAdd)
                } catch (e: Exception) {
                    println("Error al agregar nota: " + e)
                } finally {
                    openAddDialog = false
                    navController.popBackStack()
                }
            }
        )
    }
}

@Composable
fun ColorCircle(
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(start = 10.dp)
            .size(35.dp) // Tamaño del círculo
            .clip(CircleShape) // Forma de círculo
            .background(color) // Color de fondo
            .border(2.dp, Color.DarkGray, CircleShape) // Borde gris oscuro
            .clickable { onClick() } // Hacerlo seleccionable
    )
}

@Composable
fun Alerta(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    show: Boolean
) {
    if (show) {
        AlertDialog(
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun OpenAddDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    SimpleDialog(
        onDismissRequest = { onDismissRequest() },
        onConfirmation = { onConfirmation() },
        dialogTitle = stringResource(id = R.string.Add),
        dialogText = stringResource(id = R.string.Action_add)
    )
}