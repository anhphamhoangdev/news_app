package com.example.thenewsappmobile.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.thenewsappmobile.R
import com.example.thenewsappmobile.databinding.FragmentActicleBinding
import com.example.thenewsappmobile.ui.NewsActivity
import com.example.thenewsappmobile.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/


class ActicleFragment : Fragment(R.layout.fragment_acticle) {

    lateinit var newsViewModel: NewsViewModel

    // type ActicleFragmentArgs
    // property holds the arguments passed to the ActicleFragment.
    val args : ActicleFragmentArgs by navArgs()


    // used to access the views and elements defined in the fragment_acticle.xml layout file.
    lateinit var binding: FragmentActicleBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentActicleBinding.bind(view)


        // retrieves the newsViewModel instance from the NewsActivity and assigns it to the newsViewModel property of the ActicleFragment.
        newsViewModel = (activity as NewsActivity).newsViewModel

        val article = args.article


        // configures WebView in layout:
        binding.webView.apply {
            webViewClient = WebViewClient()
            // if the article.url is not null, it calls loadUrl(it) to load the article URL in the WebView
            article.url?.let {
                loadUrl(it)
            }
        }



        // sets an OnClickListener for fab view:
        binding.fab.setOnClickListener{
            newsViewModel.addToFavorite(article)
            Snackbar.make(view, "Added to favorites", Snackbar.LENGTH_SHORT).show()
        }

    }
}