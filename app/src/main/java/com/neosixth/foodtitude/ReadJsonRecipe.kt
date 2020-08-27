package com.neosixth.foodtitude

import android.content.Context
import org.json.JSONException
import org.json.JSONObject


class ReadJsonRecipe(
    var title: String,
    var type: String,
    var imageUrl: String,
    var ingredients: String,
    var steps: String) {

    companion object {

        //Convert ReadJsonRecipe Arraylist into pure string
        fun getStringFromAllRecipe(allRecipe: ArrayList<ReadJsonRecipe>): String{
            var output = "{ \"recipes\": ["
            for(i in 0..allRecipe.size-1){
                output += " { \"title\" : \""+allRecipe[i].title+"\" , "
                output += " \"type\" : \""+allRecipe[i].type+"\" , "
                output += " \"image\" : \""+allRecipe[i].imageUrl+"\" , "
                output += " \"ingredients\" : "+allRecipe[i].ingredients.replace("\\","")+" , "
                output += " \"steps\" : "+allRecipe[i].steps.replace("\\","")+""
                output += " } , "

            }
            output = output.removeSuffix(",")
            output += " ] }"
            return output
        }

        //If no new updated recipe file is created, use default json file in asset
        fun getRecipesFromAssetFile(filename: String, context: Context): ArrayList<ReadJsonRecipe> {
            val recipeList = ArrayList<ReadJsonRecipe>()

            try {
                // Load data
                val jsonString = loadJsonFromAsset(filename, context)
                val json = JSONObject(jsonString)
                val recipes = json.getJSONArray("recipes")

                // Get Recipe objects from data
                (0 until recipes.length()).mapTo(recipeList) {
                    ReadJsonRecipe(recipes.getJSONObject(it).getString("title"),
                        recipes.getJSONObject(it).getString("type"),
                        recipes.getJSONObject(it).getString("image"),
                        recipes.getJSONObject(it).getString("ingredients"),
                        recipes.getJSONObject(it).getString("steps"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return recipeList
        }

        fun getRecipesFromLocalFile(jsonText: String, context: Context): ArrayList<ReadJsonRecipe> {
            val recipeList = ArrayList<ReadJsonRecipe>()

            try {
                // Load data
                val jsonString = jsonText
                val json = JSONObject(jsonString)
                val recipes = json.getJSONArray("recipes")

                // Get Recipe objects from data
                (0 until recipes.length()).mapTo(recipeList) {
                    ReadJsonRecipe(recipes.getJSONObject(it).getString("title"),
                        recipes.getJSONObject(it).getString("type"),
                        recipes.getJSONObject(it).getString("image"),
                        recipes.getJSONObject(it).getString("ingredients").replace("\\",""),
                        recipes.getJSONObject(it).getString("steps").replace("\\",""))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return recipeList
        }

        fun addNewRecipe(allRecipe: ArrayList<ReadJsonRecipe>, title1: String, type1: String, imageUrl1: String, ingredients1: String, steps1: String ): ArrayList<ReadJsonRecipe>{
            var newRecipe = ReadJsonRecipe(title1, type1, imageUrl1, ingredients1, steps1)
            allRecipe.add(newRecipe)
            return allRecipe
        }

        private fun loadJsonFromAsset(filename: String, context: Context): String? {
            var json: String? = null

            try {
                val inputStream = context.assets.open(filename)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: java.io.IOException) {
                ex.printStackTrace()
                return null
            }
            return json
        }
    }
}