package maria.incyberspace.customviewattempt

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var view: CustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view = findViewById<CustomView>(R.id.custom_view).apply {
            setOnClickListener {
                spin()
            }
        }

        findViewById<CustomSlider>(R.id.custom_slider).apply {
            addOnChangeListener { _, float, _ ->
                val params = view.layoutParams
                val w = (250 + 400 * float / 100).roundToInt()
                params.apply {
                    width = w
                    height = w
                }
                view.layoutParams = params
                view.requestLayout()
            }
        }

        findViewById<Button>(R.id.btnReset).apply {
            setOnClickListener {
                view.clearAll()
            }
        }

    }
}