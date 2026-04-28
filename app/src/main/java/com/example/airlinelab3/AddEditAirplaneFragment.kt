package com.example.airlinelab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.airlinelab3.data.AppDatabase
import com.example.airlinelab3.databinding.FragmentAddAirplaneBinding
import com.example.airlinelab3.viewmodel.AirplaneViewModel
import com.example.airlinelab3.viewmodel.AirplaneViewModelFactory
import kotlinx.coroutines.launch

class AddEditAirplaneFragment : Fragment() {
    private var _binding: FragmentAddAirplaneBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AirplaneViewModel by activityViewModels {
        AirplaneViewModelFactory(
            AppDatabase.getDatabase(requireContext().applicationContext).airplaneDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAirplaneBinding.inflate(inflater, container, false)
        return binding.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val airplaneId = arguments?.getInt("airplaneId") ?: -1
            val isEditMode = airplaneId != -1

            if (isEditMode) {
                binding.buttonSave.text = "Оновити"

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.allAirplanes.collect { list ->
                        val airplane = list.find { it.id == airplaneId }
                        airplane?.let {
                            binding.editModel.setText(it.model)
                            binding.editCapacity.setText(it.capacity.toString())
                            binding.editManufacturer.setText(it.manufacturer)
                            binding.editAirline.setText(it.airlineName)
                        }
                    }
                }
            }

        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonSave.setOnClickListener {
            val model = binding.editModel.text.toString().trim()
            val airlineName = binding.editAirline.text.toString().trim()
            val manufacturer = binding.editManufacturer.text.toString().trim()
            val capacityText = binding.editCapacity.text.toString().trim()
            val airline = binding.editAirline.text.toString().trim()

            // validation: all fields must be filled
            if (model.isEmpty() || airlineName.isEmpty() || manufacturer.isEmpty() || capacityText.isEmpty()) {
                android.widget.Toast.makeText(requireContext(), "Будь ласка, заповніть всі поля", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // capacity must be a valid positive integer
            val capacity = capacityText.toIntOrNull()
            if (capacity == null || capacity <= 0) {
                android.widget.Toast.makeText(requireContext(), "Місткість має бути більше 0", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (capacity == null) {
                android.widget.Toast.makeText(requireContext(), "Введіть коректне число", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (capacity <= 0 || capacity > 1000) { // додамо реалістичний ліміт для літака
                android.widget.Toast.makeText(requireContext(), "Місткість має бути від 1 до 1000", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                viewModel.updateAirplane(airplaneId, model, capacityText, manufacturer, airline)
            } else {
                viewModel.insertAirplane(model, capacityText, manufacturer, airline)
            }

            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}