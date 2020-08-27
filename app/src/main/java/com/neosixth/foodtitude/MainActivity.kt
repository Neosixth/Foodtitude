package com.neosixth.foodtitude

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.cacheDir.deleteRecursively()

        val intent = Intent(this@MainActivity, RecipeTypes::class.java)
        intent.putExtra("jsonString", "")
        startActivity(intent)
    }
}
