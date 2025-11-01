package com.netanel.tmdb.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.netanel.tmdb.features.home.HomeScreen
import com.netanel.tmdb.features.movieDetails.MovieDetailsScreen

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
        modifier = Modifier
    ) {
        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                modifier = Modifier,
                onMovieDetailsClicked = { movie ->
                    navController.navigate(Screen.Details.createRoute(movie.id))
                },
                onViewAllClicked = {
                    // TODO: Implement type of movie list screen
//                    navController.navigate()
                }
            )
        }

        // Details Screen
        composable(Screen.Details.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")!!.toInt()
            MovieDetailsScreen(movieId)
        }

        // All Movies Screen
        composable(Screen.Details.route) { backStackEntry ->
            // TODO: Move type of movie to this screen and lazy loading the list of all movies
//            val movieId = backStackEntry.arguments?.getString("movieId")!!.toInt()
//            MovieDetailsScreen(movieId)
        }

    }
}
