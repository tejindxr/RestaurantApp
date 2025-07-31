package com.projectbytj.onebancrestaurant.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun DishesScreen(
    cuisineName: String,
    dishes: List<Dish>,
    navController: NavController
) {
    Column(Modifier.fillMaxSize()) {
        Text(
            text = "$cuisineName Dishes",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
        if (dishes.isEmpty()) {
            Text("No dishes available for $cuisineName", Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(dishes) { dish ->
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
