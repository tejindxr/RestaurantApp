package com.projectbytj.onebancrestaurant.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.projectbytj.onebancrestaurant.screens.Dish
import com.projectbytj.onebancrestaurant.screens.DishesScreen
import com.projectbytj.onebancrestaurant.screens.ItemDetailScreen
import com.projectbytj.onebancrestaurant.screens.PageLink
import com.projectbytj.onebancrestaurant.screens.PaymentScreen
import com.projectbytj.onebancrestaurant.screens.Screen1

@Composable
fun Nav(navController: NavController, viewModel: PageLink) {
    val tj = rememberNavController()
    NavHost(navController = tj, startDestination = "Screen1") {
        // Screen 1 - Cuisine List
        composable("Screen1") {
            Screen1("Screen1", navController = tj, viewModel = viewModel)
        }

        // Screen 2 - Dishes List
        composable("dishes/{itemsJson}/{cuisineName}") { backStackEntry ->
            val itemsJson = backStackEntry.arguments?.getString("itemsJson") ?: "[]"
            val cuisineName = Uri.decode(backStackEntry.arguments?.getString("cuisineName") ?: "")
            val dishes: List<Dish> = Gson().fromJson(itemsJson, Array<Dish>::class.java).toList()
            DishesScreen(cuisineName, dishes, tj) // using same nav controller
        }

        // Screen 3 - Dish Detail
        composable("itemDetail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            ItemDetailScreen(itemId ,tj) // fixed composable version
        }

        composable("cart") {
            PaymentScreen(navController = tj , viewModel = viewModel)
        }
    }
}
