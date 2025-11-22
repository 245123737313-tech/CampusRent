package com.example.campusrent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campusrent.ui.screens.AddItemScreen
import com.example.campusrent.ui.screens.HomeScreen
import com.example.campusrent.ui.screens.ItemDetailScreen
import com.example.campusrent.ui.screens.LoginScreen
import com.example.campusrent.ui.screens.SignupScreen
import com.example.campusrent.viewmodel.AuthViewModel

@Composable
fun CampusRentNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isUserLoggedIn = authViewModel.isUserLoggedIn
    val startDestination = if (isUserLoggedIn) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("addItem") {
            AddItemScreen(navController)
        }
        composable("itemDetail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
            ItemDetailScreen(navController, itemId)
        }
    }
}
