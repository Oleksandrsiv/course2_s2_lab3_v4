package com.example.airlinelab3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinelab3.model.Airplane

class AirplaneAdapter(
    private val onDeleteClick: (Airplane) -> Unit,
    private val onItemClick: (Airplane) -> Unit
) : ListAdapter<Airplane, AirplaneAdapter.AirplaneViewHolder>(AirplaneDiffCallback()) {

    // create card for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirplaneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_airplane, parent, false)
        return AirplaneViewHolder(view)
    }

    // bind data to the card
    override fun onBindViewHolder(holder: AirplaneViewHolder, position: Int) {
        val airplane = getItem(position)
        holder.bind(airplane, onDeleteClick, onItemClick)
    }


    class AirplaneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textModel: TextView = itemView.findViewById(R.id.text_model)
        private val textDetails: TextView = itemView.findViewById(R.id.text_airline_manufacturer)
        private val textCapacity: TextView = itemView.findViewById(R.id.text_capacity)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)


        fun bind(airplane: Airplane, onDeleteClick: (Airplane) -> Unit, onItemClick: (Airplane) -> Unit) {
            textModel.text = airplane.model
            textDetails.text = "${airplane.airlineName} • ${airplane.manufacturer}"
            textCapacity.text = "Capacity: ${airplane.capacity}"

            deleteButton.setOnClickListener {
                onDeleteClick(airplane)
            }

            itemView.setOnClickListener {
                onItemClick(airplane)
            }
        }
    }


    class AirplaneDiffCallback : DiffUtil.ItemCallback<Airplane>() {
        override fun areItemsTheSame(oldItem: Airplane, newItem: Airplane): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Airplane, newItem: Airplane): Boolean {
            return oldItem == newItem
        }
    }
}