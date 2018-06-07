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
    fun insert(child: Quote)

    @Delete
    fun delete(child: Quote)

    @Update
    fun update(child: Quote)

    @Query("Select * from quote where id = :id")
    fun getById(id : Int) : Flowable<Quote>

}
