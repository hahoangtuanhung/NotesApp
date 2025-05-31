package com.example.notesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.setSingleClick
import com.example.notesapp.ui.adapter.CategoryAdapter
import com.example.notesapp.ui.adapter.NoteCategoryPagerAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var pagerAdapter: NoteCategoryPagerAdapter

    private val categories = listOf("All", "Work", "Home")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]

        setupCategoryRecyclerView()
        setupViewPager()

        binding.btnAdd.setSingleClick {
            findNavController().navigate(com.example.notesapp.R.id.noteFragment)
        }

        binding.icSearch.setSingleClick {
            findNavController().navigate(com.example.notesapp.R.id.searchFragment)
        }
        binding.icIndex.setSingleClick {
            Toast.makeText(requireContext(), "Tính năng chưa hỗ trợ", Toast.LENGTH_SHORT).show()
        }
        binding.icMore.setSingleClick {
            Toast.makeText(requireContext(), "Tính năng chưa hỗ trợ", Toast.LENGTH_SHORT).show()
        }
        // Update category tab selection on page swipe
        binding.viewPagerNotes.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                categoryAdapter.setSelectedPosition(position)
                binding.rvCategory.smoothScrollToPosition(position)
            }
        })
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter(categories, 0) { category, position ->
            binding.viewPagerNotes.setCurrentItem(position, true)
        }

        binding.rvCategory.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
    }

    private fun setupViewPager() {
        pagerAdapter = NoteCategoryPagerAdapter(this, categories)
        binding.viewPagerNotes.adapter = pagerAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
