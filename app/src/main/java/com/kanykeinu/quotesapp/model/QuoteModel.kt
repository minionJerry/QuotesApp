package com.kanykeinu.quotesapp.model

/**
 * Created by KanykeiNu on 17.05.2018.
 */
class QuoteModel(val quote : String? = null, val author : String? = null){
    override fun toString(): String {
        return "$author - <$quote>"
    }
}
