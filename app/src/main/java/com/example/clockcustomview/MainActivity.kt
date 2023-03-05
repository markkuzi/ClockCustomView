package com.example.clockcustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clockcustomview.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = TimeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()

    }
}