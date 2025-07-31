//package com.projectbytj.onebancrestaurant.screens
//
//import android.net.Uri
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.google.gson.Gson
//
//@Composable
//fun Screen1(
//    names: String,
//    modifier: Modifier = Modifier,
//    navController: NavController,
//    viewModel: PageLink = viewModel()
//) {
//    val cuisines = viewModel.cuisines.value
//
//    Column(Modifier.fillMaxSize()) {
//        // --- Cuisines Section ---
//        Text(
//            "Food Categories",
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        )
//
//        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
//            items(cuisines) { cuisine ->
//                CuisineCardHorizontal(cuisine, navController)
//            }
//        }
//
//        // --- Dishes Section ---
//        Spacer(Modifier.height(16.dp))
//        Text(
//            "Popular Dishes",
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        )
//
//        // Combine all cuisine dishes into one list
//        val allDishes = cuisines.flatMap { it.items }
//        LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
//            itemsIndexed(allDishes) { _, dish ->
//                DishCard(dish, navController)
//            }
//        }
//    }
//}
//
//@Composable
//fun CuisineCardHorizontal(cuisine: Cuisine, navController: NavController) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .width(120.dp)
//            .clickable {
//                // Navigate directly to cuisine-specific dish list
//                val itemsJson = Uri.encode(Gson().toJson(cuisine.items))
//                val cuisineName = Uri.encode(cuisine.cuisine_name)
//                navController.navigate("dishes/$itemsJson/$cuisineName")
//            },
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            AsyncImage(
//                model = cuisine.cuisine_image_url,
//                contentDescription = cuisine.cuisine_name,
//                modifier = Modifier
//                    .height(100.dp)
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(8.dp)),
//                contentScale = ContentScale.Crop
//            )
//            Spacer(Modifier.height(8.dp))
//            Text(
//                cuisine.cuisine_name,
//                fontWeight = FontWeight.Bold,
//                fontSize = 14.sp,
//                modifier = Modifier.padding(4.dp)
//            )
//        }
//    }
//}
//

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
    var isHindi by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {

        // ---- Segment 1: Cuisine Categories ----
        Text(
            text = if (isHindi) "खाद्य श्रेणियाँ" else "Food Categories",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(cuisines) { cuisine ->
                CuisineCardHorizontal(cuisine, navController)
            }
        }

        Spacer(Modifier.height(16.dp))

        // ---- Segment 2: Top 3 Famous Dishes ----
        Text(
            text = if (isHindi) "शीर्ष 3 व्यंजन" else "Top 3 Dishes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        val topDishes = cuisines.flatMap { it.items }.take(3)

        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(topDishes) { dish ->
                DishCard(dish, navController)
            }
        }


        // ---- Segment 3: Cart Button ----
        Button(
            onClick = { navController.navigate("cart") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(if (isHindi) "कार्ट देखें" else "View Cart", color = Color.White)
        }

        // ---- Segment 4: Language Switch ----
        Button(
            onClick = { isHindi = !isHindi },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(if (isHindi) "Switch to English" else "हिंदी में बदलें", color = Color.White)
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

