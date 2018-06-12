package com.kanykeinu.quotesapp

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kanykeinu.quotesapp.adapter.CategoryAdapter
import com.kanykeinu.quotesapp.adapter.onItemSelected
import com.kanykeinu.quotesapp.database.QuotesDatabase
import com.kanykeinu.quotesapp.database.entity.Category
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_start.*
import java.util.ArrayList

class StartActivity : AppCompatActivity() {

    private var categoryAdapter: CategoryAdapter? = null

    private var database: QuotesDatabase? = null

    private val maps: HashMap<Category, Boolean> = hashMapOf()

    private val selectedCategoriesId: MutableList<Long> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar?.title = "Выберите категорию"
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
                for (map in maps)
                    if (map.value == true)
                        selectedCategoriesId.add(map.key.id)
                startActivity(Intent(this,SubCategoryActivity::class.java).putParcelableArrayListExtra("categories",selectedCategoriesId as ArrayList<Parcelable>))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initCategories(categories : MutableList<Category>){
            for (category in categories)
                maps.set(category, false)
            categoryAdapter = CategoryAdapter(this, categories, object : onItemSelected {
                override fun itemPressed(obj: Any, view: View) {
                    var isSelected = maps.get(obj)
                    if (isSelected!!) {
                        maps.set(obj as Category, false)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            view.background = getDrawable(R.drawable.category_background)
                            ((view as ViewGroup).getChildAt(0) as TextView).setTextColor(Color.WHITE)
                        }
                    } else {
                        maps.set(obj as Category, true)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            view.background = getDrawable(R.drawable.category_background_pressed)
                            ((view as ViewGroup).getChildAt(0) as TextView).setTextColor(getColor(R.color.colorPrimaryDark))
                        }
                    }
                }
            })
            val categoriesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            categoriesList.layoutManager = categoriesLayoutManager
            categoriesList.adapter = categoryAdapter
    }




    private fun fetchCategories(){
        database?.categoryDao()?.getAll()
                ?.subscribe({ cat ->
                    initCategories(cat as MutableList<Category>)
                })

    }
}
