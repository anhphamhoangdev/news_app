package com.example.thenewsappmobile.ui.fragments


import android.content.ClipData.Item
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thenewsappmobile.R
import com.example.thenewsappmobile.adapters.NewsAdapter
import com.example.thenewsappmobile.databinding.FragmentActicleBinding
import com.example.thenewsappmobile.databinding.FragmentFavoritesBinding
import com.example.thenewsappmobile.ui.NewsActivity
import com.example.thenewsappmobile.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar


/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/


// display all favorite articles in database
// user click on article -> navigate
// delete article from favorite when user swipe left
// user can undo delete
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentFavoritesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoritesBinding.bind(view)


        // gets the NewsViewModel instance from the parent NewsActivity
        newsViewModel = (activity as NewsActivity).newsViewModel
        // calls the setupFavoritesRecycler() function to set up the RecyclerView for displaying the favorite
        setupFavoritesRecycler()


        // When an item in the favorite RecyclerView is clicked,
        // this code creates a Bundle with the clicked article object
        // it then navigates to the ArticleFragment using the NavController,
        // passing the article object in the Bundle
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_favoritesFragment_to_acticleFragment, bundle)
        }


        // handle swipe and drag-and-drop gestures in a RecyclerView.
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


            // call when user starts dragging an article in the RecyclerView.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            // this method is called when the user swipes an article in the RecyclerView.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // removes the swiped article from the user's favorites list
                val article = newsAdapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view, "Removed from Favorites", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo")
                    {
                        // if the user taps the "Undo" action, the article is added back to the favorites list.
                        newsViewModel.addToFavorite(article)
                    }
                    show()
                }
            }
        }

        // creates an ItemTouchHelper instance
        // attaches it to the recyclerFavourites.
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }


        // sets up an observer on the user's favorite news articles,
        // which are retrieved from the newsViewModel.
        newsViewModel.getFavouriteNews().observe(viewLifecycleOwner, Observer {articles ->
            newsAdapter.differ.submitList(articles)

        })
    }

    private fun setupFavoritesRecycler()
    {
        newsAdapter = NewsAdapter()
        binding.recyclerFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}