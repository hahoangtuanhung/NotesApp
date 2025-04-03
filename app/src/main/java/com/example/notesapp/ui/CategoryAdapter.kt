package com.example.notesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categories: List<String>,
    private var selectedPosition: Int = 0,
    private val onCategorySelected: (String, Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String, isSelected: Boolean) {
            binding.tvCategory.text = category
            binding.tvCategory.setBackgroundResource(
                if (isSelected) com.example.notesapp.R.drawable.bg_category_selected
                else com.example.notesapp.R.drawable.bg_category_unselected
            )

            binding.tvCategory.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)
                onCategorySelected(category, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], position == selectedPosition)
    }

    override fun getItemCount() = categories.size

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }
}
