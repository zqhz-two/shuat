package com.example.shuat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shuat.presentation.home.HomeScreen
import com.example.shuat.presentation.practice.PracticeScreen
import com.example.shuat.presentation.review.ReviewScreen
import com.example.shuat.presentation.statistics.StatisticsScreen

/**
 * 导航路由
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Practice : Screen("practice/{mode}") {
        fun createRoute(mode: String) = "practice/$mode"
    }
    object Review : Screen("review/{type}") {
        fun createRoute(type: String) = "review/$type"
    }
    object Statistics : Screen("statistics")
}

/**
 * 应用导航主机
 */
@Composable
fun ShuaTiNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToPractice = { mode ->
                    navController.navigate(Screen.Practice.createRoute(mode))
                },
                onNavigateToReview = { type ->
                    navController.navigate(Screen.Review.createRoute(type))
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                }
            )
        }

        composable(
            route = Screen.Practice.route,
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "random"
            PracticeScreen(
                mode = mode,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Review.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "wrong"
            ReviewScreen(
                type = type,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
