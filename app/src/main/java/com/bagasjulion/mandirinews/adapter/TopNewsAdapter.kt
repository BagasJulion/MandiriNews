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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class TopNewsAdapter(var articles: List<Article>) : RecyclerView.Adapter<TopNewsAdapter.ViewHolder>() {

    var onItemClick : ((Article) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headline: TextView = itemView.findViewById(R.id.tv_headline)
        val publicationDate: TextView = itemView.findViewById(R.id.tv_publicationDate)
        val placeOfPublication: TextView = itemView.findViewById(R.id.tv_placeOfPublication)
        val newsImage: ImageView = itemView.findViewById(R.id.iv_news)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_beritaterkini, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        val sourceName = article.source?.name

        holder.headline.text = article.title
        holder.placeOfPublication.text = sourceName
        holder.publicationDate.text = article.getFormattedPublishedAt()

        Glide.with(holder.itemView)
            .load(article.urlToImage)
            .transform(RoundedCorners(20))
            .placeholder(R.drawable.news)
            .error(R.drawable.news)
            .into(holder.newsImage)


        holder.itemView.setOnClickListener {
            onItemClick?.invoke(article)
        }
    }


    override fun getItemCount(): Int {
        return articles.size
    }


    private fun Article.getFormattedPublishedAt(): String? {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

        if (publishedAt == null) {
            return null
        }

        return try {
            val publishedAtLocalDateTime = LocalDateTime.parse(publishedAt, dateTimeFormatter)

            val formattedDateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy")
            publishedAtLocalDateTime.format(formattedDateTimeFormatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }
}