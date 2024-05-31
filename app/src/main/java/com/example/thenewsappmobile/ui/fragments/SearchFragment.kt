package com.example.thenewsappmobile.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thenewsappmobile.R
import com.example.thenewsappmobile.adapters.NewsAdapter
import com.example.thenewsappmobile.databinding.FragmentHeadlinesBinding
import com.example.thenewsappmobile.databinding.FragmentSearchBinding
import com.example.thenewsappmobile.ui.NewsActivity
import com.example.thenewsappmobile.ui.NewsViewModel
import com.example.thenewsappmobile.util.Constants
import com.example.thenewsappmobile.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/

class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemSearchError: CardView
    lateinit var binding: FragmentSearchBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)

        itemSearchError = view.findViewById(R.id.itemSearchError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_error, null)

        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        // gets the NewsViewModel instance from the parent NewsActivity
        newsViewModel = (activity as NewsActivity).newsViewModel
        // calls the setupSearchRecycler() function to set up the RecyclerView for displaying the result from search
        setupSearchRecycler()

        // When an item in the search RecyclerView is clicked,
        // this code creates a Bundle with the clicked article object
        // it then navigates to the ArticleFragment using the NavController,
        // passing the article object in the Bundle
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_acticleFragment, bundle)
        }



        var job: Job? = null
        binding.searchEdit.addTextChangedListener(){editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_NEWS_TIME_DELAY)
                editable?.let{
                    if(editable.toString().isNotEmpty())
                    {
                        newsViewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer {
                response ->
            when(response){
                // if response success
                is Resource.Success<*> -> {

                    hideProgressBar()

                    hideProgressBar()

                    // if data is not null
                    response.data?.let {newsResponse ->
                        // update data in the news adapter by submitting the list of articles from the newsResponse
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        // calculate totalPages, add 2 ( provide some buffer )
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.searchNewsPage == totalPages
                        if(isLastPage)
                        {
                            // reaches the last page, there's no need to display the "Load More" element
                            binding.recyclerSearch.setPadding(0,0,0,0)
                        }

                    }
                }
                is Resource.Error<*>->{
                    hideProgressBar()

                    response.message?.let {message ->
                        Toast.makeText(activity,"Sorry error: $message", Toast.LENGTH_SHORT).show()
                        showErrorMessage(message)
                    }
                }

                is Resource.Loading<*> -> {
                    showProgressBar()
                }
            }
        })

        retryButton.setOnClickListener{
            // try again to get search
            if(binding.searchEdit.text.toString().isNotEmpty())
            {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
            }
            else
            {
                hideErrorMessage()
            }

        }
    }


    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    // hide progress bar when loading is done
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false;
    }


    // show progress bar when loading
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true;
    }

    private fun hideErrorMessage() {
        itemSearchError.visibility = View.INVISIBLE
        isError = false
    }


    private fun showErrorMessage(message: String) {
        itemSearchError.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }


    // listener for the scroll events of the RecyclerView
    // used to detect when the user has scrolled to the bottom of the list
    // and trigger a request for more data to be loaded.
    var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            // get the position of the first visible news article in the list
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            // get the number of news articles currently visible in the list
            val visibleItemCount = layoutManager.childCount
            // get the total number of items in the list
            val totalItemCount = layoutManager.itemCount

            // check if there are no errors
            val isNoErrors = !isError
            // the app is not currently loading data,
            // and this is not the last page of data
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            // check if the user has scrolled to the bottom of the list
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            // check if the user is not at the beginning of the list
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            // check if the total number of items is greater than the page size
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            // determine if the app should trigger a request for more data
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            // if the app should trigger a request for more data, do so
            if (shouldPaginate) {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
                isScrolling = false
            }

        }

        // called whenever the scroll state of the RecyclerView changes
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            // if the user is currently scrolling (touch scroll state)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupSearchRecycler()
    {
        newsAdapter = NewsAdapter()
        binding.recyclerSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            // responsible for detecting when the user has scrolled to the bottom of the list and
            // triggering a request for more data.
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }
}