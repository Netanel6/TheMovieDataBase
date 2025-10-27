package com.netanel.tmdb.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.netanel.tmdb.home.HomeScreen
import com.netanel.tmdb.home.MovieDetailsScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: Int) = "details/$movieId"
    }
}

@Composable
fun TmdbNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier =  Modifier.padding(innerPadding)
    ) {
        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                modifier = Modifier,
                onMovieDetailsClick = { movie ->
                    navController.navigate(Screen.Details.createRoute(movie.id))
                }
            )
        }

        // Details Screen
        composable(Screen.Details.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            MovieDetailsScreen(movieId)
        }
    }
}
