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
import com.kanykeinu.quotesapp.model.Quote
import com.kanykeinu.quotesapp.network.PaperQuotesServiceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.quote_item_big.*
import android.view.animation.AnimationUtils


class MainActivity : AppCompatActivity() {
    private val API_KEY : String ="Token e2eeb1aa9f32eb07fa04595a0c457ecb6fadb772"
    private val QUOTES_PER_REQUEST: Int = 5
    private var quotes : List<Quote>? = null

    val geeks = listOf("life", "random", "love", "motivation", "happiness", "productivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCategories()

    }

    fun initCategories(){
        var maps : HashMap<String, Boolean> = hashMapOf()
        for (geek in geeks)
            maps.set(geek,false)
        val categoryAdapter = CategoryAdapter(this,geeks,object : onItemSelected{
            override fun itemPressed(obj: Any, view: View) {
                fetchQuote(obj as String)
                var isSelected = maps.get(obj)
                var view = view as TextView
                if (isSelected!!) {
                    maps.set(obj,false)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view.background = getDrawable(R.drawable.category_background)
                        view.setTextColor(Color.WHITE)
                    }
                }
                else {
                    maps.set(obj,true)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view.background = getDrawable(R.drawable.category_background_pressed)
                        view.setTextColor(getColor(R.color.colorPrimaryDark))
                    }
                }
            }
        })
        val categoriesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoriesList.layoutManager = categoriesLayoutManager
        categoriesList.adapter = categoryAdapter
    }

    fun fetchQuote(tag: String){
        val apiService = PaperQuotesServiceApi.create()
        apiService.getQuote(API_KEY,tag,QUOTES_PER_REQUEST)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    result ->
                    Log.d("Result",result.results.toString())
                    quotes = result.results
                    initQuotesList()
                },
                { error ->
                    Log.e("Ooops!",error.message)
                })
    }

    fun initQuotesList(){
        val quoteAdapter = QuoteAdapter(this, quotes!!,object : onItemSelected{
            override fun itemPressed(obj: Any, view: View) {
                var obj = obj as Quote
                bigQuoteText.text = obj.quote
                bigQuoteAuthor.text = obj.author
                Log.d("Quote", "selected is" + obj.quote)
//                    val animation = ObjectAnimator.ofFloat(view, "translationY", 100f)
////                    animation.setInterpolator(pathInterpolator);
//                    animation.duration = 2000
//                    animation.start()
            }
        })

        val quotesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        quotesList.layoutManager = quotesLayoutManager
        quotesList.adapter = quoteAdapter
        // run animation
        val  controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_quote_anim);

        quotesList.setLayoutAnimation(controller);
        quotesList.getAdapter().notifyDataSetChanged();
        quotesList.scheduleLayoutAnimation();

    }
}
