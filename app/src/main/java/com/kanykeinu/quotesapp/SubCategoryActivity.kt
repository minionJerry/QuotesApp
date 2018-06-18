package com.kanykeinu.quotesapp

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import com.kanykeinu.quotesapp.adapter.SubCategoryAdapter
import com.kanykeinu.quotesapp.database.QuotesDatabase
import com.kanykeinu.quotesapp.database.entity.SubCategory
import kotlinx.android.synthetic.main.activity_sub_category.*
import java.util.*
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.kanykeinu.quotesapp.Constants.Companion.CATEGORIES
import com.kanykeinu.quotesapp.model.SelectableItem
import com.kanykeinu.quotesapp.prefs.SharedPreferencesManager


class SubCategoryActivity : AppCompatActivity() {

    private var database: QuotesDatabase? = null
    private var categoiesIds: MutableSet<String>? = mutableSetOf()
    private var subCategoryAdapter: SubCategoryAdapter? = null
    private var formattedSubCategories = arrayListOf<SubCategory>()

    private var sharedPreferences: SharedPreferencesManager? = null
    private val subCategoriesMap = mutableListOf<SelectableItem<SubCategory>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        sharedPreferences = SharedPreferencesManager(this)
        supportActionBar?.title = getString(R.string.choose_subcategory)
        categoiesIds = sharedPreferences?.getCategories()
        database = QuotesDatabase.getInstance(this)
        if (categoiesIds!= null)
            fetchSubCategories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ok, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.actionSave -> {
                if (getSelectedSubCategories().isEmpty())
                    showToast("Выберите хотя бы одну подкатегорию")
                else {
                    sharedPreferences?.saveSubCategories(getSelectedSubCategories())
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

        private fun fetchSubCategories() {
            for (id in categoiesIds!!) {
                var lId = id.toLong()
                var subCat = database?.subCategoryDao()?.getSubCategoriesByCategory(lId)
                formattedSubCategories.addAll(divideSubCategory(subCat!!))
            }
            initGridView()
    }

    private fun divideSubCategory(subCategories : List<SubCategory>) : List<SubCategory>{
        var copy = subCategories
        var i = 0
        for(subCategory in subCategories){
            var title = subCategory.subCategory?.split(",",":")
            copy[i].subCategory = title?.get(0)
            Log.e("Tag",title?.get(0))
            i++
        }
        return copy
    }

    private fun initGridView(){
        initMap()
        val manager = GridLayoutManager(this, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (formattedSubCategories.get(position).subCategory!!.length > 10) 2 else 1
            }
        }
        subCategoryAdapter = SubCategoryAdapter(this,subCategoriesMap)
        subCategoriesList.layoutManager = manager
        subCategoriesList.adapter = subCategoryAdapter
    }

    private fun initMap(){
        for (subcategory in formattedSubCategories) {
            val selectableItem = SelectableItem<SubCategory>(subcategory,false)
            subCategoriesMap.add(selectableItem)
        }
    }

    private fun getSelectedSubCategories(): MutableList<String> {
        val selectedSubCategoriesId: MutableList<String> = mutableListOf()
        for (selectedSubCategory in subCategoriesMap)
            if (selectedSubCategory.isSelected)
                selectedSubCategoriesId.add(selectedSubCategory.selectableItem.id.toString())
        return selectedSubCategoriesId
    }
}
