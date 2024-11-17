package com.unison.appnotas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.unison.appnotas.viewmodels.NotaViewModel
import com.unison.appnotas.views.HomeView
import com.unison.appnotas.views.ListaNotasView
import com.unison.appnotas.views.NotaView
import com.unison.appnotas.views.EditarNotaView

@Composable
fun NavManager(viewModel: NotaViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeView(navController)
        }
        composable<ListaNotas> {
            ListaNotasView(viewModel, navController)
        }
        composable<CrearNota> {
            NotaView(viewModel, navController)
        }
        composable<EditarNota> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<EditarNota>()
            EditarNotaView(args.notaId, viewModel, navController)
        }
    }
}