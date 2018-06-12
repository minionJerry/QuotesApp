package com.kanykeinu.quotesapp

import android.content.Context
import android.content.SharedPreferences
import android.provider.Contacts.SettingsColumns.KEY

class SharedPreferencesManager {

    private var sharedpreferences: SharedPreferences? = null
    val KEY = "QUOTES"
    val SUB_CATEGORIES = "SUB_CATEGORIES"
    val SUB_CATEGORY_ID = "SUB_CATEGORY_ID"
    private var editor : SharedPreferences.Editor? = null

    constructor(context: Context) {
        this.sharedpreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
        editor = sharedpreferences?.edit()
    }

    fun saveSubCategories(subCategoriesId: MutableList<String>) {
        val set = HashSet<String>()
        set.addAll(subCategoriesId)
        editor?.putStringSet(SUB_CATEGORIES, set)
        editor?.apply()
    }

    fun getSubCategories() : MutableSet<String>? {
        val set = sharedpreferences?.getStringSet(SUB_CATEGORIES, null)
        return set
    }

    fun saveLastQuoteId(id : Long){
        editor?.putLong(SUB_CATEGORY_ID,id)
    }

    fun getSavedLastQuoteId(): Long? {
        return sharedpreferences?.getLong(SUB_CATEGORY_ID,0)
    }
}