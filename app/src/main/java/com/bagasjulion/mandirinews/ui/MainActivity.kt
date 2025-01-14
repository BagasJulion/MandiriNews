package com.bagasjulion.mandirinews.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagasjulion.mandirinews.R
import com.bagasjulion.mandirinews.adapter.AllNewsAdapter
import com.bagasjulion.mandirinews.adapter.TopNewsAdapter
import com.bagasjulion.mandirinews.api.ApiConfig
import com.bagasjulion.mandirinews.news.DetailAllNews
import com.bagasjulion.mandirinews.news.DetailTopNews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var topNewsAdapter: TopNewsAdapter
    private lateinit var allNewsAdapter: AllNewsAdapter
    private lateinit var resultTextView: TextView

    private var currentPageTopNews = 1
    private var isLoadingTopNews = false
    private var currentPageAllNews = 1
    private var isLoadingAllNews = false
    private var lastVisibleItemPosition = 0

    fun openAllNewsPage(view: View) {
        val textView = findViewById<TextView>(R.id.semuaberita)
        val text = textView.text.toString()

        val spannable = SpannableString(text)
        spannable.setSpan(UnderlineSpan(), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    }

    fun openTopNewsPage(view: View) {
        val textView = findViewById<TextView>(R.id.beritaterkini)
        val text = textView.text.toString()

        val spannable = SpannableString(text)
        spannable.setSpan(UnderlineSpan(), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(300)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.result)
//        resultTextViewAll = findViewById(R.id.resultall)
        val mandiriLogoImageView = findViewById<ImageView>(R.id.iv_mandiri)

        mandiriLogoImageView.setOnClickListener {
            recreate()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val recyclerViewAllNews: RecyclerView = findViewById(R.id.recyclerViewAllNews)

        topNewsAdapter = TopNewsAdapter(emptyList())
        allNewsAdapter = AllNewsAdapter(emptyList())

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = topNewsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!isLoadingTopNews && (visibleItemCount + lastVisibleItemPosition) >= totalItemCount - 5) {
                        // Load more data when the user is near the end
                        isLoadingTopNews = true
                        currentPageTopNews++
                        fetchNews()
                    }
                }
            })

            // Handle item click
            topNewsAdapter.onItemClick = { article ->
                val intent = Intent(this@MainActivity, DetailTopNews::class.java)
                intent.putExtra("Article", article)
                startActivity(intent)
            }
        }

        recyclerViewAllNews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = allNewsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!isLoadingAllNews && (visibleItemCount + lastVisibleItemPosition) >= totalItemCount - 5) {
                        // Load more data when the user is near the end
                        isLoadingAllNews = true
                        currentPageAllNews++
                        fetchAllNews()
                    }
                }
            })

            // Handle item click
            allNewsAdapter.onItemClick = { article ->
                val intent = Intent(this@MainActivity, DetailAllNews::class.java)
                intent.putExtra("Article", article)
                startActivity(intent)
            }
        }

        fetchNews()
        fetchAllNews()



    }

    private fun fetchNews() {
        val apiKey = "3957e28cfe7f4102828a8d908d969bef"
        val country = "us"

        val apiService = ApiConfig.apiService
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getTopHeadlines(country, currentPageTopNews, apiKey)
                if (response.isSuccessful) {
                    val articles = response.body()?.articles ?: emptyList()
                    resultTextView.text = "${response.body()?.totalResults ?: 0} Result found :"
                    val validArticles = articles.filter { article ->
                        article.publishedAt != null &&
                                article.author != null &&
                                article.urlToImage != null &&
                                article.description != null &&
                                article.source != null &&
                                article.title != null &&
                                article.url != null &&
                                article.content != null
                    }

                    withContext(Dispatchers.Main) {
                        // Append the new data to the adapter
                        topNewsAdapter.articles += validArticles
                        topNewsAdapter.notifyDataSetChanged()
                        isLoadingTopNews = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchAllNews() {
        val apiKey = "3957e28cfe7f4102828a8d908d969bef"
        val q = "indonesia"
        val sort = "relevancy"
        val apiService = ApiConfig.apiService
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getEverything(q, currentPageAllNews, apiKey,sort)
                if (response.isSuccessful) {
                    val articles = response.body()?.articles ?: emptyList()

                    val validArticles = articles.filter { article ->
                        article.publishedAt != null &&
                                article.author != null &&
                                article.urlToImage != null &&
                                article.description != null &&
                                article.source != null &&
                                article.title != null &&
                                article.url != null &&
                                article.content != null
                    }

                    withContext(Dispatchers.Main) {
                        // Append the new data to the adapter
                        allNewsAdapter.newsList += validArticles
                        allNewsAdapter.notifyDataSetChanged()
                        isLoadingAllNews = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
