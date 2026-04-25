package com.example.airlinelab3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.airlinelab3.data.AirplaneDao

class AirplaneViewModelFactory(private val airplaneDao: AirplaneDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AirplaneViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AirplaneViewModel(airplaneDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}