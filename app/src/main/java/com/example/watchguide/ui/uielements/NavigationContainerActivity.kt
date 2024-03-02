package com.example.watchguide.ui.uielements

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.watchguide.databinding.ActivityNavigationContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}