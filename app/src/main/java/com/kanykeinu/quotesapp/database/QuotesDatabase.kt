package com.kanykeinu.quotesapp.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.kanykeinu.quotesapp.database.dao.CategoryDao
import com.kanykeinu.quotesapp.database.dao.QuoteDao
import com.kanykeinu.quotesapp.database.dao.SubCategoryDao
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.database.entity.Quote
import com.kanykeinu.quotesapp.database.entity.SubCategory
import com.kanykeinu.quotesapp.model.CategoryModel
import com.kanykeinu.quotesapp.model.QuoteModel
import com.kanykeinu.quotesapp.model.SubCategoryModel

@Database(entities = arrayOf(Category::class, SubCategory::class, Quote::class), version = 2)
abstract class QuotesDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun quoteDao(): QuoteDao

    companion object {
        private var databseInstance: QuotesDatabase? = null

        fun getInstance(context: Context): QuotesDatabase? {
            if (databseInstance == null) {
                synchronized(QuotesDatabase::class.java) {
                    if (databseInstance == null) {
                        databseInstance = Room.databaseBuilder(context.getApplicationContext(),
                                QuotesDatabase::class.java, "Quotes.db")
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return databseInstance
        }

        fun clearDatabase(){
            databseInstance = null
        }
    }
}