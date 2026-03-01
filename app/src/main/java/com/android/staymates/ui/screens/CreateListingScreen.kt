package com.android.staymates.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.staymates.data.Listing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    onBack: () -> Unit,
    onSave: (Listing) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var monthlyRentText by remember { mutableStateOf("") }
    var depositText by remember { mutableStateOf("") }
    var roomsText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val monthlyRent = monthlyRentText.toIntOrNull()
    val deposit = depositText.toIntOrNull() ?: 0
    val rooms = roomsText.toIntOrNull()

    val canSave = title.isNotBlank() && location.isNotBlank() && monthlyRent != null && rooms != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create listing") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Location") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = monthlyRentText,
                onValueChange = { monthlyRentText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Monthly rent (₹)") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = depositText,
                onValueChange = { depositText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Deposit (₹) - optional") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = roomsText,
                onValueChange = { roomsText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Rooms") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                label = { Text("Description") },
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val draft = Listing(
                        id = "",
                        title = title.trim(),
                        location = location.trim(),
                        monthlyRent = monthlyRent ?: 0,
                        deposit = deposit,
                        rooms = rooms ?: 1,
                        description = description.trim().ifEmpty { "-" },
                    )
                    onSave(draft)
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save")
            }

            if (!canSave) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Fill title, location, monthly rent and rooms.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
