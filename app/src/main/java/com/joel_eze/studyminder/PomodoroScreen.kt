package com.joel_eze.studyminder

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import androidx.compose.runtime.mutableIntStateOf


@Composable
fun PomodoroScreen() {
    var timeLeft by remember { mutableIntStateOf(25 * 60) } // 25 minutes
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, timeLeft) {
        if (isRunning && timeLeft > 0) {
            kotlinx.coroutines.delay(1000)
            timeLeft -= 1
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
        Text("Pomodoro Timer", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds),
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { isRunning = !isRunning }) {
                Text(if (isRunning) "Pause" else "Start")
            }

            OutlinedButton(onClick = {
                isRunning = false
                timeLeft = 25 * 60
            }) {
                Text("Reset")
            }
        }
    }
}