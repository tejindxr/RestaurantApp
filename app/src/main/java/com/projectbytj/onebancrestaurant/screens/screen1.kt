
package com.projectbytj.onebancrestaurant.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    // Language toggle state
    var isEnglish by remember { mutableStateOf(true) }
    val languageLabel = if (isEnglish) "EN" else "HI"

    Column(Modifier.fillMaxSize()) {

        // ---- Header (Title + Language Toggle) ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isEnglish) "Food Categories" else "खाद्य श्रेणियाँ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { isEnglish = !isEnglish },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0E0E0),
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("$languageLabel / ${if (isEnglish) "HI" else "EN"}", fontSize = 14.sp)
            }
        }

        // ---- Cuisine Scroll ----
        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(cuisines) { cuisine ->
                CuisineCardHorizontal(cuisine, navController)
            }
        }

        Spacer(Modifier.height(16.dp))

        // ---- Popular Dishes ----
        Text(
            text = if (isEnglish) "Popular Dishes" else "लोकप्रिय व्यंजन",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        val topDishes = cuisines.flatMap { it.items }.take(3)
        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.weight(1f) // <- takes remaining space only
        ) {
            items(topDishes) { dish ->
                val cuisineName = cuisines.firstOrNull { cuisine ->
                    cuisine.items.any { it.id == dish.id }
                }?.cuisine_name ?: "Unknown"

                DishCard(dish, navController  , cuisineName)
            }
        }

        // ---- Go to Cart Button (Always Visible) ----
        Button(
            onClick = { navController.navigate("cart") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                text = if (isEnglish) "Go to Cart" else "कार्ट पर जाएं",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---- Cuisine Card ----
@Composable
fun CuisineCardHorizontal(cuisine: Cuisine, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(220.dp)
            .height(140.dp)
            .clickable {
                val itemsJson = Uri.encode(Gson().toJson(cuisine.items))
                val cuisineName = Uri.encode(cuisine.cuisine_name)
                navController.navigate("dishes/$itemsJson/$cuisineName")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box {
            AsyncImage(
                model = cuisine.cuisine_image_url,
                contentDescription = cuisine.cuisine_name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0x55000000)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    cuisine.cuisine_name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

