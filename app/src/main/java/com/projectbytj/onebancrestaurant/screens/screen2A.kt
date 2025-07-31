package com.projectbytj.onebancrestaurant.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage


@Composable
fun DishesScreen(
    cuisineName: String,
    dishes: List<Dish>,
    navController: NavController,
    viewModel: PageLink = viewModel()
) {
    val filteredDishes = viewModel.filteredDishes.value
    val displayDishes = if (filteredDishes.isNotEmpty()) filteredDishes else dishes

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Filter by Price") }

    Column {
        Text(
            text = "$cuisineName Dishes",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )

        // Dropdown Menu for price filters
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)) {

            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selectedFilter)
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("≤ 50") },
                    onClick = {
                        selectedFilter = "Filter by Price: ≤ 50"
                        expanded = false
                        viewModel.applyPriceFilter(50, dishes)
                    }
                )
                DropdownMenuItem(
                    text = { Text("≤ 100") },
                    onClick = {
                        selectedFilter = "Filter by Price: ≤ 100"
                        expanded = false
                        viewModel.applyPriceFilter(100, dishes)
                    }
                )
                DropdownMenuItem(
                    text = { Text("≤ 200") },
                    onClick = {
                        selectedFilter = "Filter by Price: ≤ 200"
                        expanded = false
                        viewModel.applyPriceFilter(200, dishes)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Clear Filter") },
                    onClick = {
                        selectedFilter = "Filter by Price"
                        expanded = false
                        viewModel.clearFilter()
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (displayDishes.isEmpty()) {
            Text("No dishes available for $cuisineName", Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(displayDishes) { dish ->
                    DishCard(dish, navController)
                }
            }
        }
    }
}




@Composable
fun DishCard(dish: Dish, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("itemDetail/${dish.id}") },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(Modifier.padding(8.dp)) {
            AsyncImage(
                model = dish.image_url,
                contentDescription = dish.name,
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(start = 8.dp)) {
                Text(dish.name, fontWeight = FontWeight.Bold)
                Text("₹${dish.price}")
                Text("Rating: ${dish.rating}")
            }
        }
    }
}
