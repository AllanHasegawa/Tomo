package tomo.showcase.mainScreen

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringConfig
import com.facebook.rebound.SpringListener
import com.facebook.rebound.SpringSystem
import kotlin.math.absoluteValue
import kotlin.random.Random

class HappyLittleView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private lateinit var system: SpringSystem
    private lateinit var springX: Spring
    private lateinit var springY: Spring

    override fun onFinishInflate() {
        super.onFinishInflate()

        system = SpringSystem.create()

        val configX = SpringConfig(25.0, 4.0)
        val configY = SpringConfig(100.0, 4.0)

        springX = system.createSpring().setSpringConfig(configX)
        springY = system.createSpring().setSpringConfig(configY)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        springX.setCurrentValue(4.0, false)
        springY.setCurrentValue(1.0, false)

        val displacementXScale = { width * 0.1f }
        val displacementYScale = { height * 1.0f }

        val childView = getChildAt(0)

        springY.addListener(springListener(true) { spring ->
            val displacement = spring.currentValue * displacementYScale()
            childView.translationY = -displacement.toFloat().absoluteValue
        })
        springX.addListener(springListener(false) { spring ->
            val displacement = spring.currentValue * displacementXScale()
            childView.translationX = displacement.toFloat()
        })

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        springY.removeAllListeners()
        springX.removeAllListeners()
    }

    private fun jump() {
        postDelayed({
            springX.endValue = Random.nextDouble(-0.5, 0.5)
            springY.endValue = Random.nextDouble(0.3)

            postDelayed({
                springX.endValue = 0.0
                springY.endValue = 0.0
            }, 500)
        }, 500)
    }

    private fun springListener(jump: Boolean, block: (Spring) -> Unit) = object : SpringListener {
        override fun onSpringUpdate(spring: Spring) {
            block(spring)
        }

        override fun onSpringEndStateChange(spring: Spring?) {
        }

        override fun onSpringAtRest(spring: Spring) {
            if (jump) {
                jump()
            }
        }

        override fun onSpringActivate(spring: Spring?) {
        }
    }
}