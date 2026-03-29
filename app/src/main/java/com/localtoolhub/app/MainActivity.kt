package com.localtoolhub.app

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.localtoolhub.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerAdapter: DrawerAdapter
    private var currentTool: ToolItem = ToolRegistry.items.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopBar()
        setupDrawer()
        setupWebView()
        drawerAdapter.select(currentTool.id)
        loadTool(currentTool)
        handleSystemBack()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                binding.webView.reload()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupTopBar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.menuButton.setOnClickListener {
            toggleDrawer()
        }
    }

    private fun setupDrawer() {
        binding.drawerLayout.setScrimColor(Color.parseColor("#A6141620"))
        applyResponsiveDrawerWidth()

        drawerAdapter = DrawerAdapter(ToolRegistry.items) { tool ->
            drawerAdapter.select(tool.id)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            if (tool.id != currentTool.id) {
                loadTool(tool)
            }
        }

        binding.drawerRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = drawerAdapter
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = false
            loadWithOverviewMode = true
            useWideViewPort = true
        }

        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean = false

            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                binding.progressBar.isVisible = true
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String?) {
                binding.progressBar.isVisible = false
                super.onPageFinished(view, url)
            }
        }
    }

    private fun loadTool(tool: ToolItem) {
        currentTool = tool
        binding.titleView.text = tool.title
        binding.subtitleView.text = tool.url
        binding.webView.loadUrl(tool.url)
    }

    private fun handleSystemBack() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    binding.webView.canGoBack() -> {
                        binding.webView.goBack()
                    }
                    else -> finish()
                }
            }
        })
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun applyResponsiveDrawerWidth() {
        val metrics = resources.displayMetrics
        val screenWidth = metrics.widthPixels
        val targetWidth = (screenWidth * 0.78f).toInt()
        val maxWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            360f,
            metrics
        ).toInt()

        binding.drawerContainer.layoutParams = binding.drawerContainer.layoutParams.apply {
            width = minOf(targetWidth, maxWidth)
        }
    }
}
