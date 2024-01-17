package com.example.test.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.test.data.ApiClient
import com.example.test.data.EntityResponse
import com.example.test.data.ObjectResponse
import com.example.test.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var idList: EntityResponse
    private var currentEntityIndex = 0
    private lateinit var obj: ObjectResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.webViewClient = MyWebViewClient()
        binding.webView.settings.javaScriptEnabled = true

        val apiService = ApiClient.apiService
        lifecycleScope.launch {
            try {
                idList = apiService.getAllIds()
                Toast.makeText(this@MainActivity, "Натисни кнопку", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Помилка", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.nextButton.setOnClickListener {
            nextId()
        }


    }

    private fun nextId() {
        if (currentEntityIndex >= idList.data.size) {
            currentEntityIndex = 0
        }
        loadEntity(currentEntityIndex)
        currentEntityIndex++
    }

    private fun loadEntity(index: Int) {
        lifecycleScope.launch {
            try {
                    obj = ApiClient.apiService.getObject(id = idList.data[index].id)
                    Log.d("CurrentIndex", obj.toString())
                    when (obj.type) {
                        "text" -> displayText(obj.message)
                        "webview" -> displayWebView(obj.url)
                        "image" -> displayImage(obj.url)
                        "game" -> nextId()
                    }

            } catch (e: Exception) {
                // Обработка ошибок
                Log.e("Error", e.message.toString())
                Toast.makeText(this@MainActivity, "Ошибка при загрузке объекта", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun displayImage(url: String?) {
        binding.textView.visibility = View.GONE
        binding.imageView.visibility = View.VISIBLE
        binding.webView.visibility = View.GONE

        Picasso.get().load(url).into(binding.imageView)
    }

    private fun displayWebView(url: String?) {
        binding.textView.visibility = View.GONE
        binding.webView.visibility = View.VISIBLE
        binding.imageView.visibility = View.GONE

        if (!url.isNullOrEmpty()) {
            binding.webView.loadUrl(url)
        } else {
            Toast.makeText(this, "URL для WebView пуст", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayText(message: String?) {
        binding.textView.visibility = View.VISIBLE
        binding.imageView.visibility = View.GONE
        binding.webView.visibility = View.GONE

        binding.textView.text = message
    }

}
