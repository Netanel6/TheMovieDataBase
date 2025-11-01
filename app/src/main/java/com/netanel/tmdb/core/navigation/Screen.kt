package com.netanel.tmdb.core.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.netanel.tmdb.domain.models.MovieSection
import com.netanel.tmdb.features.allMovies.AllMoviesScreen
import com.netanel.tmdb.features.home.HomeScreen
import com.netanel.tmdb.features.movieDetails.MovieDetailsScreen
import java.io.Serializable

typealias MovieSectionType = MovieSection.MovieSectionType

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: Int) = "details/$movieId"
    }

    data object AllMovies : Screen("allMovies") {
        const val SECTION_KEY = "sectionType"
        val routeWithArgs = "allMovies/{$SECTION_KEY}"

        fun createRoute(movieSectionType: MovieSectionType) =
            "allMovies/${movieSectionType.name}"
    }
}

@SuppressLint("NewApi")
@Composable
fun TmdbNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                modifier = Modifier,
                onMovieDetailsClicked = { movie ->
                    navController.navigate(Screen.Details.createRoute(movie.id))
                },
                onViewAllClicked = { movieSectionType ->
                    navController.navigate(Screen.AllMovies.createRoute(movieSectionType))
                }
            )
        }

        composable(Screen.Details.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")!!.toInt()
            MovieDetailsScreen(movieId)
        }

        composable(
            route = Screen.AllMovies.routeWithArgs,
            arguments = listOf(
                navArgument(Screen.AllMovies.SECTION_KEY) {
                    type = NavType.EnumType(MovieSectionType::class.java)
                }
            )
        ) { backStackEntry ->
            val sectionType = backStackEntry.arguments?.getSerializable(
                Screen.AllMovies.SECTION_KEY,
                MovieSectionType::class.java
            ) as MovieSectionType?

            if (sectionType != null) {
                AllMoviesScreen(sectionType = sectionType)
            } else {
                navController.popBackStack()
            }
        }
    }
}