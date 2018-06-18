package com.kanykeinu.quotesapp.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.content.ContextCompat.getDrawable
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
import com.kanykeinu.quotesapp.model.SelectableItem
import com.kanykeinu.quotesapp.showToast
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.subcategory_item.view.*
import java.util.*

/**
 * Created by KanykeiNu on 14.05.2018.
 */
class SubCategoryAdapter(private val context: Context, private val objects: List<SelectableItem<SubCategory>>) : BaseAdapter<SelectableItem<SubCategory>>(context,objects) {

    override fun onBindData(holder: RecyclerView.ViewHolder, obj: SelectableItem<SubCategory>) {
        val viewHolder = holder as SubCategoryViewHolder
        viewHolder.bind(obj, context)
    }

    override fun onHolderClick(obj: SelectableItem<SubCategory>, view: View, position: Int) {
        var isSelected = obj.isSelected
        if (isSelected!!) {
            obj.isSelected = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.background = ContextCompat.getDrawable(context, R.drawable.category_background_pressed)
            }
            (view as TextView).setTextColor(Color.WHITE)
            view.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_add_white), null, null, null)
            notifyItemChanged(position)
        } else {
            obj.isSelected = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.background = ContextCompat.getDrawable(context, R.drawable.category_background)
            }
            (view as TextView).setTextColor(ContextCompat.getColor(context, android.R.color.black))
            view.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_done_black), null, null, null)
            notifyItemChanged(position)
        }
    }

    override fun setSize(): Int {
        return objects.size
    }

    override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.subcategory_item, parent, false)
        return SubCategoryViewHolder(view)
    }

    class SubCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(selectableItem: SelectableItem<SubCategory>, context: Context) {
            itemView.tvSubCategory?.text = selectableItem.selectableItem.subCategory
            var isSelected = selectableItem.isSelected
            if (isSelected!!) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemView.background = ContextCompat.getDrawable(context, R.drawable.category_background_pressed)
                }
                (itemView.tvSubCategory as TextView).setTextColor(ContextCompat.getColor(context, android.R.color.black))
                itemView.tvSubCategory.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_done_black), null, null, null)

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemView.background = ContextCompat.getDrawable(context, R.drawable.category_background)
                }
                (itemView.tvSubCategory as TextView).setTextColor(Color.WHITE)
                itemView.tvSubCategory.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_add_white), null, null, null)
            }
        }
    }
}