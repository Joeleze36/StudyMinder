package com.joel_eze.studyminder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joel_eze.studyminder.viewmodel.TodoViewModel

@Composable
fun TodoScreen(todoViewModel: TodoViewModel = viewModel()) {
    var currentInput by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Todo List", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = currentInput,
            onValueChange = { currentInput = it },
            label = { Text("Enter task") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (currentInput.text.isNotBlank()) {
                    todoViewModel.addTask(currentInput.text)
                    currentInput = TextFieldValue("") // clear input
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(16.dp))

        todoViewModel.todoItems.forEach { item ->
            Text("• $item", style = MaterialTheme.typography.bodyLarge)
        }
    }
}