package com.example.glyde

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.glyde.ui.HistoryAdapter
import com.example.glyde.ui.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUrl = findViewById<EditText>(R.id.etUrl)
        val btnShorten = findViewById<Button>(R.id.btnShorten)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)

        adapter = HistoryAdapter { shortUrl ->
            copyToClipboard(shortUrl)
        }

        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = adapter

        lifecycleScope.launch {
            viewModel.history.collect { historyList ->
                adapter.submitList(historyList)
            }
        }

        btnShorten.setOnClickListener {
            val url = etUrl.text.toString()
            if (url.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                btnShorten.isEnabled = false
                viewModel.shortenUrl(url) { result ->
                    progressBar.visibility = View.GONE
                    btnShorten.isEnabled = true
                    result.onSuccess { shortUrl ->
                        tvResult.text = shortUrl
                        copyToClipboard(shortUrl)
                    }.onFailure { error ->
                        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Short URL", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
