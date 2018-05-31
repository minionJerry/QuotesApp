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
import com.kanykeinu.quotesapp.R.id.category
import com.kanykeinu.quotesapp.R.id.parent
import com.kanykeinu.quotesapp.model.Quote
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.quote_item.view.*
import java.util.*

/**
 * Created by KanykeiNu on 14.05.2018.
 */
class QuoteAdapter (private val context: Context, private val objects: List<Quote>, private val onQuoteSelected: onItemSelected) : BaseAdapter<Quote>(context,objects){

    override fun onBindData(holder: RecyclerView.ViewHolder, obj: Quote) {
        val viewHolder = holder as QuoteViewHolder
        viewHolder.bind(obj)
    }

    override fun onHolderClick(obj: Quote, view: View) {
        onQuoteSelected.itemPressed(obj,view)
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
            itemView.quote.text = quote.quote
            itemView.author.text = quote.author
        }
    }
}