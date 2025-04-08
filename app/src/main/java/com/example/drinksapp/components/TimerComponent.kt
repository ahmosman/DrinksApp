package com.example.drinksapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon

@Composable
fun TimerComponent(
    modifier: Modifier = Modifier,
    initialTime: Int = 0,
    showTitle: Boolean = true,
    title: String = "Timer"
) {
    // Stany komponentu
    var totalSeconds by rememberSaveable { mutableIntStateOf(initialTime) }
    var remainingSeconds by rememberSaveable { mutableIntStateOf(totalSeconds) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var isEditMode by rememberSaveable { mutableStateOf(true) }

    // Obsługa cyklu życia
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) isRunning = false
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

// Logika odliczania
LaunchedEffect(isRunning) {
    if (isRunning) {
        isEditMode = false
        while (remainingSeconds > 0 && isRunning) {
            delay(1000L)
            remainingSeconds--
        }
        if (remainingSeconds == 0) {
            isRunning = false
            isEditMode = true  // Automatyczny powrót do trybu edycji po zakończeniu odliczania
        }
    }
}

    val hoursOffset = calculateInitialOffset(24)
    val minutesSecondsOffset = calculateInitialOffset(60)

    val hoursListState = rememberLazyListState(initialFirstVisibleItemIndex = hoursOffset)
    val minutesListState =
        rememberLazyListState(initialFirstVisibleItemIndex = minutesSecondsOffset)
    val secondsListState =
        rememberLazyListState(initialFirstVisibleItemIndex = minutesSecondsOffset)

// Aktualizacja czasu z kół wyboru
    LaunchedEffect(isEditMode) {
        if (isEditMode) {
            kotlinx.coroutines.flow.combine(
                snapshotFlow { hoursListState.firstVisibleItemIndex - 1 },
                snapshotFlow { minutesListState.firstVisibleItemIndex - 1 },
                snapshotFlow { secondsListState.firstVisibleItemIndex - 1 }
            ) { hours, minutes, seconds ->
                Triple(hours, minutes, seconds)
            }.collect { (hoursIndex, minutesIndex, secondsIndex) ->
                val hours = calculateTimeValue(hoursIndex, 24)
                val minutes = calculateTimeValue(minutesIndex, 60)
                val seconds = calculateTimeValue(secondsIndex, 60)

                totalSeconds = hours * 3600 + minutes * 60 + seconds
                remainingSeconds = totalSeconds
            }
        }
    }

    // Interfejs użytkownika
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Tytuł
            if (showTitle) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Wyświetlanie czasu lub koła wyboru
            if (!isEditMode) {
                DisplayTime(remainingSeconds)
            } else {
                TimeWheelPicker(
                    hoursListState = hoursListState,
                    minutesListState = minutesListState,
                    secondsListState = secondsListState
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Przyciski
            ControlButtons(
                isRunning = isRunning,
                canStart = totalSeconds > 0,
                onStart = { isRunning = true },
                onPause = { isRunning = false },
                onReset = {
                    isRunning = false
                    remainingSeconds = totalSeconds
                    isEditMode = true
                }
            )
        }
    }
}

@Composable
private fun DisplayTime(seconds: Int) {
    Text(
        text = String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            seconds / 3600,
            (seconds % 3600) / 60,
            seconds % 60
        ),
        color = Color.White,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    )
}

@Composable
private fun TimeWheelPicker(
    hoursListState: LazyListState,
    minutesListState: LazyListState,
    secondsListState: LazyListState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TimePickerWheel(listState = hoursListState, items = (0..23).toList())
        Text(":", color = Color.White, fontSize = 32.sp)
        TimePickerWheel(listState = minutesListState, items = (0..59).toList())
        Text(":", color = Color.White, fontSize = 32.sp)
        TimePickerWheel(listState = secondsListState, items = (0..59).toList())
    }
}

@Composable
private fun ControlButtons(
    isRunning: Boolean,
    canStart: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = onStart,
            enabled = !isRunning && canStart,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                disabledContainerColor = Color.Gray
            ),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.size(60.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Start",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Button(
            onClick = onPause,
            enabled = isRunning,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA000),
                disabledContainerColor = Color.Gray
            ),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.size(60.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Pause",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Button(
            onClick = onReset,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.size(60.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun TimePickerWheel(
    listState: LazyListState,
    items: List<Int>
) {
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val selectedIndex by remember {
        derivedStateOf {
            val rawIndex = listState.firstVisibleItemIndex + 1
            val index = (rawIndex % items.size).let { if (it < 0) it + items.size else it }
            index.coerceIn(items.indices)
        }
    }

    Box(
        modifier = Modifier
            .height(150.dp)
            .width(60.dp)
            .background(color = Color(0x22FFFFFF), shape = RoundedCornerShape(8.dp))
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(Int.MAX_VALUE) { index ->
                val itemIndex = ((index % items.size) + items.size) % items.size
                val formattedValue = "%02d".format(items[itemIndex])
                val alpha = if (itemIndex == selectedIndex) 1f else 0.3f

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(width = 60.dp, height = 50.dp)
                ) {
                    Text(
                        text = formattedValue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Funkcja pomocnicza do obliczania wartości czasu
private fun calculateTimeValue(listIndex: Int, modulo: Int): Int {
    return ((listIndex + 2) % modulo).let { if (it < 0) it + modulo else it }
}

private fun calculateInitialOffset(modulo: Int): Int {
    return (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % modulo) - 2
}