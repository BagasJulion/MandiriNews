package com.bagasjulion.mandirinews.news

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bagasjulion.mandirinews.R
import com.bagasjulion.mandirinews.data.Article
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailAllNews : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_all_news)

        val itemButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        itemButton.setOnClickListener {
            finish()
        }

        val article = intent.getParcelableExtra<Article>("Article")
        if (article != null) {
            val titleTextView: TextView = findViewById(R.id.titleDetail)
            val authorTextView: TextView = findViewById(R.id.authorTextView)
            val contentTextView: TextView = findViewById(R.id.content)
            val newsImage: ImageView = findViewById(R.id.imageView)
            val publishTextView: TextView = findViewById(R.id.publish_news)
            val descriptionTextView: TextView = findViewById(R.id.desc)
            val linkNews: TextView = findViewById(R.id.urlnews)

            titleTextView.text = article.title
            authorTextView.text = article.source?.name
            contentTextView.text = article.content
            publishTextView.text = article.getFormattedPublishedAt()
            descriptionTextView.text = article.description

            Glide.with(this)
                .load(article.urlToImage)
                .transform(RoundedCorners(20))
                .error(R.drawable.news)
                .into(newsImage)

            linkNews.setOnClickListener {
                val url = article?.url
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
    }

    private fun Article.getFormattedPublishedAt(): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val publishedAtLocalDateTime = LocalDateTime.parse(publishedAt, dateTimeFormatter)

        val formattedDateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy")
        return publishedAtLocalDateTime.format(formattedDateTimeFormatter)
    }
}