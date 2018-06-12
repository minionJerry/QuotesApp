package com.kanykeinu.quotesapp.database.dao

import android.arch.persistence.room.*
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.database.entity.Quote
import io.reactivex.Flowable

@Dao
interface QuoteDao {
    @Query("SELECT * from quote")
    fun getAll(): Flowable<List<Quote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(quote: Quote) : Long

    @Delete
    fun delete(quote: Quote)

    @Update
    fun update(quote: Quote)

    @Query("Select * from quote where id = :id")
    fun getById(id : Long) : Flowable<Quote>

    @Query("Select * from quote where sub_category_id = :id")
    fun getQuotesBySubCategoryId(id : Long) : Flowable<List<Quote>>

}
