package com.kanykeinu.quotesapp.database.dao

import android.arch.persistence.room.*
import com.kanykeinu.quotesapp.database.entity.Category
import io.reactivex.Flowable

@Dao
interface CategoryDao {
    @Query("SELECT * from category")
    fun getAll(): Flowable<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(child: Category) : Long

    @Delete
    fun delete(child: Category)

    @Update
    fun update(child: Category)

    @Query("Select * from category where id = :id")
    fun getById(id : Int) : Flowable<Category>


}
