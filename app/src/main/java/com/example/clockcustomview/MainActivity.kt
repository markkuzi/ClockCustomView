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

        binding.btn.setOnClickListener {
            binding.customCLock3.setTimeZone("Asia/Tokyo")
        }


    }
}