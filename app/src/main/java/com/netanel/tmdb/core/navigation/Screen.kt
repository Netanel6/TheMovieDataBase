package com.netanel.tmdb.core.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
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

typealias MovieSectionType = MovieSection.MovieSectionType

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: Int) = "details/$movieId"
    }

    data object AllMovies : Screen("allMovies") {
        const val SECTION_KEY = "sectionType"
        const val QUERY_KEY = "query"
        val routeWithSection = "allMovies/section/{$SECTION_KEY}"
        val routeWithQuery = "allMovies/search/{$QUERY_KEY}"

        fun createRouteWithSection(movieSectionType: MovieSectionType?) =
            "allMovies/section/${movieSectionType?.name}"

        fun createRouteWithQuery(query: String?) =
            "allMovies/search/${query}"
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
                onViewAllClicked = { movieSectionType, query ->
                    movieSectionType?.let {
                        navController.navigate(
                            Screen.AllMovies.createRouteWithSection(
                                movieSectionType
                            )
                        )
                    }
                    query?.let {
                        navController.navigate(Screen.AllMovies.createRouteWithQuery(query))
                    }
                }
            )
        }

        composable(Screen.Details.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")!!.toInt()
            MovieDetailsScreen(movieId)
        }

        composable(
            route = Screen.AllMovies.routeWithSection,
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
                AllMoviesScreen(
                    sectionType = sectionType,
                    query = null,
                    onMovieDetailsClicked = { movie ->
                        navController.navigate(Screen.Details.createRoute(movie.id))
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable(
            route = Screen.AllMovies.routeWithQuery,
            arguments = listOf(
                navArgument(Screen.AllMovies.QUERY_KEY) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getSerializable(
                Screen.AllMovies.QUERY_KEY,
                String::class.java
            ) as String?

            if (query != null) {
                AllMoviesScreen(
                    sectionType = null,
                    query = query,
                    onMovieDetailsClicked = { movie ->
                        navController.navigate(Screen.Details.createRoute(movie.id))
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}