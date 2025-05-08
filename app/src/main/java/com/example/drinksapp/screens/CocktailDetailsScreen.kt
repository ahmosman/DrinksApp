package com.example.drinksapp.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontStyle
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

    val isLandscapeLayout = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE ||
            configuration.screenWidthDp >= 600

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
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = cocktail.name,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Category: ${cocktail.category}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (isLandscapeLayout) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.35f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        CocktailImage(cocktail, true)
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.65f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        IngredientsAndRecipeContent(cocktail)
                    }
                }
            } else {
                CocktailImage(cocktail, false)
                IngredientsAndRecipeContent(cocktail)
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.3f)
            )

            TimerComponent(showTitle = false)
        }
    }
}

@Composable
private fun CocktailImage(cocktail: Cocktail, isLandscapeLayout: Boolean) {
    val imageModifier = if (isLandscapeLayout) {
        Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
    } else {
        Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        cocktail.imageBlob?.let { blob ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(blob)
                    .crossfade(true)
                    .build(),
                contentDescription = cocktail.name,
                contentScale = ContentScale.Fit,
                modifier = imageModifier
            )
        } ?: Box(
            modifier = imageModifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Image",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun IngredientsAndRecipeContent(cocktail: Cocktail) {
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