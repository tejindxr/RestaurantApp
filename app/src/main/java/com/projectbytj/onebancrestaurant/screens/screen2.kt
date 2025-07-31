
package com.projectbytj.onebancrestaurant.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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






@Composable
fun ItemDetailScreen(itemId: String, navController: NavController, viewModel: PageLink = viewModel()) {
    LaunchedEffect(itemId) {
        itemId.toIntOrNull()?.let { id ->
            viewModel.fetchItemById(id)
        }
    }

    val dish = viewModel.selectedItem.value

    if (dish == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    // Dish Image
                    AsyncImage(
                        model = dish.item_image_url,
                        contentDescription = dish.item_name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(top = 8.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Dish Name
                    Text(
                        text = dish.item_name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Cuisine Name
                    Text(
                        text = dish.cuisine_name,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )

                    // Price
                    Text(
                        text = "₹${dish.item_price}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )

                    // Rating
                    Text(
                        text = "Rating: ${dish.item_rating}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                    )

                    // Description
                    Text(
                        text = "Description: Delicious ${dish.item_name}. (Demo text as API doesn’t give description)",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Back Button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }

            // Add to Cart Button
            var inCart by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    if (inCart) {
                        navController.navigate("cart")   // Go to cart screen
                    } else {
                        inCart = true
                        // Add the item to Cart list here
                        Cart.add(
                            CartItem(
                                id = dish.item_id,          // keep as Int
                                name = dish.item_name,
                                imageUrl = dish.item_image_url,
                                price = dish.item_price,
                                quantity = mutableStateOf(1)
                            )
                        )






                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.85f)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (inCart) Color.Black else Color.White,
                    contentColor = if (inCart) Color.White else Color.Black
                ),
                border = if (inCart) null else ButtonDefaults.outlinedButtonBorder
            ) {
                Text(if (inCart) "View Cart" else "Add to Cart")
            }

        }
    }
}
