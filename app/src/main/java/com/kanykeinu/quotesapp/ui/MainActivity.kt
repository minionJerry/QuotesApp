package com.kanykeinu.quotesapp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.kanykeinu.quotesapp.adapter.QuoteAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.quote_item_big.*
import com.kanykeinu.quotesapp.QuotesApp.Companion.database
import com.kanykeinu.quotesapp.QuotesApp.Companion.sharedPreferences
import com.kanykeinu.quotesapp.R
import com.kanykeinu.quotesapp.adapter.OnItemSelected
import com.kanykeinu.quotesapp.database.entity.Quote
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {

    private val quotes: MutableList<Quote> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val subCategoryIds = sharedPreferences.getSubCategories()
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
                startActivity(Intent(this, CategoryActivity::class.java))
                finish()
            }
            R.id.switch_subcategory -> {
                startActivity(Intent(this, SubCategoryActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchQuote(ids: MutableSet<String>){
        for (id in ids){
            val lId = id.toLong()
            database.quoteDao().getQuotesBySubCategoryId(lId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe{ quote ->
                            quotes.addAll(quote)
                            quotesList.adapter?.notifyDataSetChanged()
                    }
        }
    }

    private fun initQuotesList(){
        val quoteAdapter = QuoteAdapter(this, quotes,object : OnItemSelected{
            override fun itemPressed(obj: Quote) {
                bigQuoteText.text = obj.text
                bigQuoteAuthor.text = obj.author
                sharedPreferences.saveLastQuoteId(obj.id)
            }

        })
        val quotesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        quotesList.layoutManager = quotesLayoutManager
        quotesList.adapter = quoteAdapter
    }

    private fun initMainQuote(){
        val id = sharedPreferences.getSavedLastQuoteId()
        if (id!= null && id.toInt() != 0){
            database.quoteDao().getById(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe{ savedQuote ->
                        bigQuoteText.text = savedQuote.text
                        bigQuoteAuthor.text = savedQuote.author
                    }

        }
    }


}
