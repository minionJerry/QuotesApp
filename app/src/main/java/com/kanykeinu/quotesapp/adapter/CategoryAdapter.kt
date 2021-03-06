package com.kanykeinu.quotesapp.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kanykeinu.quotesapp.R
import com.kanykeinu.quotesapp.database.entity.Category
import com.kanykeinu.quotesapp.model.SelectableItem
import kotlinx.android.synthetic.main.category_item.view.*

/**
 * Created by KanykeiNu on 14.05.2018.
 */
class CategoryAdapter(private val context: Context, private val objects: List<SelectableItem<Category>>) : BaseAdapter<SelectableItem<Category>>(context,objects){

    override fun onHolderClick(obj: SelectableItem<Category>, view: View, position: Int) {
        val isSelected = obj.isSelected
        if (isSelected) {
            obj.isSelected = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.background = ContextCompat.getDrawable(context,R.drawable.category_background_pressed)
            }
            (view.category as TextView).setTextColor(Color.WHITE)
            notifyItemChanged(position)
        } else {
            obj.isSelected = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.background = ContextCompat.getDrawable(context,R.drawable.category_background)
            }
            (view.category as TextView).setTextColor(ContextCompat.getColor(context,android.R.color.black))
            notifyItemChanged(position)
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemView.background = ContextCompat.getDrawable(context,R.drawable.category_background_pressed)
                }
                (itemView.category as TextView).setTextColor(ContextCompat.getColor(context,android.R.color.black))
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemView.background = ContextCompat.getDrawable(context,R.drawable.category_background)
                }
                (itemView.category as TextView).setTextColor(Color.WHITE)
            }
        }
    }
}
