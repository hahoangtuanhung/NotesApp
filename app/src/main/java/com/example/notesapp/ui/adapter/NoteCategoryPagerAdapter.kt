package com.example.notesapp.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.notesapp.ui.NoteListFragment

class NoteCategoryPagerAdapter(
    fragment: Fragment,
    private val categories: List<String>
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = categories.size

    override fun createFragment(position: Int): Fragment {
        val fragment = NoteListFragment()
        fragment.arguments = Bundle().apply {
            putString("category", categories[position])
        }
        return fragment
    }
}