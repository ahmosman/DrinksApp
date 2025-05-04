package com.example.drinksapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.drinksapp.components.LogoComponent
import kotlin.math.abs

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    isWelcomeScreen: Boolean = false,
    currentRoute: String = "",
    onNavigate: (String) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0B0B3B), Color(0xFF170B3B), Color(0xFF230B3B), Color(0xFF330B3B)
        )
    )

    val cosmosGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF05051E),
            Color(0xFF0B0B28),
            Color(0xFF12093B),
            Color(0xFF1D0A45)
        )
    )

    val navigationItems = listOf(
        NavigationItem("Home", Icons.Default.Home, "welcome"),
        NavigationItem("Categories", Icons.Default.Category, "categories"),
        NavigationItem("List", Icons.Default.GridView, "list"),
        NavigationItem("Random", Icons.Default.Shuffle, "random")
    )

    var dragStartX by remember { mutableStateOf(0f) }
    var dragEndX by remember { mutableStateOf(0f) }

    val handleSwipe = { endX: Float ->
        val diff = endX - dragStartX
        if (abs(diff) > 2) {
            val currentIndex = navigationItems.indexOfFirst { it.route == currentRoute }
            if (currentIndex != -1) {
                val newIndex = if (diff > 0) {
                    (currentIndex - 1).coerceAtLeast(0)
                } else {
                    (currentIndex + 1).coerceAtMost(navigationItems.size - 1)
                }
                onNavigate(navigationItems[newIndex].route)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = Color.White,
        topBar = {
            if (!isWelcomeScreen) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cosmosGradient)
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 1.dp)
                ) {
                    LogoComponent(horizontal = true)
                }
            }
        },
        bottomBar = {
            if (!isWelcomeScreen) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cosmosGradient),
                    containerColor = Color.Transparent
                ) {
                    navigationItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = { onNavigate(item.route) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                unselectedTextColor = Color.White.copy(alpha = 0.5f),
                                indicatorColor = Color(0xFF9C27B0)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { dragStartX = it.x },
                        onDragEnd = { handleSwipe(dragEndX) },
                        onHorizontalDrag = { _, dragAmount -> dragEndX = dragStartX + dragAmount }
                    )
                }
        ) {
            content(paddingValues)
        }
    }
}