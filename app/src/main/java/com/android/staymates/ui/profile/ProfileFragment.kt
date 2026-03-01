package com.android.staymates.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.staymates.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.rentCalculatorCard.setOnClickListener {
            showRentCalculatorDialog()
        }

        binding.savedListingsCard.setOnClickListener {
        }

        binding.myListingsCard.setOnClickListener {
        }

        binding.settingsCard.setOnClickListener {
        }

        binding.logoutButton.setOnClickListener {
        }
    }

    private fun showRentCalculatorDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(
                com.android.staymates.R.layout.dialog_rent_calculator,
                null
            )

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(com.android.staymates.R.string.rent_calculator_title)
            .setView(dialogView)
            .setPositiveButton("Calculate") { _, _ ->
                val totalRentInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                    com.android.staymates.R.id.totalRentInput
                )
                val roommatesInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                    com.android.staymates.R.id.roommatesInput
                )

                val totalRent = totalRentInput.text.toString().toDoubleOrNull() ?: 0.0
                val roommates = roommatesInput.text.toString().toIntOrNull() ?: 1

                if (totalRent > 0 && roommates > 0) {
                    val perPerson = totalRent / roommates
                    showResultDialog(perPerson)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun showResultDialog(perPerson: Double) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Rent Split Result")
            .setMessage("Each person pays: $${String.format("%.2f", perPerson)}")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
