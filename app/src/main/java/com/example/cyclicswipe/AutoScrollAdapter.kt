package com.example.cyclicswipe

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager


private const val MaxSize = 20
private const val SCROLL_DELAY:Long= 5*1000

class AutoScrollAdapter(private val item: Array<AutoScrollModel>, private val mViewPager: ViewPager, private val container: LinearLayout) :
    PagerAdapter(),
    ViewPager.OnPageChangeListener, View.OnTouchListener {

    private var lastPage = 0
    private var page = 0
    private val mScHdr by lazy {
        AutoScrollHandler(Looper.getMainLooper())
    }

    private var thd: Thread

    var isContinue = true

    init {
        mViewPager.adapter = this
        mViewPager.addOnPageChangeListener(this)

        for (e in item.indices) {
            val v = LayoutInflater.from(container.context).inflate(
                R.layout.item_dot,
                container,
                false
            ) as View
            v.background = ContextCompat.getDrawable(
                container.context,
                R.drawable.unselected_dot
            )
            container.addView(v)
        }


        thd = Thread {
            while (isContinue)
                try {
                    Thread.sleep(SCROLL_DELAY)
                    mScHdr.obtainMessage().also { msg ->
                        msg.arg1 = 0
                        mScHdr.sendMessage(msg)
                    }
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                }
        }
        thd.start()


    }

    fun setCurrentItem() = mViewPager.setCurrentItem(MaxSize / 2, false)

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = MaxSize

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pos = position % item.size
        val iv =
            LayoutInflater.from(container.context)
                .inflate(R.layout.fragment_text, container, false)
        val tv = iv.findViewById<TextView>(R.id.as_tv)
        tv.text = item[pos].name
        val im = iv.findViewById<ImageView>(R.id.as_iv)
        im.setImageResource(item[pos].res)
        iv.findViewById<Button>(R.id.as_btn).setOnTouchListener(this)
        container.addView(iv)
        return iv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }

    //region=========Implementation of Scroll Page Handler==========


    override fun onPageScrollStateChanged(state: Int) {
        if (page == 0 || page == (MaxSize - 1)) {
            mViewPager.setCurrentItem(MaxSize / 2, false)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        lastPage = page
        page = position
        Log.e("PageSelected", "page=$page, lastPage=$lastPage")
        Log.e("PageSelected", "===============================")
        val pos = position % item.size
        (container.getChildAt(pos) as View).background =
            ContextCompat.getDrawable(
                container.context,
                R.drawable.selected_dot
            )

        when (pos) {
            0 -> {
                (container.getChildAt(1) as View).background =
                    ContextCompat.getDrawable(
                        container.context,
                        R.drawable.unselected_dot
                    )
                (container.getChildAt(item.size - 1) as View).background =
                    ContextCompat.getDrawable(
                        container.context,
                        R.drawable.unselected_dot
                    )
            }
            item.size - 1 -> {
                (container.getChildAt(0) as View).background =
                    ContextCompat.getDrawable(
                        container.context,
                        R.drawable.unselected_dot
                    )
                (container.getChildAt(item.size - 2) as View).background =
                    ContextCompat.getDrawable(
                        container.context,
                        R.drawable.unselected_dot
                    )
            }
            else -> {

                (container.getChildAt(pos - 1) as View).background =
                    ContextCompat.getDrawable(
                        container.context,
                        R.drawable.unselected_dot
                    )


                (container.getChildAt(pos + 1) as View).background =
                    ContextCompat.getDrawable(
                        container.context,
                        R.drawable.unselected_dot
                    )
            }
        }


    }

    //endregion


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Log.e("MOTION EVENT", "${event?.action}")
        when(event?.action){
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP, MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_CANCEL ->{
                thd.interrupt()
            }
        }
        return v?.onTouchEvent(event)?:false
    }

    inner class AutoScrollHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            removeCallbacksAndMessages(null)

            if (page - lastPage >= 0) {
                this.post {
                    lastPage = page
                    val x=page+1
                    mViewPager.setCurrentItem(x, true)
                }
            } else {
                this.post {
                    lastPage=page
                    val x = page-1
                    mViewPager.setCurrentItem(x, true)
                }
            }

        }

    }


}


class AutoScrollModel(val name: String, val res: Int)