package com.example.thenewsappmobile.repository

import com.example.thenewsappmobile.api.RetrofitInstance
import com.example.thenewsappmobile.db.ArticlesDatabase
import com.example.thenewsappmobile.models.Article

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/



class NewsRepository(val db: ArticlesDatabase) {

    // uses Retrofit to retrieve top news headlines based on the country code and page number.
    suspend fun getHeadlines(countryCode: String, pageNumber : Int) = RetrofitInstance.api.getHeadlines(countryCode, pageNumber)


    // uses Retrofit to search for news base on search query and page number
    suspend fun searchNews(searchQuery : String, pageNumber: Int) = RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    // inserts or updates an Article object in the local database
    // uses the upsert method provided by the ArticleDao interface to perform the operation
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)


    // retrieves all the favorite news articles stored in the local database.
    fun getFavouriteNews() = db.getArticleDao().getAllArticles()

    // deletes the provided Article object from the local database
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)


}