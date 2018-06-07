package com.kanykeinu.quotesapp.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "quote",
        foreignKeys = arrayOf(ForeignKey(
        entity = SubCategory::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("sub_category_id"),
        onDelete = ForeignKey.CASCADE)))
data class Quote (
        @PrimaryKey(autoGenerate = true)
        val id : Long,
        val author : String,
        val text : String,
        @ColumnInfo(name = "sub_category_id")
        val subCategoryId : Long
){
}