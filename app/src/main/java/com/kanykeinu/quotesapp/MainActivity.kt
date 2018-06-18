package com.kanykeinu.quotesapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kanykeinu.quotesapp.adapter.QuoteAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.quote_item_big.*
import android.view.animation.AnimationUtils
import com.kanykeinu.quotesapp.adapter.OnItemSelected
import com.kanykeinu.quotesapp.database.QuotesDatabase
import com.kanykeinu.quotesapp.database.entity.Quote
import com.kanykeinu.quotesapp.prefs.SharedPreferencesManager

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
        initMainQuote()
        initQuotesList()
        if (subCategoryIds != null) {
            fetchQuote(subCategoryIds)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.switch_category -> {
                startActivity(Intent(this,StartActivity::class.java))
                finish()
            }
            R.id.switch_subcategory -> {
                startActivity(Intent(this,SubCategoryActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchQuote(ids: MutableSet<String>){
        for (id in ids){
            val lId = id.toLong()
            database?.quoteDao()?.getQuotesBySubCategoryId(lId)
                    ?.subscribe({ quote ->
                        quotes.addAll(quote)
                        runOnUiThread({

                            quotesList.adapter?.notifyDataSetChanged()})
                    },{},{})
        }
    }

    private fun initQuotesList(){
        val quoteAdapter = QuoteAdapter(this, quotes,object : OnItemSelected{
            override fun itemPressed(obj: Quote) {
                sharedPreferences?.saveLastQuoteId(obj.id)
                bigQuoteText.text = obj.text
                bigQuoteAuthor.text = obj.author
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
        val id = sharedPreferences?.getSavedLastQuoteId()
        if (id != null){
            val savedQuote = database?.quoteDao()?.getById(id)
            if (savedQuote != null) {
                bigQuoteText.text = savedQuote.text
                bigQuoteAuthor.text = savedQuote.author
            }

        }
    }


}
