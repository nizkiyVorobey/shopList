package com.example.shoppinglist.presentation.shop_item.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemShopName: TextView = view.findViewById(R.id.itemShopName)
    val itemShopCount: TextView = view.findViewById(R.id.itemShopCount)
}