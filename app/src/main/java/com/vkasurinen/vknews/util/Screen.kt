package com.vkasurinen.vknews.util

sealed class Screen(val route: String) {

    object Main : Screen("main")
    object Home : Screen("home")
    object Search : Screen("search")
    object Saved: Screen("saved")
    object Details: Screen("details")



}