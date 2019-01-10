package com.kanykeinu.quotesapp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.kanykeinu.quotesapp.QuotesApp.Companion.database
import com.kanykeinu.quotesapp.QuotesApp.Companion.sharedPreferences
import com.kanykeinu.quotesapp.R
import com.kanykeinu.quotesapp.adapter.CategoryAdapter
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.model.SelectableItem
import com.kanykeinu.quotesapp.extension.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_start.*

class CategoryActivity : AppCompatActivity() {

    private var categoryAdapter: CategoryAdapter? = null
    private var selectableCategories: MutableList<SelectableItem<Category>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar?.title = getString(R.string.choose_category)
        fetchCategories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ok, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.actionSave -> {
                if (getSelectedCategories().isEmpty())
                    showToast(getString(R.string.choose_at_least_one_category))
                else {
                    sharedPreferences.saveCategories(getSelectedCategories())
                    startActivity(Intent(this, SubCategoryActivity::class.java))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun fetchCategories(){
        val categories = database.categoryDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    list -> createSelectableCategoryList(list)
                },{
                    it -> showToast(it.localizedMessage)
                },{
                   showToast("bla")
                })
    }

    private fun createSelectableCategoryList(categories : List<Category>){
        for (category in categories){
            val selectableItem = SelectableItem<Category>(category,false)
            selectableCategories.add(selectableItem)
        }
        initCategoriesRecyclerView()
    }

    private fun initCategoriesRecyclerView(){
        categoryAdapter = CategoryAdapter(this, selectableCategories)
        val categoriesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        categoriesList.layoutManager = categoriesLayoutManager
        categoriesList.adapter = categoryAdapter
    }

    private fun getSelectedCategories(): MutableList<String> {
        val selectedCategoriesId: MutableList<String> = mutableListOf()
        for (selectableCategory in selectableCategories)
            if (selectableCategory.isSelected)
                selectedCategoriesId.add(selectableCategory.selectableItem.id.toString())
        return selectedCategoriesId
    }
}
