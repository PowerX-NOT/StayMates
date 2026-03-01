package com.android.staymates.ui.roommates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.staymates.databinding.ItemRoommateBinding
import com.google.android.material.chip.Chip

class RoommatesAdapter(private val roommates: List<RoommateProfile>) :
    RecyclerView.Adapter<RoommatesAdapter.RoommateViewHolder>() {

    inner class RoommateViewHolder(private val binding: ItemRoommateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(roommate: RoommateProfile) {
            binding.nameText.text = roommate.name
            binding.ageText.text = "Age: ${roommate.age}"
            binding.majorText.text = roommate.major
            binding.matchPercentageText.text = roommate.matchPercentage

            binding.preferencesChipGroup.removeAllViews()
            roommate.preferences.forEach { preference ->
                val chip = Chip(binding.root.context).apply {
                    text = preference
                    isClickable = false
                    isCheckable = false
                }
                binding.preferencesChipGroup.addView(chip)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoommateViewHolder {
        val binding = ItemRoommateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoommateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoommateViewHolder, position: Int) {
        holder.bind(roommates[position])
    }

    override fun getItemCount() = roommates.size
}
