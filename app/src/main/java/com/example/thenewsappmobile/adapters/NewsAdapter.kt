package com.example.thenewsappmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thenewsappmobile.R
import com.example.thenewsappmobile.models.Article

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/




class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)


    // lateinit variables that hold references to the views in the item_news layout
    // used to display the news article data.
    lateinit var articleImage: ImageView
    lateinit var articleSource: TextView
    lateinit var articleTitle: TextView
    lateinit var articleDescription: TextView
    lateinit var articleDateTime: TextView


    // using differCallBack to determine the changes between the old and new lists.
    // allows AsyncListDiffer to only update the changed parts instead of having to rebuild the entire list
    private val differCallBack = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)


    // create a new ArticleViewHolder
    // inflates the item_news layout and returns a new ArticleViewHolder instance.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )
    }



    // returns the number of news articles in the current list.
    // determine the number of news articles to be displayed in the RecyclerView.
    override fun getItemCount(): Int {
        // a reference to the current list of news being managed by the AsyncListDiffer instance
        return differ.currentList.size
    }

    // initialized as null => no listener has been set
    private var onItemClickListener: ((Article) -> Unit)? = null


    // setting a listener for the click event on each news article.
    fun setOnItemClickListener(listener : (Article)-> Unit){
        onItemClickListener = listener
    }


    // binding the data from the article object to the corresponding views in the ArticleViewHolder
    // set an OnClickListener on the itemView to allow handling click events on each news article.
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        // retrieve the article data at the current position from the differ.currentList
        var article = differ.currentList[position]

        // find the corresponding views in the ArticleViewHolder using their IDs
        articleImage = holder.itemView.findViewById(R.id.articleImage)
        articleSource = holder.itemView.findViewById(R.id.articleSource)
        articleTitle = holder.itemView.findViewById(R.id.articleTitle)
        articleDescription = holder.itemView.findViewById(R.id.articleDescription)
        articleDateTime = holder.itemView.findViewById(R.id.articleDateTime)


        // populate the views with the data from the article object.
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).placeholder(R.drawable.img_3).into(articleImage)
            articleSource.text = article.source?.name
            articleTitle.text = article.title
            articleDescription.text = article.description
            articleDateTime.text = article.publishedAt

            // set an OnClickListener on the itemView of the ArticleViewHolder.
            setOnClickListener{
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }




}