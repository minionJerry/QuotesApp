package com.kanykeinu.quotesapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.*
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.database.entity.Quote
import com.kanykeinu.quotesapp.database.entity.SubCategory
import com.kanykeinu.quotesapp.model.CategoryModel
import com.kanykeinu.quotesapp.model.QuoteModel
import com.kanykeinu.quotesapp.model.SubCategoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.kanykeinu.quotesapp.QuotesApp
import com.kanykeinu.quotesapp.QuotesApp.Companion.database
import com.kanykeinu.quotesapp.R
import com.kanykeinu.quotesapp.extension.removeCategoryPrefix
import com.kanykeinu.quotesapp.extension.showToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : Activity() {

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mQuotesDatabaseReference: DatabaseReference? = null
    private var mEventListener: ValueEventListener? = null
    private var mChildListener: ChildEventListener? = null
    private val dataFromFirebase = ArrayList<CategoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkQuotesSize()
    }

    private fun checkQuotesSize(){
        Log.d("entryStatus--> ",QuotesApp.sharedPreferences.getEntryStatus().toString())
        if (!QuotesApp.sharedPreferences.getEntryStatus()) {
            progressBar.visibility = View.VISIBLE
            tvloadText.visibility = View.VISIBLE
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
        if (mChildListener==null && mEventListener==null) {
            mChildListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    val category = dataSnapshot.getValue<CategoryModel>(CategoryModel::class.java)
                    category?.category?.removeCategoryPrefix()
                    dataFromFirebase.add(category!!)
                    Log.d("Category from Firebase:", category.category)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

                override fun onChildMoved(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

                override fun onCancelled(databaseError: DatabaseError) {}
            }

            mEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {}

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d("Finished loading data", dataSnapshot.childrenCount.toString())
                    saveDataToRoomDb()

                    // start new screen after saving all data to local db
                    startActivity(Intent(this@SplashActivity, CategoryActivity::class.java))
                    finish()
                }
            }
            mQuotesDatabaseReference?.addChildEventListener(mChildListener)
            mQuotesDatabaseReference?.addListenerForSingleValueEvent(mEventListener)
        }
    }

    private fun saveDataToRoomDb(){
       dataFromFirebase.forEach { category ->
           addCategoryToLocalDb(category)
       }
    }

    private fun addCategoryToLocalDb (categoryModel: CategoryModel){
        Observable.fromCallable {
            Log.d(categoryModel.category, " ${categoryModel}")
            val categoryId = database.categoryDao().insert(Category(0, categoryModel.category))
            addSubCategoryToLocalDb(categoryModel.subCategories, categoryId)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe()
    }

    private fun addSubCategoryToLocalDb (subCategoryModels: List<SubCategoryModel>?, categoryId : Long){
        if (subCategoryModels != null) {
            Log.d("subCategories", " $subCategoryModels")
            for (subCategory in subCategoryModels) {
                val subCategoryId = database.subCategoryDao().insert(SubCategory(0, subCategory.subCategory, categoryId))
                addQuotesToLocalDb(subCategory.quotes, subCategoryId)
            }
        }
    }

    private fun addQuotesToLocalDb (quotes: List<QuoteModel>?, subCategoryId: Long){
        if (quotes != null) {
            Log.d("quotes", " $quotes")
            for (quote in quotes)
                database.quoteDao().insert(Quote(0, quote.author!!, quote.quote!!,subCategoryId))
        }
    }

    override fun onStop() {
        super.onStop()
        detachDatabaseReadListener()
    }

    private fun detachDatabaseReadListener() {
        if (mQuotesDatabaseReference != null) {
            mQuotesDatabaseReference?.removeEventListener(mChildListener)
            mQuotesDatabaseReference?.removeEventListener(mEventListener)
            mChildListener = null
            mEventListener = null
        }
    }
}
