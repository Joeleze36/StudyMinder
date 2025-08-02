package com.joel_eze.studyminder

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import androidx.compose.runtime.mutableIntStateOf

@Composable
fun PomodoroScreen() {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI) }

    val minuteOptions = listOf(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60)
    var selectedMinutes by remember { mutableIntStateOf(25) }
    var timeLeft by remember { mutableIntStateOf(selectedMinutes * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var hasPlayedSound by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, timeLeft) {
        if (isRunning && timeLeft > 0) {
            kotlinx.coroutines.delay(1000)
            timeLeft -= 1
        } else if (timeLeft == 0 && !hasPlayedSound) {
            mediaPlayer.start()
            hasPlayedSound = true
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Pomodoro Timer", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds),
            fontSize = 48.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Minute Picker
        LazyColumn(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(minuteOptions) { minute ->
                Text(
                    text = "$minute",
                    fontSize = if (minute == selectedMinutes) 32.sp else 20.sp,
                    fontWeight = if (minute == selectedMinutes) FontWeight.Bold else FontWeight.Normal,
                    color = if (minute == selectedMinutes) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            selectedMinutes = minute
                            timeLeft = minute * 60
                            isRunning = false
                            hasPlayedSound = false
                        },
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                isRunning = !isRunning
                hasPlayedSound = false
            }) {
                Text(if (isRunning) "Pause" else "Start")
            }

            OutlinedButton(onClick = {
                isRunning = false
                timeLeft = selectedMinutes * 60
                hasPlayedSound = false
            }) {
                Text("Reset")
            }
        }
    }
}
