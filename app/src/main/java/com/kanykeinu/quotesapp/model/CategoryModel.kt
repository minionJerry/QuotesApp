package com.kanykeinu.quotesapp.model

class CategoryModel(var category:String? = null,
                         val subCategories: List<SubCategoryModel>? = null)