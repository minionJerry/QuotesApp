package com.kanykeinu.quotesapp

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.kanykeinu.quotesapp.adapter.CategoryAdapter
import com.kanykeinu.quotesapp.adapter.onItemSelected
import kotlinx.android.synthetic.main.activity_main.*

class StartActivity : AppCompatActivity() {

    private var categoryAdapter: CategoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

//    fun initCategories(){
//        var maps : HashMap<String, Boolean> = hashMapOf()
//        for (category in categories)
//            maps.set(category,false)
//        categoryAdapter = CategoryAdapter(this,categories,false,object : onItemSelected {
//            override fun itemPressed(obj: Any, view: View) {
//                fetchQuote(obj as String)
//                var isSelected = maps.get(obj)
//                var view = view as TextView
//                if (isSelected!!) {
//                    maps.set(obj,false)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        view.background = getDrawable(R.drawable.category_background)
//                        view.setTextColor(Color.WHITE)
//                    }
//                }
//                else {
//                    maps.set(obj,true)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        view.background = getDrawable(R.drawable.category_background_pressed)
//                        view.setTextColor(getColor(R.color.colorPrimaryDark))
//                    }
//                }
//            }
//        })
//        val categoriesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        categoriesList.layoutManager = categoriesLayoutManager
//        categoriesList.adapter = categoryAdapter
//    }
}
