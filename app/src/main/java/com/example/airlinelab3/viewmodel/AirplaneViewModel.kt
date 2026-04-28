package com.example.airlinelab3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.airlinelab3.data.AirplaneDao
import com.example.airlinelab3.model.Airplane
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AirplaneViewModel(private val airplaneDao: AirplaneDao) : ViewModel() {

    val allAirplanes: Flow<List<Airplane>> = airplaneDao.getAllAirplanes()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    fun insertAirplane(model: String, capacityStr: String, manufacturer: String, airlineName: String) {
        viewModelScope.launch {
            try {
                // must be not empty
                if (!isInputNotEmpty(model, capacityStr, manufacturer, airlineName)) {
                    _errorEvent.emit("Помилка: Заповніть всі поля")
                    return@launch
                }

                // capacity must be a valid positive integer
                if (!isInputValidPositive(capacityStr)) {
                    _errorEvent.emit("Помилка: Місткість має бути додатним числом")
                    return@launch
                }

                // if validation passed => create the Airplane object
                val newAirplane = Airplane(
                    model = model,
                    capacity = capacityStr.toInt(),
                    manufacturer = manufacturer,
                    airlineName = airlineName
                )

                airplaneDao.insert(newAirplane)

            } catch (e: Exception) {
                _errorEvent.emit("Помилка в опрацюванні запиту: ${e.localizedMessage}")
            }
        }
    }

    fun updateAirplane(id: Int, model: String, capacityText: String, manufacturer: String, airlineName: String) {
        viewModelScope.launch {
            try {
                val capacity = capacityText.toIntOrNull() ?: 0

                val updatedAirplane = Airplane(
                    id = id,
                    model = model,
                    airlineName = airlineName,
                    manufacturer = manufacturer,
                    capacity = capacity
                )
                airplaneDao.update(updatedAirplane)
            } catch (e: Exception) {
                _errorEvent.emit("Помилка оновлення: ${e.message}")
            }
        }
    }

    fun deleteAirplane(airplane: Airplane) {
        viewModelScope.launch {
            try {
                airplaneDao.delete(airplane)
            } catch (e: Exception) {
                _errorEvent.emit("Помилка видалення: ${e.localizedMessage}")
            }
        }
    }

    private fun isInputNotEmpty(model: String, capacity: String, manufacturer: String, airlineName: String): Boolean {
        return !(model.isBlank() || capacity.isBlank() || manufacturer.isBlank() || airlineName.isBlank())
    }

    private fun isInputValidPositive(capacity: String): Boolean {
        val capacityInt = capacity.toIntOrNull()
        return capacityInt != null && capacityInt > 0
    }



}

