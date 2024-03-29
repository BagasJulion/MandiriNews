package com.bagasjulion.mandirinews.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bagasjulion.mandirinews.R
import com.bagasjulion.mandirinews.data.Article
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class AllNewsAdapter(var newsList:List<Article>) : RecyclerView.Adapter<AllNewsAdapter.NewsViewHolder>(){

    var onItemClick : ((Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_berita, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val newsImageView: ImageView = itemView.findViewById(R.id.newsImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val publishNewsTextView: TextView = itemView.findViewById(R.id.publish_news)
        private val nameTextView: TextView = itemView.findViewById(R.id.authorTextViewAll)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(newsList[position])
                }
            }
        }

        fun bind(newsItem: Article) {
            titleTextView.text = newsItem.title
            publishNewsTextView.text = newsItem.getFormattedPublishedAt()
            nameTextView.text = newsItem.source?.name

            Glide.with(itemView)
                .load(newsItem.urlToImage)
                .placeholder(R.drawable.news)
                .into(newsImageView)
            }
        }

    private fun Article.getFormattedPublishedAt(): String? {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

        if (publishedAt.isNullOrEmpty()) {
            return null
        }

        return try {
            val publishedAtLocalDateTime = LocalDateTime.parse(publishedAt, dateTimeFormatter)

            val formattedDateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy")
            publishedAtLocalDateTime.format(formattedDateTimeFormatter)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            null
        }
    }
}