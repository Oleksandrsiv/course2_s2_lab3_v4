package com.example.airlinelab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinelab3.data.AppDatabase
import com.example.airlinelab3.databinding.FragmentListBinding
import com.example.airlinelab3.viewmodel.AirplaneViewModel
import com.example.airlinelab3.viewmodel.AirplaneViewModelFactory
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
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
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AirplaneAdapter(
            onDeleteClick = { airplane -> viewModel.deleteAirplane(airplane) },
            onItemClick = { airplane ->
                val bundle = Bundle().apply {
                    putInt("airplaneId", airplane.id)
                }
                findNavController().navigate(R.id.action_list_to_add, bundle)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allAirplanes.collect { listOfAirplanes ->
                adapter.submitList(listOfAirplanes)
            }
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_add)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}