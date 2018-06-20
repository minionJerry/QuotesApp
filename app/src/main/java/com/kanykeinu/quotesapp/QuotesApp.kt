package com.kanykeinu.quotesapp

import android.app.Application
import com.kanykeinu.quotesapp.database.QuotesDatabase
import com.kanykeinu.quotesapp.prefs.SharedPreferencesManager

class QuotesApp : Application() {
    companion object {
        lateinit var database: QuotesDatabase
        lateinit var sharedPreferences : SharedPreferencesManager
    }

    override fun onCreate() {
        super.onCreate()
        QuotesApp.database = QuotesDatabase.getInstance(this)!!
        QuotesApp.sharedPreferences = SharedPreferencesManager(this)
    }
}