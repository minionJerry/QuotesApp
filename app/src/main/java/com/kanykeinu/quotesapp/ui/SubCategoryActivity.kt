package com.kanykeinu.quotesapp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kanykeinu.quotesapp.adapter.SubCategoryAdapter
import com.kanykeinu.quotesapp.database.entity.SubCategory
import kotlinx.android.synthetic.main.activity_sub_category.*
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.kanykeinu.quotesapp.QuotesApp.Companion.database
import com.kanykeinu.quotesapp.QuotesApp.Companion.sharedPreferences
import com.kanykeinu.quotesapp.R
import com.kanykeinu.quotesapp.model.SelectableItem
import com.kanykeinu.quotesapp.extension.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SubCategoryActivity : AppCompatActivity() {

    private var categoiesIds: MutableSet<String>? = mutableSetOf()
    private var subCategoryAdapter: SubCategoryAdapter? = null
    private var formattedSubCategories = arrayListOf<SubCategory>()
    private val subCategoriesMap = mutableListOf<SelectableItem<SubCategory>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        supportActionBar?.title = getString(R.string.choose_subcategory)
        categoiesIds = sharedPreferences.getCategories()
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
                    showToast(getString(R.string.choose_at_least_subcategory))
                else {
                    sharedPreferences.saveSubCategories(getSelectedSubCategories())
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

        private fun fetchSubCategories() {
            for (id in categoiesIds!!) {
                val lId = id.toLong()
                val subCat = database.subCategoryDao().getSubCategoriesByCategory(lId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ subCategories ->
                            formattedSubCategories.addAll(divideSubCategory(subCategories))
                            initGridView()
                        },{
                            error -> showToast(error.localizedMessage)
                        },{
                            showToast("bla")
                        })
            }

    }

    private fun divideSubCategory(subCategories : List<SubCategory>) : List<SubCategory>{
        val copy = subCategories
        var i = 0
        for(subCategory in subCategories){
            val title = subCategory.subCategory?.split(",",":")
            copy[i].subCategory = title?.get(0)
            i++
        }
        return copy
    }

    private fun initGridView(){
        initMap()
        subCategoryAdapter = SubCategoryAdapter(this,subCategoriesMap)
        val manager = GridLayoutManager(this, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (subCategoryAdapter!!.getItemViewType(position)){
                    subCategoryAdapter!!.TYPE_BIG_SUBCATEGORY -> return 2
                    subCategoryAdapter!!.TYPE_REGULAR_SUBCATEGORY ->  return 1
                }
                return 1
            }
        }
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
