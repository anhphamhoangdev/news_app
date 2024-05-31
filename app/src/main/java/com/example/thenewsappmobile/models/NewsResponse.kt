package com.example.thenewsappmobile.models

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/


data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)