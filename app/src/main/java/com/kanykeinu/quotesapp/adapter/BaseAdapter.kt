package com.kanykeinu.quotesapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kanykeinu.quotesapp.model.SelectableItem

/**
 * Created by KanykeiNu on 14.05.2018.
 */
abstract class BaseAdapter<T>(private val mContext: Context, private var objects: List<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return setViewHolder(parent)
    }

    abstract fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun onBindData(holder: RecyclerView.ViewHolder, obj : T)

    abstract fun onHolderClick(obj: T,view : View,position: Int)

    abstract fun setSize(): Int

    override fun getItemCount(): Int {
        return setSize()
    }


    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(holder, objects.get(position))
        holder.itemView.setOnClickListener { onHolderClick(objects[position], holder.itemView as View, position) }
    }
}