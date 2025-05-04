package com.example.drinksapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.drinksapp.Cocktail
import com.example.drinksapp.components.TimerComponent

@Composable
fun CocktailDetailsScreen(
    cocktail: Cocktail,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current

    val imageWidthFraction = when {
        configuration.screenWidthDp >= 600 -> 0.7f
        else -> 0.5f
    }

    val imageAspectRatio = when {
        configuration.screenWidthDp >= 600 -> 1f
        else -> 0.8f
    }

    AppScaffold(
        currentRoute = currentRoute,
        onNavigate = onNavigate
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
                .verticalScroll(scrollState)
        ) {
            Text(
                text = cocktail.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Category: ${cocktail.category}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                cocktail.imageBlob?.let { blob ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(blob)
                            .crossfade(true)
                            .build(),
                        contentDescription = cocktail.name,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .widthIn(max = (configuration.screenWidthDp * imageWidthFraction).dp)
                            .aspectRatio(imageAspectRatio)
                    )
                } ?: Box(
                    modifier = Modifier
                        .widthIn(max = (configuration.screenWidthDp * imageWidthFraction).dp)
                        .aspectRatio(imageAspectRatio),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Image",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }

            Text(
                text = "Ingredients:",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            cocktail.ingredients.forEach { ingredient ->
                Text(
                    text = "• $ingredient",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Text(
                text = "Recipe:",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = cocktail.recipe,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.3f)
            )

            TimerComponent(
                showTitle = false,
            )
        }
    }
}