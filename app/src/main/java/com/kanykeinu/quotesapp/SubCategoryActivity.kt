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
import com.kanykeinu.quotesapp.adapter.onItemSelected
import com.kanykeinu.quotesapp.database.QuotesDatabase
import com.kanykeinu.quotesapp.database.entity.SubCategory
import kotlinx.android.synthetic.main.activity_sub_category.*
import java.util.*
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.*


class SubCategoryActivity : AppCompatActivity() {

    private var database: QuotesDatabase? = null
    private var categoiesIds: ArrayList<Parcelable>? = null
    private var subCategoryAdapter: SubCategoryAdapter? = null
    private val maps: HashMap<SubCategory, Boolean> = hashMapOf()
    private var formattedSubCategories = arrayListOf<SubCategory>()
    private val selectedSubCategoriesId: MutableList<String> = mutableListOf()
    private var sharedPreferences: SharedPreferencesManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        supportActionBar?.title = "Выберите подкатегорию"
        categoiesIds = intent.getParcelableArrayListExtra<Parcelable>("categories")
        database = QuotesDatabase.getInstance(this)
        sharedPreferences = SharedPreferencesManager(this)
        initGridView()
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
                for (map in maps)
                    if (map.value == true)
                        selectedSubCategoriesId.add(map.key.id.toString())
                sharedPreferences?.saveSubCategories(selectedSubCategoriesId)
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

        private fun fetchSubCategories() {
            for (id:Long in categoiesIds!! as ArrayList<Long>)
                database?.subCategoryDao()?.getSubCategoriesByCategory(id)
                        ?.subscribe({ subCat ->
                            formattedSubCategories.addAll(divideSubCategory(subCat))
                            initMap()
                            runOnUiThread({subCategoryAdapter?.notifyDataSetChanged()})
                        },{},{})
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
        val manager = GridLayoutManager(this, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (formattedSubCategories.get(position).subCategory!!.length > 12) 2 else 1
            }
        }



        subCategoryAdapter = SubCategoryAdapter(this,formattedSubCategories,object : onItemSelected{
            override fun itemPressed(obj: Any, view: View) {
                var isSelected = maps.get(obj)
                if (isSelected!!) {
                    maps.set(obj as SubCategory, false)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view.background = getDrawable(R.drawable.category_background)
                        (view as TextView).setTextColor(Color.WHITE)
                        view.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_add_white),null,null,null)
                    }
                } else {
                    maps.set(obj as SubCategory, true)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view.background = getDrawable(R.drawable.category_background_pressed)
                        (view as TextView).setTextColor(getColor(android.R.color.black))
                        view.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_done_black),null,null,null)
                    }
                }
            }

        })

        subCategoriesList.layoutManager = manager
        subCategoriesList.adapter = subCategoryAdapter
    }

    private fun initMap(){
        for (category in formattedSubCategories)
            maps.set(category, false)
    }


}
