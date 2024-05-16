package com.example.caloriecounter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomAdapter(private val context: Context, private val itemList: List<String>) :
    ArrayAdapter<String>(context, R.layout.custom_adapater_layout, itemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_adapater_layout, parent, false)

        val itemNameTextView = view.findViewById<TextView>(R.id.item_name)
        val itemCaloriesTextView = view.findViewById<TextView>(R.id.item_calories)

        val item = itemList[position]
        val parts = item.split("  |  ")

        itemNameTextView.text = parts[0]
        itemCaloriesTextView.text = parts[1]

        return view
    }
}