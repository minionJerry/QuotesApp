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
import com.kanykeinu.quotesapp.database.entity.SubCategory
import com.kanykeinu.quotesapp.showToast
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.subcategory_item.view.*
import java.util.*

/**
 * Created by KanykeiNu on 14.05.2018.
 */
class SubCategoryAdapter(private val context: Context, private val objects: MutableList<SubCategory>, private val onCategorySelected: onItemSelected) : BaseAdapter<SubCategory>(context,objects){

    override fun onHolderClick(obj: SubCategory, view: View) {
        onCategorySelected.itemPressed(obj,view)
        context.showToast(obj.subCategory!!)
    }

    override fun onBindData(holder: RecyclerView.ViewHolder, obj: SubCategory) {
        val viewHolder = holder as SubCategoryViewHolder
        viewHolder.bind(obj)
    }

    override fun setSize(): Int {
       return objects.size
    }

    override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.subcategory_item,parent,false)
        return SubCategoryViewHolder(view)
    }

    class SubCategoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: SubCategory) {
            itemView.tvSubCategory?.text = category.subCategory
        }
    }
}
