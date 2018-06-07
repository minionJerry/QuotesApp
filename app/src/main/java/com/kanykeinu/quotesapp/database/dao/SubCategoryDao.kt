package com.kanykeinu.quotesapp.database.dao

import android.arch.persistence.room.*
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.database.entity.SubCategory
import io.reactivex.Flowable

@Dao
interface SubCategoryDao {
    @Query("SELECT * from sub_category")
    fun getAll(): Flowable<List<SubCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(child: SubCategory) : Long

    @Delete
    fun delete(child: SubCategory)

    @Update
    fun update(child: SubCategory)

    @Query("Select * from sub_category where id = :id")
    fun getById(id : Int) : Flowable<SubCategory>

}
