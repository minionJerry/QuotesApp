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
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.subcategory_item.view.*
import java.util.*

/**
 * Created by KanykeiNu on 14.05.2018.
 */
class CategoryAdapter(private val context: Context, private val objects: List<String>, private val isCategory: Boolean, private val onCategorySelected: onItemSelected) : BaseAdapter<String>(context,objects){

    override fun onBindData(holder: RecyclerView.ViewHolder, obj: String) {
        val viewHolder = holder as CategoryViewHolder
        viewHolder.bind(obj)
    }

    override fun onHolderClick(obj: String, view: View) {
        onCategorySelected.itemPressed(obj,view)
        Toast.makeText(context,obj,Toast.LENGTH_SHORT).show()
    }

    override fun setSize(): Int {
       return objects.size
    }

    override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        var layoutRes : Int
        if (isCategory) layoutRes = R.layout.category_item else layoutRes = R.layout.subcategory_item
        val view = LayoutInflater.from(context).inflate(layoutRes,parent,false)
        return CategoryViewHolder(view)
    }

    class CategoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: String) {
            itemView.category?.text = category
            itemView.subCategory?.text = category
        }
    }

}

interface onItemSelected{
    fun itemPressed(obj : Any ,view: View)
}