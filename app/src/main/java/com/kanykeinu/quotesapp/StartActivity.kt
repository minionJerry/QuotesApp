package com.kanykeinu.quotesapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.kanykeinu.quotesapp.adapter.CategoryAdapter
import com.kanykeinu.quotesapp.database.QuotesDatabase
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.model.SelectableItem
import com.kanykeinu.quotesapp.prefs.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {


    private var categoryAdapter: CategoryAdapter? = null
    private var database: QuotesDatabase? = null
    private val maps: HashMap<Category, Boolean> = hashMapOf()
    private var sharedPreferences: SharedPreferencesManager? = null
    private var selectableCategories: MutableList<SelectableItem<Category>> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar?.title = getString(R.string.choose_category)
        sharedPreferences = SharedPreferencesManager(this)
        database = QuotesDatabase.getInstance(this)
        fetchCategories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ok, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.actionSave -> {
                sharedPreferences?.saveCategories(getSelectedCategories())
                startActivity(Intent(this,SubCategoryActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun fetchCategories(){
        val categories = database?.categoryDao()?.getAll()
        if (categories!=null)
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
