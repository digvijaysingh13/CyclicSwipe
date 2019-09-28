package com.example.cyclicswipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val item = arrayOf(AbcModel("Scene-1", R.mipmap.exe_1), AbcModel("Scene-2", R.mipmap.exe_2),
            AbcModel("Scene-3", R.mipmap.exe_3), AbcModel("Scene-4", R.mipmap.exe_4),AbcModel("Scene-5", R.mipmap.exe_5))


        val adp = XyzAdapter(item)

        val viewPager = findViewById<ViewPager>(R.id.vpd)

        viewPager.adapter = adp

        val ll = findViewById<LinearLayout>(R.id.dot_container)

        for (e in 0 until item.size) {
            val v = LayoutInflater.from(this).inflate(R.layout.item_dot, ll, false) as View
            v.background = ContextCompat.getDrawable(this, R.drawable.unselected_dot)
            ll.addView(v)
        }


        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var page = 0
            override fun onPageScrollStateChanged(state: Int) {
                if (page == 0 || page == (MaxSize - 1)) {
                    viewPager.setCurrentItem(MaxSize / 2, false)
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                page = position
                val pos = position % item.size
                (ll.getChildAt(pos) as View).background =
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.selected_dot)

                when (pos) {
                    0 -> {
                        (ll.getChildAt(1) as View).background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.unselected_dot)
                        (ll.getChildAt(item.size - 1) as View).background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.unselected_dot)
                    }
                    item.size - 1 -> {
                        (ll.getChildAt(0) as View).background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.unselected_dot)
                        (ll.getChildAt(item.size - 2) as View).background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.unselected_dot)
                    }
                    else -> {

                        (ll.getChildAt(pos - 1) as View).background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.unselected_dot)


                        (ll.getChildAt(pos + 1) as View).background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.unselected_dot)
                    }
                }


            }

        })

        viewPager.setCurrentItem(MaxSize / 2, false)

    }
}

const val MaxSize = 50

class AbcModel(val name:String, val res:Int)

class XyzAdapter(private val item: Array<AbcModel>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = MaxSize

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pos = position % item.size
        val iv =
            LayoutInflater.from(container.context).inflate(R.layout.fragment_text, container, false)
        val tv = iv.findViewById<TextView>(R.id.text_frag_tv)
        tv.text = item[pos].name
        val im= iv.findViewById<ImageView>(R.id.text_frag_iv)
        im.setImageResource(item[pos].res)
        container.addView(iv)
        return iv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }


}
