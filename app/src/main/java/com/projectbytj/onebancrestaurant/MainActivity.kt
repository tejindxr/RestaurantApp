package com.projectbytj.onebancrestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.projectbytj.onebancrestaurant.navigation.Nav
import com.projectbytj.onebancrestaurant.ui.theme.OnebancRestaurantTheme
import com.projectbytj.onebancrestaurant.screens.PageLink


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnebancRestaurantTheme {
                val viewModel: PageLink = viewModel()
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    home(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun home( modifier: Modifier = Modifier , navController: NavController , viewModel: PageLink){


    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "\uD835\uDD46\uD835\uDD5F\uD835\uDD56\uD835\uDD39\uD835\uDD52\uD835\uDD5F\uD835\uDD54.\uD835\uDD57\uD835\uDD60\uD835\uDD60\uD835\uDD55\uD835\uDD6A",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF57C00),  // Yellow AppBar
                    titleContentColor = Color.Black
                )
            )
        },
//        bottomBar = {
//            BottomNavigation(
//                backgroundColor = Color.Transparent,  // Transparent background
//                elevation = 0.dp                      // Remove shadow
//            ) {
//                // Your Bottom Nav Items
//            }
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)  // <-- This respects system bars automatically
        ) {
            Nav(navController,viewModel)
        }
    }

}

