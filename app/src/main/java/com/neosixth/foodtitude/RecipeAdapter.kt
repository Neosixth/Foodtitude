package com.neosixth.foodtitude

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class RecipeAdapter(private val context: Context,
                    private val dataSource: ArrayList<ReadJsonRecipe>) :BaseAdapter(){

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
        val rowView = inflater.inflate(R.layout.list_item_recipe, parent, false)

        //Each row's content view
        val titleTextView = rowView.findViewById(R.id.recipe_list_title) as TextView
        val thumbnailImageView = rowView.findViewById(R.id.recipe_list_thumbnail) as ImageView

        //Inserting actual recipe data into content view
        val recipe = getItem(position) as ReadJsonRecipe
        titleTextView.text = recipe.title
        //Loads images on a separate thread
        Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)

        return rowView
    }



}