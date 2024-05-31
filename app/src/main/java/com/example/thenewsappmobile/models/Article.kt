package com.example.thenewsappmobile.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/


@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Serializable