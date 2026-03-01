package com.android.staymates.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.staymates.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val listings = getSampleListings()
        binding.listingsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ListingsAdapter(listings)
        }
    }

    private fun getSampleListings(): List<PropertyListing> {
        return listOf(
            PropertyListing(
                "Modern Studio near Campus",
                "$800/month",
                "University District",
                "1 bed, 1 bath",
                true
            ),
            PropertyListing(
                "Spacious 2BR Apartment",
                "$1,200/month",
                "Downtown",
                "2 bed, 2 bath",
                true
            ),
            PropertyListing(
                "Cozy Room in Shared House",
                "$500/month",
                "Near College",
                "Shared, 1 bath",
                false
            ),
            PropertyListing(
                "Luxury Student Housing",
                "$950/month",
                "Campus Area",
                "1 bed, 1 bath",
                true
            ),
            PropertyListing(
                "Affordable Single Room",
                "$600/month",
                "Student Housing Complex",
                "Private room, Shared bath",
                false
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class PropertyListing(
    val title: String,
    val price: String,
    val location: String,
    val details: String,
    val verified: Boolean
)
