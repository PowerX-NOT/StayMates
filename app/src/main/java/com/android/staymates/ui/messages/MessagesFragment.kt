package com.android.staymates.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.staymates.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val conversations = getSampleConversations()

        if (conversations.isEmpty()) {
            binding.emptyStateText.isVisible = true
            binding.messagesRecyclerView.isVisible = false
        } else {
            binding.emptyStateText.isVisible = false
            binding.messagesRecyclerView.isVisible = true
            binding.messagesRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = MessagesAdapter(conversations)
            }
        }
    }

    private fun getSampleConversations(): List<Conversation> {
        return listOf(
            Conversation(
                "Sarah Johnson",
                "That sounds great! When can we meet?",
                "2m ago",
                true
            ),
            Conversation(
                "Mike Chen",
                "I'm interested in viewing the apartment",
                "1h ago",
                false
            ),
            Conversation(
                "Emily Davis",
                "Thanks for sharing the details!",
                "3h ago",
                false
            ),
            Conversation(
                "Property Owner",
                "The place is available from next month",
                "Yesterday",
                true
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class Conversation(
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val hasUnread: Boolean
)
