package com.neosixth.foodtitude

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class IngredientsAdapter(private val context: Context,
                    private val dataSource: List<String>) :BaseAdapter(){

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //Unique ID of each row
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for each row
        val rowView = inflater.inflate(R.layout.list_item_ingredients, parent, false)
        //Each row's content view
        val titleTextView = rowView.findViewById(R.id.list_item_ingredients_title) as TextView

        //Inserting actual recipe data into content view
        val recipe = getItem(position)
        titleTextView.text = recipe.toString()
        return rowView
    }



}