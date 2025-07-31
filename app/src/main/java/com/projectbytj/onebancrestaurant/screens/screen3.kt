package com.projectbytj.onebancrestaurant.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text


// Dummy Cart list (Replace with your actual cart list)
var Cart = mutableStateListOf<CartItem>()

data class CartItem(
    val id: Int,  // instead of String
    val name: String,
    val imageUrl: String,
    val price: Int,
    var quantity: MutableState<Int> = mutableStateOf(1)
)

@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: PageLink = viewModel()
)
{
    val total by remember { derivedStateOf { Cart.sumOf { it.price * it.quantity.value } } }
    val paymentResponse = viewModel.paymentResponse.value

    var showDialog by remember { mutableStateOf(false) }

    // If payment successful, trigger dialog
    LaunchedEffect(paymentResponse) {
        if (paymentResponse != null && paymentResponse.outcome_code == 1) {
            showDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Text(
            text = "CART",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(Cart) { item ->
                CartItemCard(item)
            }
            item {
                OrderDetails(total)
                ReturnPolicy()
            }
        }

        PaymentCard(total, viewModel)
    }

    // ===== Payment Success Dialog =====
    if (showDialog && paymentResponse != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Payment Successful",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column {
                    Text(text = "Transaction ID: ${paymentResponse.transaction_id ?: "N/A"}")
                    Text(text = "Your order is on its way!", fontWeight = FontWeight.SemiBold)
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
            // Product Image (From URL)
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

                // Counter
                CounterView(item)
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
fun OrderDetails(total: Int) {
    val deliveryFee = 100
    val platformFee = 19
    val payableAmount = total + deliveryFee + platformFee

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("ORDER DETAILS", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            OrderDetailRow("Bag Total", total)
            OrderDetailRow("Delivery Fee", deliveryFee)
            OrderDetailRow("Platform Fee", platformFee)
            Divider()
            OrderDetailRow("Amount Payable", payableAmount, isBold = true)
        }
    }
}

@Composable
fun OrderDetailRow(label: String, amount: Int, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = "₹$amount",
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
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
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Return & Refund Policy", fontWeight = FontWeight.Bold)
            Text(
                text = "In case of any return, we ensure quick refunds. Full amount will be reflected excluding convenience fee.",
                fontSize = 12.sp
            )
        }
    }
}
@Composable
fun PaymentCard(total: Int, viewModel: PageLink) {
    val deliveryFee = 100
    val platformFee = 19
    val payableAmount = total + deliveryFee + platformFee   // For display only

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Display payable amount (UI only)
            Text(
                text = "₹$payableAmount",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Payment button
            Button(
                onClick = {
                    if (Cart.isNotEmpty()) {
                        val item = Cart[0]

                        // API should only get the item total, not delivery/platform fee
                        val amount = item.price * item.quantity.value

                        // Ensure numeric string for itemId


                        viewModel.makePayment(
                            itemId = item.id,
                            quantity = item.quantity.value,
                            amount = amount
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Proceed to Payment >", color = Color.White)
            }
        }
    }
}
