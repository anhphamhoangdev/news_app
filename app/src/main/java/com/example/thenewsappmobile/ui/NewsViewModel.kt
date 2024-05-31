package com.example.thenewsappmobile.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.thenewsappmobile.models.Article
import com.example.thenewsappmobile.models.NewsResponse
import com.example.thenewsappmobile.repository.NewsRepository
import com.example.thenewsappmobile.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response


/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/



// inherits from AndroidViewModel - part of the MVVM
// intermediary between the UI (View) and the data (Model).

class NewsViewModel(app: Application, val newsRepository: NewsRepository): AndroidViewModel(app) {

    // holds the headlines news data
    val headlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    // current page for headlines
    var headlinesPage = 1

    // stores the NewsResponse object for headlines.
    var headlinesResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchResponse: NewsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null


    // when the app starts -> calls the getHeadlines and set countryCode = us to get the default headline news
    init {
        getHeadlines("us")
    }

    // fetch headlines
    fun getHeadlines(countryCode: String) = viewModelScope.launch {
        headlinesInternet(countryCode)
    }


    // fetch news articles when searching
    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }

    // handles the response from the API call to fetch headlines
    private fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        // checks if the response is successful or not
        if (response.isSuccessful) {
            // If successful, updates the news list by merging the new news list with the current news list (if any)
            response.body()?.let { resultResponse ->
                headlinesPage++
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    val oldArticle = headlinesResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                // returns a Resource object containing the news data
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        // if not successful, return error
        return Resource.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        // checks if the response is successful or not
        if (response.isSuccessful) {
            // If successful
            response.body()?.let { resultResponse ->
                // check search response null or new search query has changed
                if (searchResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticle = searchResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                // returns a Resource object containing the news data
                return Resource.Success(searchResponse ?: resultResponse)
            }
        }
        // if not successful, return error
        return Resource.Error(response.message())
    }

    // add news article to favorite
    fun addToFavorite(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    // get list favorite
    fun getFavouriteNews() = newsRepository.getFavouriteNews()

    // delete article from favorite
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    // checks whether the device is connected to the Internet or not by using the ConnectivityManager
    fun internetConnection(context: Context): Boolean
    {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }


    // fetch news headlines from a country
    private suspend fun headlinesInternet(countryCode: String)
    {
        // update the value of the headlines LiveData to Resource.Loading()
        // this notifies the UI components that data is being loaded
        headlines.postValue(Resource.Loading())
        try {
            // checks the device's internet connection
            if(internetConnection(this.getApplication()))
            {
                // call the getHeadlines() function of the newsRepository to get the headline news
                val response = newsRepository.getHeadlines(countryCode, headlinesPage)
                // update the value of the headlines LiveData with the result from handleHeadlinesResponse()
                headlines.postValue(handleHeadlinesResponse(response))
            }
            else
            {
                // if there is no internet connection
                // update the value of the headlines LiveData to Resource.Error()
                headlines.postValue(Resource.Error("No Internet Connection"))
            }
        }catch(t : Throwable)
        {
            // handle any exceptions that may occur
            when(t)
            {
                is IOException -> headlines.postValue(Resource.Error("Unable to connect"))
                else -> headlines.postValue(Resource.Error("No signal"))
            }
        }
    }


    // similar to headlinesInternet
    private suspend fun searchNewsInternet(searchQuery: String)
    {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication()))
            {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }
            else
            {
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch(t : Throwable)
        {
            when(t)
            {
                is IOException -> searchNews.postValue(Resource.Error("Unable to connect"))
                else -> searchNews.postValue(Resource.Error("No signal"))
            }
        }
    }

}