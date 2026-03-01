package com.android.staymates.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.staymates.databinding.ItemListingBinding

class ListingsAdapter(private val listings: List<PropertyListing>) :
    RecyclerView.Adapter<ListingsAdapter.ListingViewHolder>() {

    inner class ListingViewHolder(private val binding: ItemListingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listing: PropertyListing) {
            binding.titleText.text = listing.title
            binding.priceText.text = listing.price
            binding.locationText.text = listing.location
            binding.detailsText.text = listing.details
            binding.verifiedBadge.isVisible = listing.verified
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val binding = ItemListingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        holder.bind(listings[position])
    }

    override fun getItemCount() = listings.size
}
