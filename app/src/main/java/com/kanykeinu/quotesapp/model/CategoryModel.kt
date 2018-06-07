package com.kanykeinu.quotesapp.model

data class CategoryModel(var category:String? = null,
                         val subCategoryModels: List<SubCategoryModel>? = null) {

}