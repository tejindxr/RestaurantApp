package com.projectbytj.onebancrestaurant.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.draw.rotate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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

    var expanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Filter by Price") }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            // --- Top Row with Back Button & Title ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("<", color = Color.White)
                }

                Text(
                    text = cuisineName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(40.dp)) // For symmetry
            }

            // --- Dropdown Filter ---
            Box(Modifier.padding(horizontal = 12.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(selectedFilter, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(rotation)
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(12.dp))
                ) {
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

            Spacer(Modifier.height(12.dp))

            if (displayDishes.isEmpty()) {
                Text(
                    "No dishes available for $cuisineName",
                    Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                    items(displayDishes) { dish ->
                        DishCard(dish, navController ,cuisineName )
                    }
                }
            }
        }

        // --- Fixed Checkout Button ---
        Button(
            onClick = { navController.navigate("cart") },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .height(55.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                "Proceed to Checkout",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DishCard(dish: Dish, navController: NavController , cuisineName: String) {
    // Initialize quantity from Cart if already added
    var quantity by remember { mutableStateOf(Cart.find { it.id == dish.id }?.quantity?.value ?: 0) }

    // Keep Cart in sync when quantity changes
    LaunchedEffect(quantity) {
        val existingItem = Cart.find { it.id == dish.id }
        if (quantity > 0) {
            if (existingItem != null) {
                existingItem.quantity.value = quantity
            } else {

                Cart.add(
                    CartItem(
                        id = dish.id.toString(),
                        name = dish.name,
                        cuisineName = cuisineName,
                        imageUrl = dish.image_url,
                        price = dish.price.toInt(),
                        quantity = mutableStateOf(quantity)
                    )
                )
            }
        } else {
            Cart.removeAll { it.id == dish.id }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { navController.navigate("itemDetail/${dish.id}") },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = dish.image_url,
                contentDescription = dish.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(dish.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("₹${dish.price}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF388E3C))
                Text("⭐ ${dish.rating}", fontSize = 14.sp, color = Color(0xFFFFA000))
            }

            // --- Counter Buttons (Compact Modern Look) ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Minus Button
                Card(
                    modifier = Modifier.size(28.dp),
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { if (quantity > 0) quantity-- }
                    ) {
                        Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                // Quantity Display
                Card(
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Text(
                        quantity.toString(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Plus Button
                Card(
                    modifier = Modifier.size(28.dp),
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF388E3C)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { quantity++ }
                    ) {
                        Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
