package com.kanykeinu.quotesapp.model

class SubCategoryModel(val subCategory: String?= null, val quotes : List<QuoteModel>? = null){

    override fun toString(): String {
        return "$subCategory : $quotes"
    }
}