package com.example.thenewsappmobile.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.thenewsappmobile.R
import com.example.thenewsappmobile.databinding.ActivityNewsBinding
import com.example.thenewsappmobile.db.ArticlesDatabase
import com.example.thenewsappmobile.repository.NewsRepository

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/

class NewsActivity : AppCompatActivity() {


    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // initialize a NewsRepository object by passing in an ArticlesDatabase object
        val newsRepository = NewsRepository(ArticlesDatabase(this))

        // initialize a NewsViewModelProviderFactory object by passing in the application and newsRepository
        val viewModelProviderFactory = NewsViewModelProviderFactory(this.application, newsRepository)

        // Initialize a NewsViewModel object by using ViewModelProvider and viewModelProviderFactory
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)


        // get the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment

        // get the NavController
        val navController = navHostFragment.navController

        // set up the BottomNavigationView with the NavController
        binding.bottomNavigationView.setupWithNavController(navController)


    }
}