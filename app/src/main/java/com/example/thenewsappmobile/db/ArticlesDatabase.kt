package com.example.thenewsappmobile.db

import android.content.Context
import androidx.navigation.ui.AppBarConfiguration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.thenewsappmobile.models.Article

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/


@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticlesDatabase : RoomDatabase()
{

    // abstract method to return an instance of ArticleDAO to interact with the Article table in the database.
    abstract fun getArticleDao() : ArticleDAO

    companion object{
        @Volatile
        private var instance: ArticlesDatabase? = null
        private val LOCK = Any()


        // operator overloading function for the invoke operator to create an instance of ArticlesDatabase
        // If instance already exists, returns instance.
        // Otherwise, create a new instance by calling createDatabase(context) within the synchronized
        // block to ensure thread-safety.
        // ( ensure only one thread can create database instance at a time )
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{
                instance = it
            }
        }

        // creates a new instance of ArticlesDatabase using Room.databaseBuilder.
        // Build the database name "article_db.db" and the ArticlesDatabase class.
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticlesDatabase::class.java,
                name = "article_db.db"
            ).build()
    }

}