package com.kanykeinu.quotesapp.prefs

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract
import com.kanykeinu.quotesapp.Constants.Companion.CATEGORIES
import com.kanykeinu.quotesapp.Constants.Companion.IS_FIRST_ENTRY
import com.kanykeinu.quotesapp.Constants.Companion.QUOTE
import com.kanykeinu.quotesapp.Constants.Companion.SUB_CATEGORIES

class SharedPreferencesManager(context: Context) {

    private var sharedpreferences: SharedPreferences? = null
    private var editor : SharedPreferences.Editor? = null

    init {
        this.sharedpreferences = context.getSharedPreferences(ContactsContract.Settings.DATA_SET, Context.MODE_PRIVATE)
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
        editor?.putLong(QUOTE,id)
        editor?.apply()
    }

    fun getSavedLastQuoteId(): Long? {
        return sharedpreferences?.getLong(QUOTE,0)
    }

    fun saveCategories(categoriesId: MutableList<String>) {
        val set = HashSet<String>()
        set.addAll(categoriesId)
        editor?.putStringSet(CATEGORIES, set)
        editor?.apply()
    }

    fun getCategories() : MutableSet<String>? {
        val set = sharedpreferences?.getStringSet(CATEGORIES, null)
        return set
    }

    fun saveEntryStatus(status : Boolean){
        editor?.putBoolean(IS_FIRST_ENTRY,status)
        editor?.apply()
    }

    fun getEntryStatus() : Boolean{
        return sharedpreferences?.getBoolean(IS_FIRST_ENTRY,false)!!
    }
}