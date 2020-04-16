package com.example.cyclicswipe

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val item = arrayOf(
            AutoScrollModel(
                "Scene-1",
                "https://cdn.pixabay.com/photo/2013/08/20/15/47/sunset-174276__340.jpg"
            ),
            AutoScrollModel(
                "Scene-2",
                "https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg"
            ),
            AutoScrollModel(
                "Scene-3",
                "https://cdn.pixabay.com/photo/2016/10/20/18/35/sunrise-1756274__340.jpg"
            ),
            AutoScrollModel(
                "Scene-4",
                "https://cdn.pixabay.com/photo/2018/01/12/10/19/fantasy-3077928__340.jpg"
            ),
            AutoScrollModel(
                "Scene-5",
                "https://cdn.pixabay.com/photo/2018/10/30/16/06/water-lily-3784022__340.jpg"
            )
        )


        val viewPager = findViewById<ViewPager>(R.id.vpd)
        val ll = findViewById<LinearLayout>(R.id.dot_container)
        val adp = AutoScrollAdapter(item, viewPager, ll)

        val density = resources.displayMetrics.density
        adp.setCurrentItem()
        viewPager.clipToPadding = false
        viewPager.setPadding(0, 0, (46*density).toInt(), 0)
        viewPager.pageMargin = (16*density).toInt()


    }
}


fun loadImage(url: String, iv: ImageView) {

    val pcb = Picasso.Builder(iv.context).listener { picasso, uri, exception -> exception?.printStackTrace() }
        .build()

    pcb.load(url)
        .error(R.drawable.ic_broken_image)
        .networkPolicy(NetworkPolicy.OFFLINE)
        .into(iv)

}


fun loadImageG(url: String, iv: ImageView) {


    Glide.with(iv.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .listener(object : RequestListener< Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                e?.printStackTrace()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("Glide", "Image loaded ${dataSource?.name}")
                return false
            }

        } )
        .into(iv)
}

