package com.kanykeinu.quotesapp.model

/**
 * Created by KanykeiNu on 17.05.2018.
 */
data class Quote(val quote : String,val author : String,val tags : ArrayList<String>,val image : String)

data class Response(val next : String, val previous : String, val results : List<Quote>)