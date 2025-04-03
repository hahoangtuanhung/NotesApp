package com.example.notesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentNoteListBinding
import com.example.notesapp.ui.adapter.NoteAdapter

class NoteListFragment : Fragment() {
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val category = arguments?.getString("category") ?: "All"
        noteAdapter = NoteAdapter { note ->
            val bundle = Bundle().apply { putInt("note_id", note.id) }
            findNavController().navigate(R.id.noteFragment, bundle)
        }

        binding.rvCategory.apply {
            adapter = noteAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        viewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        viewModel.getNotesByCategory(category).observe(viewLifecycleOwner) {
            noteAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
