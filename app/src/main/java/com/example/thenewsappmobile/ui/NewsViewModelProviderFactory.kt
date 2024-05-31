package com.example.thenewsappmobile.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.thenewsappmobile.repository.NewsRepository

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/




// creating instances of the NewsViewModel class
class NewsViewModelProviderFactory(val app: Application, val newsRepository: NewsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}