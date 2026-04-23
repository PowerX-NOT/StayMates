package com.android.staymates.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.staymates.data.models.ConversationWithDetails
import com.android.staymates.data.repositories.ChatRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userId: String?,
    onChatClick: (String) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var conversations by remember { mutableStateOf<List<ConversationWithDetails>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        if (userId == null) { error = "User not logged in"; isLoading = false; return@LaunchedEffect }
        val repository = ChatRepository(userId)
        coroutineScope.launch {
            try {
                isLoading = true
                conversations = repository.getConversations()
                error = null
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Messages",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        if (conversations.isNotEmpty()) {
                            Text(
                                "${conversations.size} conversations",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("😕", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Error loading conversations: $error",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            conversations.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💬", fontSize = 52.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "No conversations yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Match with roommates to start chatting",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(conversations, key = { it.conversation.id }) { convDetails ->
                        ChatConversationCard(
                            conversationDetails = convDetails,
                            onClick = { onChatClick(convDetails.conversation.id) }
                        )
                    }
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val now = Instant.now()
        val minutes = ChronoUnit.MINUTES.between(instant, now)
        val hours = ChronoUnit.HOURS.between(instant, now)
        val days = ChronoUnit.DAYS.between(instant, now)
        when {
            minutes < 60 -> "${minutes}m"
            hours < 24 -> "${hours}h"
            days < 7 -> "${days}d"
            else -> DateTimeFormatter.ofPattern("MMM d").withZone(ZoneId.systemDefault()).format(instant)
        }
    } catch (e: Exception) { timestamp }
}

@Composable
private fun ChatConversationCard(
    conversationDetails: ConversationWithDetails,
    onClick: () -> Unit
) {
    val otherParticipant = conversationDetails.otherParticipant
    val lastMessage = conversationDetails.lastMessage
    val unreadCount = conversationDetails.unreadCount
    val (colorA, colorB) = avatarColors(otherParticipant.name)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gradient avatar
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(colorA, colorB))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = otherParticipant.name.take(2).uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = otherParticipant.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = lastMessage?.createdAt?.let { formatTimestamp(it) } ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (unreadCount > 0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
                Spacer(Modifier.height(3.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lastMessage?.content ?: "No messages yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (unreadCount > 0) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (unreadCount > 0) {
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = unreadCount.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
