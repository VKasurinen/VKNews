package com.vkasurinen.vknews

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vkasurinen.vknews.presentation.homescreen.HomeScreen
import com.vkasurinen.vknews.presentation.homescreen.HomeScreenRoot
import com.vkasurinen.vknews.util.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val bottomNavController = rememberNavController()
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    IconButton(onClick = { /* Handle menu click */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Profile") },
                            onClick = { menuExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = { menuExpanded = false }
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(bottomNavController = bottomNavController)
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreenRoot(navController = navController)
                }

                composable(Screen.Search.route) {

                }

                composable(Screen.Saved.route) {

                }

            }
        }
    }
}

@Composable
fun BottomNavigationBar(bottomNavController: NavHostController) {
    val items = listOf(
        BottomItem("Discover", icon = Icons.Rounded.Home),
        BottomItem("Search", icon = Icons.Rounded.Search),
        BottomItem("Saved", icon = Icons.Rounded.Star)
    )

    val selected = rememberSaveable { mutableStateOf(0) }

    NavigationBar(
        modifier = Modifier.height(45.dp),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Row(modifier = Modifier.background(Color.Transparent)) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected.value == index,
                    onClick = {
                        selected.value = index
                        when (selected.value) {
                            0 -> bottomNavController.navigate(Screen.Home.route)
                            1 -> bottomNavController.navigate(Screen.Search.route)
                            2 -> bottomNavController.navigate(Screen.Saved.route)
                        }
                    },
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = bottomItem.icon,
                                contentDescription = bottomItem.title,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = bottomItem.title,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 10.sp
                            )
                        }
                    }
                )
            }
        }
    }
}

data class BottomItem(val title: String, val icon: ImageVector)
