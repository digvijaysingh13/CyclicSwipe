package com.example.cyclicswipe

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val item = arrayOf(
            AutoScrollModel("Scene-1", R.mipmap.exe_1),
            AutoScrollModel("Scene-2", R.mipmap.exe_2),
            AutoScrollModel("Scene-3", R.mipmap.exe_3),
            AutoScrollModel("Scene-4", R.mipmap.exe_4),
            AutoScrollModel("Scene-5", R.mipmap.exe_5)
        )


        val viewPager = findViewById<ViewPager>(R.id.vpd)
        val ll = findViewById<LinearLayout>(R.id.dot_container)
        val adp = AutoScrollAdapter(item, viewPager, ll)


        adp.setCurrentItem()


    }
}



