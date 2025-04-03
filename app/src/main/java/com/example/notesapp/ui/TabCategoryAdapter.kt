package com.example.notesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.databinding.ItemCategoryBinding

class TabCategoryAdapter(
    private val categories: List<String>,
    private val selectedCategory: String,
    private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<TabCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            binding.tvCategory.text = category
            if (category == selectedCategory) {
                binding.tvCategory.setBackgroundResource(R.drawable.bg_category_selector)
            } else {
                binding.tvCategory.setBackgroundResource(R.drawable.bg_category_unselected)
            }
            binding.tvCategory.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size
}
