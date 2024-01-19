package com.example.watchguide.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.watchguide.databinding.ActivityNavigationContainerBinding

class NavigationContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}