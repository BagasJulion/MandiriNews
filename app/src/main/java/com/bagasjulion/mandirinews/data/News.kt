package com.bagasjulion.mandirinews.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class News(

    @field:SerializedName("totalResults")
	val totalResults: Int? = null,

    @field:SerializedName("articles")
	val articles: List<Article>? = null,

    @field:SerializedName("status")
	val status: String? = null

) : Parcelable