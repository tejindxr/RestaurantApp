package com.projectbytj.onebancrestaurant.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

var Cart = mutableStateListOf<CartItem>()

data class CartItem(
    val id: String,
    val name: String,
    val cuisineName: String,
    val imageUrl: String,
    val price: Int,
    var quantity: MutableState<Int> = mutableStateOf(1)
)

@Composable
fun CartItemCard(item: CartItem) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .height(120.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "₹${item.price}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                CounterView(item)
            }

            IconButton(
                onClick = { Cart.remove(item) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun CounterView(item: CartItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Button(
            onClick = { if (item.quantity.value > 1) item.quantity.value-- },
            modifier = Modifier.size(30.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("-", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Text(
            text = "${item.quantity.value}",
            modifier = Modifier.padding(horizontal = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = { item.quantity.value++ },
            modifier = Modifier.size(30.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("+", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: PageLink = viewModel()
) {
    val total by remember { derivedStateOf { Cart.sumOf { it.price * it.quantity.value } } }
    val cuisinesSelected = Cart.map { it.cuisineName }.distinct()
    var showDialog by remember { mutableStateOf(false) }

    val cgst = total * 0.025
    val sgst = total * 0.025
    val grandTotal = total + cgst + sgst

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF6EC))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
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
                text = "Your Cart",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(40.dp))
        }

        Card(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(Modifier.padding(10.dp)) {
                Text("Cuisines Selected", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(cuisinesSelected.joinToString(", "), fontSize = 14.sp, color = Color.Gray)
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            if (Cart.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Oops, your cart is empty! 🛒",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add some delicious dishes and make your tummy happy! 😋",
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(Cart) { item -> CartItemCard(item) }
                item {
                    OrderSummary(total, cgst, sgst, grandTotal)
                    ReturnPolicy()
                }
            }
        }

        Button(
            onClick = {
                if (Cart.isNotEmpty()) {
                    showDialog = true
                    Cart.clear()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(55.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Place Order", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Payment Successful", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                text = {
                    Column {
                        Text("Transaction ID: ${System.currentTimeMillis()}")
                        Text("Your order is on its way!", fontWeight = FontWeight.SemiBold)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
fun OrderSummary(netTotal: Int, cgst: Double, sgst: Double, grandTotal: Double) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("ORDER SUMMARY", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            OrderDetailRow("Net Total", netTotal)
            OrderDetailRow("CGST (2.5%)", cgst.toInt())
            OrderDetailRow("SGST (2.5%)", sgst.toInt())
            Divider()
            OrderDetailRow("Grand Total", grandTotal.toInt(), isBold = true)
        }
    }
}

@Composable
fun OrderDetailRow(label: String, amount: Int, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text("₹$amount", fontSize = 14.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun ReturnPolicy() {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Return & Refund Policy", fontWeight = FontWeight.Bold)
            Text(
                text = "In case of any return, we ensure quick refunds. Full amount will be reflected excluding convenience fee.",
                fontSize = 12.sp
            )
        }
    }
}
