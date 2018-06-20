package com.kanykeinu.quotesapp.adapter

import com.kanykeinu.quotesapp.database.entity.Quote

interface OnItemSelected {
    fun itemPressed(obj: Quote)
}