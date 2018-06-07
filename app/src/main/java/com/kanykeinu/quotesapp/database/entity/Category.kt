package com.kanykeinu.quotesapp.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable

@Entity(tableName = "category")
data class Category(
        @PrimaryKey(autoGenerate = true)
        val id : Long,
        val category : String?
)


