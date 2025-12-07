package com.example.glyde.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.glyde.R
import kotlinx.coroutines.launch

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvHistory = view.findViewById<RecyclerView>(R.id.rvHistory)

        adapter = HistoryAdapter { shortUrl ->
            copyToClipboard(shortUrl)
        }

        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.history.collect { historyList ->
                adapter.submitList(historyList)
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Short URL", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
