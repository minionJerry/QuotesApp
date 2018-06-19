package com.kanykeinu.quotesapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.database.*
import com.kanykeinu.quotesapp.database.QuotesDatabase
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.database.entity.Quote
import com.kanykeinu.quotesapp.database.entity.SubCategory
import com.kanykeinu.quotesapp.model.CategoryModel
import com.kanykeinu.quotesapp.model.QuoteModel
import com.kanykeinu.quotesapp.model.SubCategoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class SplashActivity : Activity() {

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mQuotesDatabaseReference: DatabaseReference? = null
    private var mChildListener: ChildEventListener? = null
    private var mEventListener: ValueEventListener? = null
    private var database: QuotesDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        database = QuotesDatabase.getInstance(this)
        var quotes = database?.quoteDao()?.getAll()
        checkQuotesSize(quotes)
    }


    override fun onStop() {
        super.onStop()
        detachDatabaseReadListener()
    }

    private fun checkQuotesSize(quotes: List<Quote>?){
        if (quotes?.size == 0) {
            initFirebase()
            attachDatabaseReadListener()
        }else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun initFirebase(){
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mQuotesDatabaseReference = mFirebaseDatabase!!.getReference().child("quotes")
    }

    private fun attachDatabaseReadListener() {
        if (mChildListener == null && mEventListener==null) {
            mChildListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    val category = dataSnapshot.getValue<CategoryModel>(CategoryModel::class.java)
                    category?.category = removePreffix(category?.category!!)
                    addCategoryToLocalDb(category)
                    println("Previous Post ID: " + category)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

                override fun onChildMoved(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

                override fun onCancelled(databaseError: DatabaseError) {}
            }

            mEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {}

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    println("We're done loading the initial " + dataSnapshot.childrenCount + " items")
                    startActivity(Intent(this@SplashActivity, StartActivity::class.java))
                    finish()
                }
            }

            mQuotesDatabaseReference?.addListenerForSingleValueEvent(mEventListener)
            mQuotesDatabaseReference?.addChildEventListener(mChildListener)
        }
    }

    private fun detachDatabaseReadListener() {
        if (mQuotesDatabaseReference != null) {
            mQuotesDatabaseReference?.removeEventListener(mChildListener)
            mQuotesDatabaseReference?.removeEventListener(mEventListener)
            mChildListener = null
            mEventListener = null
        }
    }

    private fun removePreffix(category: String) : String{
        if (category.contains(" ПРО "))
            return category.replaceBefore("ПРО","")
        else
            return category.replaceBefore(" О ","")
    }

    private fun addCategoryToLocalDb(categoryModel: CategoryModel){
        val categoryId  = database?.categoryDao()?.insert(Category(0,categoryModel.category))
        if (categoryId != null) {
            addSubCategoryToLocalDb(categoryModel.subCategories,categoryId)
        }
    }

    private fun addSubCategoryToLocalDb(subCategoryModels: List<SubCategoryModel>?, categoryId : Long){
        if (subCategoryModels != null) {
            for (subCategory in subCategoryModels) {
                val subCategoryId = database?.subCategoryDao()?.insert(SubCategory(0, subCategory.subCategory, categoryId))
                if (subCategoryId != null) {
                    addQuotesToLocalDb(subCategory.quotes, subCategoryId)
                }
            }
        }
    }

    private fun addQuotesToLocalDb(quotes: List<QuoteModel>?, subCategoryId: Long){
        if (quotes != null) {
            for (quote in quotes){
                database?.quoteDao()?.insert(Quote(0, quote.author!!, quote.quote!!,subCategoryId))
            }
        }
    }
}
