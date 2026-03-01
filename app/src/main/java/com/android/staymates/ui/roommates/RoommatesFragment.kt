package com.android.staymates.ui.roommates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.staymates.databinding.FragmentRoommatesBinding

class RoommatesFragment : Fragment() {
    private var _binding: FragmentRoommatesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoommatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val roommates = getSampleRoommates()
        binding.roommatesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RoommatesAdapter(roommates)
        }
    }

    private fun getSampleRoommates(): List<RoommateProfile> {
        return listOf(
            RoommateProfile(
                "Sarah Johnson",
                "21",
                "Computer Science",
                "95% Match",
                listOf("Non-smoker", "Early bird", "Tidy", "Pet-friendly")
            ),
            RoommateProfile(
                "Mike Chen",
                "22",
                "Business Administration",
                "88% Match",
                listOf("Non-smoker", "Night owl", "Clean", "Quiet")
            ),
            RoommateProfile(
                "Emily Davis",
                "20",
                "Engineering",
                "92% Match",
                listOf("Non-smoker", "Early bird", "Very tidy", "Social")
            ),
            RoommateProfile(
                "Alex Martinez",
                "23",
                "Pre-Med",
                "85% Match",
                listOf("Non-smoker", "Flexible", "Organized", "Studious")
            ),
            RoommateProfile(
                "Jessica Lee",
                "21",
                "Liberal Arts",
                "90% Match",
                listOf("Non-smoker", "Early bird", "Clean", "Pet lover")
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class RoommateProfile(
    val name: String,
    val age: String,
    val major: String,
    val matchPercentage: String,
    val preferences: List<String>
)
