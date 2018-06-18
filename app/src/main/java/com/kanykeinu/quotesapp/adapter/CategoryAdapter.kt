package com.kanykeinu.quotesapp.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.kanykeinu.quotesapp.R
import com.kanykeinu.quotesapp.R.id.parent
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.model.SelectableItem
import com.kanykeinu.quotesapp.showToast
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.subcategory_item.view.*
import java.util.*

/**
 * Created by KanykeiNu on 14.05.2018.
 */
class CategoryAdapter(private val context: Context, private val objects: List<SelectableItem<Category>>) : BaseAdapter<SelectableItem<Category>>(context,objects){

    override fun onHolderClick(obj: SelectableItem<Category>, view: View, position: Int) {
        val isSelected = obj.isSelected
        if (isSelected) {
            obj.isSelected = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.background = context.getDrawable(R.drawable.category_background)
                (view.category as TextView).setTextColor(Color.WHITE)
//                view.category.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_add_white),null,null,null)
                notifyItemChanged(position)
            }
        } else {
            obj.isSelected = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.background = context.getDrawable(R.drawable.category_background_pressed)
                (view.category as TextView).setTextColor(context.getColor(android.R.color.black))
//                view.category.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_done_black),null,null,null)
                notifyItemChanged(position)
            }
        }
    }

    override fun onBindData(holder: RecyclerView.ViewHolder, obj: SelectableItem<Category>) {
        val viewHolder = holder as CategoryViewHolder
        viewHolder.bind(obj,context)
    }

    override fun setSize(): Int {
       return objects.size
    }

    override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false)

        return CategoryViewHolder(view)
    }



    class CategoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: SelectableItem<Category>,context: Context) {
            itemView.category?.text = category.selectableItem.category
            val isSelected = category.isSelected
            if (isSelected) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.background = context.getDrawable(R.drawable.category_background_pressed)
                    (itemView.category as TextView).setTextColor(context.getColor(android.R.color.black))
//                    itemView.category.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_done_black),null,null,null)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.background = context.getDrawable(R.drawable.category_background)
                    (itemView.category as TextView).setTextColor(Color.WHITE)
//                    itemView.category.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_add_white),null,null,null)
                }
            }
        }
    }
}
