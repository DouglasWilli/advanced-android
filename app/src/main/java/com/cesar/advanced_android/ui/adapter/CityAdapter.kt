package com.cesar.advanced_android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cesar.advanced_android.databinding.ItemCityBinding
import com.cesar.advanced_android.models.City
import java.io.File

class CityAdapter(
    private val onItemClick: (City) -> Unit
): ListAdapter<City, CityAdapter.ViewHolder>(SearchDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            binding.apply {
                tvItemName.text = city.local
                tvItemDias.text = city.dias
                tvItemPreco.text = city.preco
                imgCity.setImageURI(File(itemView.context.filesDir, city.imagem).toUri())
                itemView.setOnClickListener {
                    onItemClick(city)
                }
            }
        }
    }


    class SearchDiff: DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City) = oldItem == newItem
        override fun areContentsTheSame(oldItem: City, newItem: City) = oldItem == newItem
    }
}