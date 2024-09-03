package com.rahul.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rahul.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.spinView.apply {
            setDataItems(
                listOf(
                    "100x",
                    "0.5x",
                    "1x",
                    "2x",
                    "3x",
                    "4x",
                    "5x"
                )
            )
            startInfiniteScroll()
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                setResult("5x") {
                    Log.e("Hello", "World")
                }
            }
        }
    }
}