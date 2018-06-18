package com.kanykeinu.quotesapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanykeinu.quotesapp.R
import com.kanykeinu.quotesapp.database.entity.Quote
import kotlinx.android.synthetic.main.quote_item.view.*

/**
 * Created by KanykeiNu on 14.05.2018.
 */
class QuoteAdapter (private val context: Context, private val objects: MutableList<Quote>, private val onQuoteSelected: OnItemSelected) : BaseAdapter<Quote>(context,objects){
    override fun onHolderClick(obj: Quote, view: View, position: Int) {
        onQuoteSelected.itemPressed(obj)
    }

    override fun onBindData(holder: RecyclerView.ViewHolder, obj: Quote) {
        val viewHolder = holder as QuoteViewHolder
        viewHolder.bind(obj)
    }

    override fun setSize(): Int {
       return objects.size
    }

    override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quote_item,parent,false)
        return QuoteViewHolder(view)
    }

    class QuoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(quote: Quote) {
            itemView.quote.text = quote.text
            itemView.author.text = quote.author
        }
    }
}

interface OnItemSelected{
    fun itemPressed(obj: Quote)
}