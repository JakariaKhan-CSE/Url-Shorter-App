package com.example.glyde.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.glyde.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUrl = view.findViewById<TextInputEditText>(R.id.etUrl)
        val btnShorten = view.findViewById<Button>(R.id.btnShorten)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val cvResult = view.findViewById<MaterialCardView>(R.id.cvResult)
        val tvResult = view.findViewById<TextView>(R.id.tvResult)
        val btnCopy = view.findViewById<Button>(R.id.btnCopy)

        btnShorten.setOnClickListener {
            val url = etUrl.text.toString()
            if (url.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                btnShorten.isEnabled = false
                cvResult.visibility = View.GONE
                viewModel.shortenUrl(url) { result ->
                    progressBar.visibility = View.GONE
                    btnShorten.isEnabled = true
                    result.onSuccess { shortUrl ->
                        cvResult.visibility = View.VISIBLE
                        tvResult.text = shortUrl
                        copyToClipboard(shortUrl)
                    }.onFailure { error ->
                        Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a URL", Toast.LENGTH_SHORT).show()
            }
        }

        btnCopy.setOnClickListener {
            val shortUrl = tvResult.text.toString()
            if (shortUrl.isNotEmpty()) {
                copyToClipboard(shortUrl)
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
