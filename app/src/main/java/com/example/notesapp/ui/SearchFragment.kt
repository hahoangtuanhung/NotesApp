package com.example.notesapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentSearchBinding
import com.example.notesapp.setSingleClick
import com.example.notesapp.ui.adapter.NoteAdapter

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private var allNotes = listOf<com.example.notesapp.data.Note>()
    private var search = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Focus vào EditText
        binding.editTextSearch.requestFocus()

        // Hiện bàn phím
        val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.showSoftInput(binding.editTextSearch, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]

        adapter = NoteAdapter { note ->
            val bundle = Bundle().apply { putInt("note_id", note.id) }
            findNavController().navigate(R.id.noteFragment, bundle)
        }

        binding.rvSearchResult.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSearchResult.adapter = adapter

        // Observe once, keep the full list for filtering
        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            allNotes = notes
            filterNotes(binding.editTextSearch.text.toString())
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNotes(s.toString())
                search = true
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.tvCancel.setSingleClick {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun filterNotes(query: String) {
        val filtered = allNotes.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
        }
        if (search == true) adapter.submitList(filtered)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
