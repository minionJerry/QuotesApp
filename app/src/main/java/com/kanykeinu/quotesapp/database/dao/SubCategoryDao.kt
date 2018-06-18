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
    fun insert(subCategory: SubCategory) : Long

    @Delete
    fun delete(subCategory: SubCategory)

    @Update
    fun update(subCategory: SubCategory)

    @Query("Select * from sub_category where id = :id")
    fun getById(id : Int) : Flowable<SubCategory>

    @Query("Select * from sub_category where category_id = :categoryId ")
    fun getSubCategoriesByCategory(categoryId  : Long) : List<SubCategory>

}
