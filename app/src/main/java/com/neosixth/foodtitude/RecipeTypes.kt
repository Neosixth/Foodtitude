package com.neosixth.foodtitude

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.io.File

class RecipeTypes : AppCompatActivity() {

    var chosenFilter = "All"
    var allRecipe = ArrayList<ReadJsonRecipe>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_types)

        val newjson_file = File(this.filesDir, "recipesUpdated.json")
        var contentsY = ""
        //If file exists, use this updated record
        if (newjson_file.exists()) {
            contentsY = newjson_file.readText()
            allRecipe  = ReadJsonRecipe.getRecipesFromLocalFile(contentsY, this)

        } else {
            //If not exist, create file
            allRecipe = ReadJsonRecipe.getRecipesFromAssetFile("recipes2.json", this)
            val filename = "recipesUpdated.json"
            //get text of recipes2.json
            val fileContents = application.assets.open("recipes2.json").bufferedReader().use{
                it.readText()
            }
            //write above text into file recipesUpdated.json
            this.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(fileContents.toString().toByteArray())
            }
        }

        //Check if new json is received from RecipePage
        var jsonStringLocal = ""
        if(intent.getStringExtra("jsonString") != ""){
            //Changes are made. Update allRecipe
            jsonStringLocal = intent.getStringExtra("jsonString")
            allRecipe = ReadJsonRecipe.getRecipesFromLocalFile(jsonStringLocal, this)
            contentsY = jsonStringLocal

            //Update file
            val filename = "recipesUpdated.json"
            val fileContents = contentsY
            //write above text into file recipesUpdated.json
            this.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(fileContents.toString().toByteArray())
            }
        }

        //Setup listView
        var listView = findViewById<ListView>(R.id.recipe_list_view)
        //Actual recipes to show after filter
        val filteredRecipes = arrayListOf<ReadJsonRecipe>()
        var adapterLV = RecipeAdapter(this, filteredRecipes)
        listView.adapter = adapterLV
        listView.setOnItemClickListener { parent, view, position, id ->
            val element = adapterLV.getItem(position) as ReadJsonRecipe // The item that was clicked
            val intent = Intent(this, RecipePage::class.java)
            intent.putExtra("title", element.title)
            intent.putExtra("type", element.type)
            intent.putExtra("image", element.imageUrl)
            intent.putExtra("ingredients", element.ingredients)
            intent.putExtra("steps", element.steps)
            intent.putExtra("jsonString", ReadJsonRecipe.getStringFromAllRecipe(allRecipe))
            startActivity(intent)
        }

        val recipeTypes = resources.getStringArray(R.array.recipeTypes)
        val spinner_recipeTypes = findViewById<Spinner>(R.id.spinner)
        if (spinner_recipeTypes != null) {
            val adapterSpinner = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, recipeTypes
            )
            spinner_recipeTypes.adapter = adapterSpinner
            spinner_recipeTypes.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    chosenFilter = recipeTypes[position]
                    filteredRecipes.clear()
                    for (i in 0..allRecipe.size-1){
                        val recipe0 = allRecipe.get(i)
                        //listTitles[i] = recipe0.title
                        if(recipe0.type == chosenFilter||chosenFilter == "All"){
                            filteredRecipes.add(recipe0)
                        }
                    }
                    adapterLV.notifyDataSetChanged()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }//End of Spinner's if

        //Reset all recipes to original default values
        var button_recipeTypes = findViewById<Button>(R.id.recipe_types_reset)
        button_recipeTypes.setOnClickListener{
            allRecipe = ReadJsonRecipe.getRecipesFromAssetFile("recipes2.json", this)
            val filename = "recipesUpdated.json"
            //get text of recipes2.json
            val fileContents = application.assets.open("recipes2.json").bufferedReader().use{
                it.readText()
            }
            //write above text into file recipesUpdated.json
            this.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(fileContents.toString().toByteArray())
            }
            val spinner_recipeTypes = findViewById<Spinner>(R.id.spinner)
            filteredRecipes.clear()
            for (i in 0..allRecipe.size-1){
                val recipe0 = allRecipe.get(i)
                if(recipe0.type == chosenFilter||chosenFilter == "All"){
                    filteredRecipes.add(recipe0)
                }
            }
            adapterLV.notifyDataSetChanged()
            Toast.makeText(this@RecipeTypes, "All recipes data has been reset", Toast.LENGTH_SHORT).show()
        }

        var button_createRecipe = findViewById<Button>(R.id.recipe_types_createRecipe)
        button_createRecipe.setOnClickListener{
            val intent = Intent(this, RecipePage::class.java)
            intent.putExtra("title", "New Recipe")
            intent.putExtra("type", "Breakfast")
            intent.putExtra("image", "https://www.edamam.com/web-img/341/3417c234dadb687c0d3a45345e86bff4.jpg")
            intent.putExtra("ingredients", "[\"Add ingredients\", \"Add ingredients\"]")
            intent.putExtra("steps", "[\"Add steps\", \"Add steps\"]")
            intent.putExtra("jsonString", ReadJsonRecipe.getStringFromAllRecipe(allRecipe))
            intent.putExtra("createRecipe", "true")
            startActivity(intent)
        }

    }//End of onCreate


}//End of class