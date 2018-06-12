package com.kanykeinu.quotesapp

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.TextView
import com.kanykeinu.quotesapp.adapter.CategoryAdapter
import com.kanykeinu.quotesapp.adapter.QuoteAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.kanykeinu.quotesapp.adapter.onItemSelected
import com.kanykeinu.quotesapp.model.QuoteModel
import kotlinx.android.synthetic.main.quote_item_big.*
import android.view.animation.AnimationUtils
import com.google.firebase.database.*
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ChildEventListener
import com.kanykeinu.quotesapp.R.id.*
import com.kanykeinu.quotesapp.database.QuotesDatabase
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.database.entity.Quote

class MainActivity : AppCompatActivity() {

    private val quotes: MutableList<Quote> = mutableListOf()
    private var sharedPreferences: SharedPreferencesManager? = null
    private var database: QuotesDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = SharedPreferencesManager(this)
        database = QuotesDatabase.getInstance(this)
        val subCategoryIds = sharedPreferences?.getSubCategories()
        initQuotesList()
        if (subCategoryIds != null) {
            fetchQuote(subCategoryIds)
        }
    }

    private fun fetchQuote(ids: MutableSet<String>){
            for (id in ids){
                val lId = id.toLong()
                database?.quoteDao()?.getQuotesBySubCategoryId(lId)
                        ?.subscribe({ quote ->
                            quotes.addAll(quote)
                            runOnUiThread({
                                initMainQuote()
                                quotesList.adapter?.notifyDataSetChanged()})
                        },{},{})
            }
    }

    private fun initQuotesList(){
        val quoteAdapter = QuoteAdapter(this, quotes ,object : onItemSelected{
            override fun itemPressed(obj: Any, view: View) {
                var obj = obj as Quote
                bigQuoteText.text = obj.text
                bigQuoteAuthor.text = obj.author
                Log.d("QuoteModel", "selected is" + obj.text)
                sharedPreferences?.saveLastQuoteId(obj.id)
            }
        })

        val quotesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        quotesList.layoutManager = quotesLayoutManager
        quotesList.adapter = quoteAdapter
        // run animation
        val  controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_quote_anim);
        quotesList.setLayoutAnimation(controller);
        quoteAdapter.notifyDataSetChanged();
        quotesList.scheduleLayoutAnimation();

    }

    private fun initMainQuote(){
        if (sharedPreferences?.getSavedLastQuoteId() != null){
            bigQuoteText.text = quotes?.get(0)?.text
            bigQuoteAuthor.text = quotes?.get(0)?.author
        }
    }


}
