package com.example.notesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notesapp.data.Note
import com.example.notesapp.databinding.FragmentNoteBinding
import com.example.notesapp.hide
import com.example.notesapp.setSingleClick
import java.text.SimpleDateFormat
import java.util.*

class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private var noteId: Int? = null
    private var currentNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]

        noteId = arguments?.getInt("note_id")
        if (noteId != null) {
            viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
                currentNote = notes.find { it.id == noteId }
                currentNote?.let { note ->
                    binding.edtNoteTitle.setText(note.title)
                    binding.edtNoteDes.setText(note.content)
                    binding.tvTimestamp.text = "Sửa đổi lần cuối: ${formatDate(note.timestamp)}"
                }
            }
        } else {
            binding.icDelete.hide()
            // Ghi chú mới → hiển thị thời gian hiện tại
            binding.tvTimestamp.text = "Đang tạo: ${formatDate(System.currentTimeMillis())}"
        }
        binding.icMore.setSingleClick {
            Toast.makeText(requireContext(), "Tính năng chưa hỗ trợ", Toast.LENGTH_SHORT).show()
        }
        binding.icCheck.setSingleClick {
            val newTitle = binding.edtNoteTitle.text.toString()
            val newContent = binding.edtNoteDes.text.toString()

            // Nếu nội dung rỗng thì không làm gì cả
            if (newTitle.isBlank() && newContent.isBlank()) {
                Toast.makeText(requireContext(), "Bản ghi trống", Toast.LENGTH_SHORT).show()
                return@setSingleClick}

            // So sánh với nội dung cũ
            val hasChanged = currentNote?.let {
                it.title != newTitle || it.content != newContent
            } ?: true // Nếu là ghi chú mới thì luôn coi là có thay đổi

            if (hasChanged) {
                val selectedCategory = binding.spinnerCategory.selectedItem.toString()
                val newNote = currentNote?.copy(
                    title = newTitle,
                    content = newContent,
                    timestamp = System.currentTimeMillis(),
                    category = selectedCategory
                ) ?: Note(
                    title = newTitle,
                    content = newContent,
                    timestamp = System.currentTimeMillis(),
                    category = selectedCategory
                )

                viewModel.insert(newNote)
                Toast.makeText(requireContext(), "Lưu ghi chú thành công", Toast.LENGTH_SHORT).show()
            }

            findNavController().navigateUp()
        }
        // xoa note
        binding.icDelete.setSingleClick {
            currentNote?.let {
                viewModel.delete(it)
                Toast.makeText(requireContext(), "Đã xoá ghi chú", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
        binding.icShare.setSingleClick {
            val title = binding.edtNoteTitle.text.toString()
            val content = binding.edtNoteDes.text.toString()

            val shareText = buildString {
                if (title.isNotBlank()) append("Tiêu đề: $title\n")
                if (content.isNotBlank()) append("Nội dung: $content")
            }

            if (shareText.isNotBlank()) {
                val intent = android.content.Intent().apply {
                    action = android.content.Intent.ACTION_SEND
                    putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                startActivity(android.content.Intent.createChooser(intent, "Chia sẻ ghi chú qua"))
            } else {
                android.widget.Toast.makeText(requireContext(), "Không có nội dung để chia sẻ", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
