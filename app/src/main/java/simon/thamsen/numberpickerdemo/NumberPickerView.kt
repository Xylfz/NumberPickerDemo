package simon.thamsen.numberpickerdemo

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import simon.thamsen.numberpickerdemo.databinding.NumberPickerViewBinding
import kotlin.math.abs

class NumberPickerView : LinearLayout {

    private lateinit var binding: NumberPickerViewBinding
    private var popUpVisible = false
    private var px = convertDpToPixels(48f)
    private var margin = convertDpToPixels(24f)

    private var touchBarWidth = 0
    private var activeNum = ""

    private var numOne = Pair(0, 0)
    private var numTwo = Pair(0, 0)
    private var numThree = Pair(0, 0)
    private var numFour = Pair(0, 0)
    private var numFive = Pair(0, 0)
    private var numSix = Pair(0, 0)
    private var numSeven = Pair(0, 0)
    private var numEight = Pair(0, 0)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        binding = NumberPickerViewBinding.inflate(LayoutInflater.from(context), this, true)

        binding.touchBar.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.touchBar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                calculatePositions()
                setupTouchListener()
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        // Listen for touch events
        binding.touchBar.setOnTouchListener { _, event ->
            val x = event.x
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!popUpVisible) {
                        popUpVisible = true
                        binding.knob.text = ""
                        moveKnobs(x)
                        animatePopUp(binding.popUpKnob, true)
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    moveKnobs(x)
                    determineNumberOverlap(x.toInt())
                }
                MotionEvent.ACTION_UP -> {
                    if (popUpVisible) {
                        popUpVisible = false
                        animatePopUp(binding.popUpKnob, false)
                        binding.knob.text = activeNum
                    }
                }
            }
            true
        }
    }

    // Determine number positions
    private fun calculatePositions() {
        touchBarWidth = binding.touchBar.width
        val section = touchBarWidth / 8
        numOne = Pair(0, section)
        numTwo = Pair(section, section * 2)
        numThree = Pair(section * 2, section * 3)
        numFour = Pair(section * 3, section * 4)
        numFive = Pair(section * 4, section * 5)
        numSix = Pair(section * 5, section * 6)
        numSeven = Pair(section * 6, section * 7)
        numEight = Pair(section * 7, section * 8)
    }

    // Determine active number based on knob position
    private fun determineNumberOverlap(pos: Int) {
        when (pos) {
            in numOne.first..numOne.second -> {
                activeNum = "1"
                binding.popUpKnob.text = activeNum
            }
            in numTwo.first..numTwo.second -> {
                activeNum = "2"
                binding.popUpKnob.text = activeNum
            }
            in numThree.first..numThree.second -> {
                activeNum = "3"
                binding.popUpKnob.text = activeNum
            }
            in numFour.first..numFour.second -> {
                activeNum = "4"
                binding.popUpKnob.text = activeNum
            }
            in numFive.first..numFive.second -> {
                activeNum = "5"
                binding.popUpKnob.text = activeNum
            }
            in numSix.first..numSix.second -> {
                activeNum = "6"
                binding.popUpKnob.text = activeNum
            }
            in numSeven.first..numSeven.second -> {
                activeNum = "7"
                binding.popUpKnob.text = activeNum
            }
            in numEight.first..numEight.second -> {
                activeNum = "8"
                binding.popUpKnob.text = activeNum
            }
        }
    }

    // Update knob positions
    private fun moveKnobs(pos: Float) {
        if (pos > margin && pos < touchBarWidth - margin) {
            binding.popUpKnob.x = pos
            binding.knob.x = pos
        }
    }

    // Animate pop up button
    private fun animatePopUp(view: View, up: Boolean) {
        val distance = if (up) -abs(px) else 0f
        val effect = if (up) OvershootInterpolator() else null
        ObjectAnimator.ofFloat(view, "translationY", distance).apply {
            duration = 150
            interpolator = effect
            start()
        }
    }

    // Convert dp to pixels
    private fun convertDpToPixels(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        )
    }
}