package com.android.staymates.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.staymates.ui.theme.GradientEnd
import com.android.staymates.ui.theme.GradientMid
import com.android.staymates.ui.theme.GradientStart

data class Expense(
    val name: String,
    val amount: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Quick Split", "Expense Tracker")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Split Bills",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ── Tab row ────────────────────────────────────────────────────────
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .height(3.dp)
                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                            .background(
                                Brush.horizontalGradient(listOf(GradientStart, GradientMid))
                            )
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // ── Tab content ────────────────────────────────────────────────────
            when (selectedTab) {
                0 -> QuickSplitTab()
                1 -> ExpenseTrackerTab()
            }
        }
    }
}

@Composable
private fun QuickSplitTab() {
    var totalText by remember { mutableStateOf("") }
    var peopleText by remember { mutableStateOf("") }

    val total = totalText.toIntOrNull()
    val people = peopleText.toIntOrNull()
    val perPerson = if (total != null && people != null && people > 0) total / people else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Split Equally",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedTextField(
                    value = totalText,
                    onValueChange = { totalText = it },
                    label = { Text("Total Amount (₹)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = styledTextFieldColors()
                )
                OutlinedTextField(
                    value = peopleText,
                    onValueChange = { peopleText = it },
                    label = { Text("Number of People") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = styledTextFieldColors()
                )
            }
        }

        // Result card
        AnimatedVisibility(
            visible = perPerson != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (perPerson != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(GradientStart, GradientMid, GradientEnd),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, 0f)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            "Each Person Pays",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "₹$perPerson",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "from ₹$total total · ${people} people",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpenseTrackerTab() {
    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var peopleText by remember { mutableStateOf("") }
    val expenses = remember { mutableStateListOf<Expense>() }

    val expenseTotal = expenses.sumOf { it.amount }
    val expensePeople = peopleText.toIntOrNull() ?: 0
    val expensePerPerson = if (expenseTotal > 0 && expensePeople > 0) expenseTotal / expensePeople else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // People input
        OutlinedTextField(
            value = peopleText,
            onValueChange = { peopleText = it },
            label = { Text("Number of People") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(14.dp),
            colors = styledTextFieldColors()
        )

        // Add expense
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Add Expense",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = expenseName,
                        onValueChange = { expenseName = it },
                        label = { Text("Item") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = styledTextFieldColors()
                    )
                    OutlinedTextField(
                        value = expenseAmount,
                        onValueChange = { expenseAmount = it },
                        label = { Text("₹") },
                        modifier = Modifier.width(100.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(14.dp),
                        colors = styledTextFieldColors()
                    )
                }
                Button(
                    onClick = {
                        val amount = expenseAmount.toIntOrNull()
                        if (expenseName.isNotBlank() && amount != null && amount > 0) {
                            expenses.add(Expense(expenseName.trim(), amount))
                            expenseName = ""
                            expenseAmount = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = expenseName.isNotBlank() && expenseAmount.toIntOrNull() != null,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Add Expense", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Expense list
        if (expenses.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Expenses",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))

                    expenses.forEachIndexed { index, expense ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    expense.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "₹${expense.amount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(4.dp))
                                IconButton(
                                    onClick = { expenses.removeAt(index) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Remove",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        if (index < expenses.lastIndex) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            "₹$expenseTotal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Per-person result
            AnimatedVisibility(
                visible = expensePerPerson != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (expensePerPerson != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(GradientMid, GradientEnd),
                                    start = Offset(0f, 0f),
                                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Each Pays",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    "₹$expensePerPerson",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            Text(
                                "÷ $expensePeople",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White.copy(alpha = 0.4f),
                                fontSize = 40.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
