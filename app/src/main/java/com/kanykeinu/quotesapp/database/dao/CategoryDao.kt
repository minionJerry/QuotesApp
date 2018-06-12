package com.kanykeinu.quotesapp.database.dao

import android.arch.persistence.room.*
import com.kanykeinu.quotesapp.database.entity.Category
import io.reactivex.Flowable

@Dao
interface CategoryDao {
    @Query("SELECT * from category")
    fun getAll(): Flowable<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category) : Long

    @Delete
    fun delete(category: Category)

    @Update
    fun update(category: Category)

    @Query("Select * from category where id = :id")
    fun getById(id : Int) : Flowable<Category>

    @Query("delete from category")
    fun deleteAll()
}
