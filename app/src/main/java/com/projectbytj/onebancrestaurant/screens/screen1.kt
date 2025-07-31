package com.projectbytj.onebancrestaurant.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson

@Composable
fun Screen1(
    names: String,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PageLink = viewModel()
) {
    val cuisines = viewModel.cuisines.value

    Column(Modifier.fillMaxSize()) {
        // --- Cuisines Section ---
        Text(
            "Food Categories",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(cuisines) { cuisine ->
                CuisineCardHorizontal(cuisine, navController)
            }
        }

        // --- Dishes Section ---
        Spacer(Modifier.height(16.dp))
        Text(
            "Popular Dishes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Combine all cuisine dishes into one list
        val allDishes = cuisines.flatMap { it.items }
        LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
            itemsIndexed(allDishes) { _, dish ->
                DishCard(dish, navController)
            }
        }
    }
}

@Composable
fun CuisineCardHorizontal(cuisine: Cuisine, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
            .clickable {
                // Navigate directly to cuisine-specific dish list
                val itemsJson = Uri.encode(Gson().toJson(cuisine.items))
                val cuisineName = Uri.encode(cuisine.cuisine_name)
                navController.navigate("dishes/$itemsJson/$cuisineName")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = cuisine.cuisine_image_url,
                contentDescription = cuisine.cuisine_name,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(
                cuisine.cuisine_name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

