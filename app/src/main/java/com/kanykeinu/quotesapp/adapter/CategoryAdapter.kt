package com.kanykeinu.quotesapp.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.kanykeinu.quotesapp.R
import com.kanykeinu.quotesapp.R.id.parent
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.showToast
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.subcategory_item.view.*
import java.util.*

/**
 * Created by KanykeiNu on 14.05.2018.
 */
class CategoryAdapter(private val context: Context, private val objects: MutableList<Category>, private val onCategorySelected: onItemSelected) : BaseAdapter<Category>(context,objects){

    override fun onHolderClick(obj: Category, view: View) {
        onCategorySelected.itemPressed(obj,view)
        context.showToast(obj.category!!)
    }

    override fun onBindData(holder: RecyclerView.ViewHolder, obj: Category) {
        val viewHolder = holder as CategoryViewHolder
        viewHolder.bind(obj)
    }

    override fun setSize(): Int {
       return objects.size
    }

    override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false)
        return CategoryViewHolder(view)
    }

    class CategoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: Category) {
            itemView.category?.text = category.category
        }
    }
}

interface onItemSelected{
    fun itemPressed(obj : Any ,view: View)
}