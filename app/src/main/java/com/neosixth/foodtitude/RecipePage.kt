package com.neosixth.foodtitude

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import android.os.Build
import kotlinx.android.synthetic.main.alert_dialog_ingredientssteps.view.*
import kotlinx.android.synthetic.main.alert_dialog_with_edittext.view.dialog_editText
import kotlinx.android.synthetic.main.alert_dialog_with_edittext.view.dialog_enter


class RecipePage : AppCompatActivity() {

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 400;
        //Permission code
        private val PERMISSION_CODE = 401;
    }

    var allRecipe = ArrayList<ReadJsonRecipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_page)

        //Retrieve information from menu

        var recipeTitle = intent.getStringExtra("title")
        var recipeType = intent.getStringExtra("type")
        var recipeImage = intent.getStringExtra("image")
        var recipeIngredients = intent.getStringExtra("ingredients")
        recipeIngredients = recipeIngredients.removeSurrounding("[", "]")
        var recipeSteps = intent.getStringExtra("steps")
        recipeSteps = recipeSteps.removeSurrounding("[", "]")

        var jsonString = intent.getStringExtra("jsonString")
        allRecipe = ReadJsonRecipe.getRecipesFromLocalFile(jsonString, this)
        var allRecipeIndex = 0
        for(i in 0..allRecipe.size-1){
            if(allRecipe[i].title == recipeTitle){
                allRecipeIndex = i
                break
            }
        }



        var titleView = findViewById<TextView>(R.id.recipe_page_title)
        var typeView = findViewById<TextView>(R.id.recipe_page_type)
        var imageView = findViewById<ImageView>(R.id.recipe_page_image)

        var list_recipeIngredients: MutableList<String> = recipeIngredients.split(",") as MutableList<String>
        var list_recipeSteps = recipeSteps.split(",") as MutableList<String>

        titleView.text = recipeTitle
        typeView.text = "Type: "+recipeType
        Picasso.with(this).load(recipeImage).placeholder(R.mipmap.ic_launcher).into(imageView)

        //Setup listView
        var ingredients_listView = findViewById<ListView>(R.id.recipe_page_ingredients)
        var steps_listView = findViewById<ListView>(R.id.recipe_page_steps)
        ingredients_listView.setFocusable(false);
        steps_listView.setFocusable(false)

        var adapter_ingredients = IngredientsAdapter(this, list_recipeIngredients)
        ingredients_listView.adapter = adapter_ingredients
        UIUtils.setListViewHeightBasedOnItems(ingredients_listView)

        ingredients_listView.setOnItemClickListener { parent, view, position, id ->
            val mDialogView = layoutInflater.inflate(R.layout.alert_dialog_ingredientssteps, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Update ingredient")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            //Update ingredient column
            mDialogView.dialog_enter.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
                //get text from EditTexts of custom layout
                val updated_string = mDialogView.dialog_editText.text.toString()
                list_recipeIngredients[position] = updated_string
                var newString = "["+list_recipeIngredients.joinToString(",")+"]"

                allRecipe[allRecipeIndex].ingredients = newString
                adapter_ingredients.notifyDataSetChanged()
            }
            //Delete ingredient column
            mDialogView.dialog_delete.setOnClickListener {
                mAlertDialog.dismiss()
                list_recipeIngredients.removeAt(position)
                var newString = "["+list_recipeIngredients.joinToString(",")+"]"
                allRecipe[allRecipeIndex].ingredients = newString
                adapter_ingredients.notifyDataSetChanged()
            }
        }

        var adapter_steps = IngredientsAdapter(this, list_recipeSteps)
        steps_listView.adapter = adapter_steps
        UIUtils.setListViewHeightBasedOnItems(steps_listView)

        steps_listView.setOnItemClickListener { parent, view, position, id ->
            val mDialogView = layoutInflater.inflate(R.layout.alert_dialog_ingredientssteps, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Update step")
            val  mAlertDialog = mBuilder.show()
            //Edit step column
            mDialogView.dialog_enter.setOnClickListener {
                mAlertDialog.dismiss()
                //get text from EditTexts of custom layout
                val updated_string = mDialogView.dialog_editText.text.toString()
                list_recipeSteps[position] = updated_string
                var newString = "["+list_recipeSteps.joinToString(",")+"]"
                allRecipe[allRecipeIndex].steps = newString
                adapter_steps.notifyDataSetChanged()
            }

            //Delete column from steps
            mDialogView.dialog_delete.setOnClickListener {
                mAlertDialog.dismiss()
                list_recipeSteps.removeAt(position)
                var newString = "["+list_recipeSteps.joinToString(",")+"]"
                allRecipe[allRecipeIndex].steps = newString
                adapter_steps.notifyDataSetChanged()
            }
        }

        //Update Title
        var updateTitleView = findViewById<ImageView>(R.id.recipe_page_updateTitle)
        updateTitleView.setOnClickListener {
            //Inflate the dialog with custom view
            val mDialogView = layoutInflater.inflate(R.layout.alert_dialog_with_edittext, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Update title")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.dialog_enter.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
                //get text from EditTexts of custom layout
                val updated_string = mDialogView.dialog_editText.text.toString()
                titleView.text = updated_string
                allRecipe[allRecipeIndex].title = updated_string
            }
        }

        var updateTypeView = findViewById<ImageView>(R.id.recipe_page_updateType)
        updateTypeView.setOnClickListener {
            val listItems = arrayOf("Breakfast", "Main Dish", "Salad", "Dessert")
            val mBuilder = AlertDialog.Builder(this@RecipePage)
            mBuilder.setTitle("Choose recipe's type")
            mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
                typeView.text = "Type: "+listItems[i]
                allRecipe[allRecipeIndex].type = listItems[i]
                dialogInterface.dismiss()
            }
            // Set cancel button click listener
            mBuilder.setNeutralButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            val mDialog = mBuilder.create()
            mDialog.show()
        }

        var updateImageView = findViewById<ImageView>(R.id.recipe_page_updateImage)
        updateImageView.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
        var addIngredientView = findViewById<ImageView>(R.id.recipe_page_addIngredients)
        addIngredientView.setOnClickListener {

            //Inflate the dialog with custom view
            val mDialogView = layoutInflater.inflate(R.layout.alert_dialog_with_edittext, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Add ingredient")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.dialog_enter.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
                val updated_string = mDialogView.dialog_editText.text.toString()
                list_recipeIngredients.add(updated_string)
                var newString = "["+list_recipeIngredients.joinToString(",")+"]"
                allRecipe[allRecipeIndex].ingredients = newString
                adapter_ingredients.notifyDataSetChanged()
                UIUtils.setListViewHeightBasedOnItems(ingredients_listView)
            }
        }

        var addStepView = findViewById<ImageView>(R.id.recipe_page_addSteps)
        addStepView.setOnClickListener {

            //Inflate the dialog with custom view
            val mDialogView = layoutInflater.inflate(R.layout.alert_dialog_with_edittext, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Add step")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.dialog_enter.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
                val updated_string = mDialogView.dialog_editText.text.toString()
                list_recipeSteps.add(updated_string)
                var newString = "["+list_recipeSteps.joinToString(",")+"]"
                allRecipe[allRecipeIndex].steps = newString
                adapter_steps.notifyDataSetChanged()
                UIUtils.setListViewHeightBasedOnItems(steps_listView)
            }
        }





        val tb_back = findViewById<TextView>(R.id.toolbar_recipe_page_back)
        tb_back.setOnClickListener{
            var recipeTitle = intent.getStringExtra("createRecipe")
            if(intent.getStringExtra("createRecipe")!=null){
                var newRecipeOb = ReadJsonRecipe.addNewRecipe(allRecipe, titleView.text as String, typeView.text as String, recipeImage,"["+list_recipeIngredients.joinToString(",")+"]","["+list_recipeSteps.joinToString(",")+"]")
                val intent = Intent(this, RecipeTypes::class.java)
                intent.putExtra("jsonString", ReadJsonRecipe.getStringFromAllRecipe(newRecipeOb))
                startActivity(intent)

            } else{
                val intent = Intent(this, RecipeTypes::class.java)
                //need turn allRecipe back to string
                intent.putExtra("jsonString", ReadJsonRecipe.getStringFromAllRecipe(allRecipe))
                startActivity(intent)
            }
        }
    } //End of onCreate

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            var imageView = findViewById<ImageView>(R.id.recipe_page_image)
            imageView.setImageURI(data?.data)
        }
    }

}
