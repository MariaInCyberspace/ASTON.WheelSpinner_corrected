package maria.incyberspace.customviewattempt

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val paintForOuterCircle = Paint()
    private val rect = RectF(0f, 0f, 0f, 0f)
    private val colors = Colors.colors
    private val randomTexts = RandomTexts.randomTexts
    private val sweepAngle = 360.0f / colors.size

    private var currentColor = Color.WHITE
    private var imageNotEmpty = false
    private var textNotEmpty = false

    init {
        paint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        paintForOuterCircle.apply {
            style = Paint.Style.STROKE
            strokeWidth = 20f
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rect.set(100f, 100f, width.toFloat() - 100, height.toFloat() - 100)

        canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, width.toFloat() / 2 - 50,
            paintForOuterCircle.apply { color = currentColor })

        colors.forEachIndexed { index, color ->
            paint.color = ContextCompat.getColor(context, color)
            canvas.drawArc(rect, sweepAngle * index, sweepAngle, true, paint)
        }

    }

    fun spin() {
        val colorIndex = (colors.indices).random()
        val toAngle = sweepAngle * ( if (colorIndex == 0) 1 else colorIndex)

        ValueAnimator.ofFloat(0f, toAngle * 5).apply {
            duration = 3500
            interpolator = DecelerateInterpolator()

            addUpdateListener {
                rotation = it.animatedValue as Float
                currentColor = Color.WHITE
                invalidate()
            }

            doOnEnd {
                currentColor = ContextCompat.getColor(context, colors[colorIndex])
                showContent(colorIndex)
                invalidate()
            }

            start()

        }
    }

    fun clearAll() {
        (parent as ViewGroup).findViewById<LinearLayout>(R.id.ll_horizontal).apply {
                findViewById<LinearLayout>(R.id.ll_vertical_pic).removeAllViews()
                findViewById<LinearLayout>(R.id.ll_vertical_text).removeAllViews()
            }
    }

    private fun clearImage() {
        (parent as ViewGroup).findViewById<LinearLayout>(R.id.ll_horizontal).apply {
                findViewById<LinearLayout>(R.id.ll_vertical_pic).removeAllViews()
            }
    }

    private fun clearText() {
        (parent as ViewGroup).findViewById<LinearLayout>(R.id.ll_horizontal).apply {
                findViewById<LinearLayout>(R.id.ll_vertical_text).removeAllViews()
            }
    }

    private fun addContent(content: View) {
        val parentViewGroup = (parent as ViewGroup).findViewById<LinearLayout>(R.id.ll_horizontal)
        if (content is ImageView) {
            if (imageNotEmpty) clearImage()
            parentViewGroup.findViewById<LinearLayout>(R.id.ll_vertical_pic).addView(content)
            imageNotEmpty = true
        } else {
            if (textNotEmpty) clearText()
            parentViewGroup.findViewById<LinearLayout>(R.id.ll_vertical_text).addView(content)
            textNotEmpty = true
        }
    }


    private fun showContent(index: Int) {
        if (index % 2 == 0) showText() else showImage()
    }

    private fun showImage() {
        val iv = ImageView(context).apply {
            loadImage(this)
        }
        addContent(iv)
    }

    private fun showText() {
        val tv = TextView(context).apply {
            val i = (randomTexts.indices).random()
            text = randomTexts[i]
            textSize = 20f
            setTextColor(Color.BLACK)
        }
        addContent(tv)
    }

    private fun loadImage(image: ImageView) {
        Picasso.get()
            .load("https://placebeard.it/300")
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(image)
    }

}