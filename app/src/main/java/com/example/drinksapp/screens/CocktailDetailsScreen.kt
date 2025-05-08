package com.example.drinksapp.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.core.net.toUri
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
    val context = LocalContext.current

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
        onNavigate = onNavigate,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val smsText = buildSmsText(cocktail)
                    sendSms(context, smsText)
                },
                containerColor = Color(0xFF9C27B0)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send SMS with Cocktail",
                    tint = Color.White
                )
            }
        }
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


private fun buildSmsText(cocktail: Cocktail): String {
    val sb = StringBuilder()
    sb.append("Cocktail: '${cocktail.name}':\n\n")

    sb.append("Ingredients:\n")
    cocktail.ingredients.forEachIndexed { index, ingredient ->
        sb.append("${index + 1}. $ingredient\n")
    }
    sb.append("\nRecipe:\n")
    sb.append(cocktail.recipe)

    return sb.toString()
}

private fun sendSms(context: Context, message: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "smsto:".toUri()
        putExtra("sms_body", message)
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No SMS app found",
            Toast.LENGTH_SHORT
        ).show()
    }
}