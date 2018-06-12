package com.kanykeinu.quotesapp.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "sub_category",
        foreignKeys = arrayOf(ForeignKey(
                entity = Category::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("category_id"),
                onDelete = ForeignKey.CASCADE)))
data class SubCategory(
        @PrimaryKey(autoGenerate = true)
        val id : Long,
        @ColumnInfo(name = "sub_category")
        var subCategory : String?,
        @ColumnInfo(name = "category_id")
        val categoryId : Long
)