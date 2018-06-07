package com.kanykeinu.quotesapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanykeinu.quotesapp.R
import com.kanykeinu.quotesapp.model.QuoteModel
import kotlinx.android.synthetic.main.quote_item.view.*

/**
 * Created by KanykeiNu on 14.05.2018.
 */
class QuoteAdapter (private val context: Context, private val objects: List<QuoteModel>, private val onQuoteSelected: onItemSelected) : BaseAdapter<QuoteModel>(context,objects){

    override fun onBindData(holder: RecyclerView.ViewHolder, obj: QuoteModel) {
        val viewHolder = holder as QuoteViewHolder
        viewHolder.bind(obj)
    }

    override fun onHolderClick(obj: QuoteModel, view: View) {
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
        fun bind(quoteModel: QuoteModel) {
            itemView.quote.text = quoteModel.quote
            itemView.author.text = quoteModel.author
        }
    }
}