package com.unison.appnotas.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.unison.appnotas.R
import com.unison.appnotas.navigation.ListaNotas

@Composable
fun HomeView(navController: NavHostController) {
    Home(navController)
}

@Composable
fun Home(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Imagen()
        Botones(navController)
        MiNombre()
    }
}

@Composable
fun Imagen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.unison),
        contentDescription = "Escudo Unison",
        modifier = modifier
            .width(500.dp)
            .padding(top = 70.dp)
    )
}

@Composable
fun Botones(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExtendedFloatingActionButton(
            onClick = { navController.navigate(ListaNotas) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.inversePrimary
//            containerColor = colorResource(id = R.color.verde_claro),
//            contentColor = colorResource(id = R.color.verde_brillante)
        ) {
            Text(text = "Iniciar", fontSize = 30.sp, modifier = Modifier.padding(15.dp))
        }
    }
}

@Composable
fun MiNombre(modifier: Modifier = Modifier) {
    Text(
        text = "Armando González Martínez",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
//        color = colorResource(id = R.color.verde_claro),
        modifier = modifier.padding(bottom = 50.dp)
    )
}