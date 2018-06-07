package com.kanykeinu.quotesapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.firebase.database.*
import com.kanykeinu.quotesapp.database.QuotesDatabase
import com.kanykeinu.quotesapp.database.entity.Quote
import com.kanykeinu.quotesapp.database.entity.SubCategory
import com.kanykeinu.quotesapp.model.CategoryModel
import com.kanykeinu.quotesapp.model.QuoteModel
import com.kanykeinu.quotesapp.model.SubCategoryModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer

class SplashActivity : Activity() {

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mQuotesDatabaseReference: DatabaseReference? = null
    private var mChildListener: ChildEventListener? = null
    private val categoryModels: MutableList<CategoryModel> = mutableListOf<CategoryModel>()

    private var database: QuotesDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        database = QuotesDatabase.getInstance(this)
        database?.categoryDao()?.getAll()
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(Consumer { categories ->
                            if (categories.size!=0) {
                                initFirebase()
                                attachDatabaseReadListener()
                            }else
                                startActivity(Intent(this,StartActivity::class.java))
                            finish()
                        })

    }

    override fun onStop() {
        super.onStop()
        detachDatabaseReadListener()
    }


    fun initFirebase(){
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mQuotesDatabaseReference = mFirebaseDatabase!!.getReference().child("quoteModels")
    }

    private fun attachDatabaseReadListener() {
        if (mChildListener == null) {
            mChildListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    val category = dataSnapshot.getValue<CategoryModel>(CategoryModel::class.java)
                    category?.category = removePreffix(category?.category!!)
                    addCategoryToLocalDb(category)
                    showToast(category.category + " " + getString(R.string.data_saved))
                    println("Previous Post ID: " + category)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

                override fun onChildMoved(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            mQuotesDatabaseReference?.addChildEventListener(mChildListener)
        }
    }

    private fun detachDatabaseReadListener() {
        if (mQuotesDatabaseReference != null) {
            mQuotesDatabaseReference?.removeEventListener(mChildListener)
            mChildListener = null
        }
    }

    private fun removePreffix(category: String) : String{
        if (category.contains(" ПРО "))
            return category.replaceBefore("ПРО","")
        else
            return category.replaceBefore(" О ","")
    }

    private fun addCategoryToLocalDb(categoryModel: CategoryModel){
        val categoryId  = database?.categoryDao()?.insert(com.kanykeinu.quotesapp.database.entity.Category(0,categoryModel.category))
        if (categoryId != null) {
            addSubCategoryToLocalDb(categoryModel.subCategoryModels,categoryId)
        }
    }

    private fun addSubCategoryToLocalDb(subCategoryModels: List<SubCategoryModel>?, categoryId : Long){
        if (subCategoryModels != null) {
            for (subCategory in subCategoryModels) {
                val subCategoryId = database?.subCategoryDao()?.insert(SubCategory(0, subCategory.subCategory, categoryId!!))
                if (subCategoryId != null) {
                    addQuotesToLocalDb(subCategory.quoteModels!!, subCategoryId)
                }
            }
        }
    }

    private fun addQuotesToLocalDb(quotes : List<QuoteModel>,subCategoryId : Long){
        for (quote in quotes){
            database?.quoteDao()?.insert(Quote(0, quote.author!!, quote.quote!!,subCategoryId))
        }

    }
}
