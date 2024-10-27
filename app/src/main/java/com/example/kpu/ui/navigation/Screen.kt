package com.example.kpu.ui.navigation

import com.example.kpu.data.Entry
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object Dashboard : Screen("dashboard")

    @Serializable
    data object Info : Screen("info")

    @Serializable
    data object Form : Screen("form")

    @Serializable
    data object List : Screen("list")

    @Serializable
    data class Detail(
        val entry: Entry
    ): Screen("detail")
}