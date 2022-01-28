package com.raywenderlich.cinematic

import android.animation.Animator
import android.animation.ValueAnimator
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.transition.Transition
import androidx.transition.TransitionValues

class TextSizeTransition : Transition() {

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureTextSize(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureTextSize(transitionValues)
    }

    // captureStartValues takes one parameter: a TransitionValues object.
    // TransitionValues is just a container for a View and a HashMap of properties. You’ll
    // store the details you care about in this object.
    // This function is used to save the start values(the textSize of the TextView)
    // of the logo TextView and save them into that HashMap.
    private fun captureTextSize(transitionValues: TransitionValues) {
        (transitionValues.view as? TextView)?.let { textView ->
            transitionValues.values[textSizeProp] = textView.textSize
        }
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        // If either of this parameters is null, there’s no animation to run, so simply return.
        if (startValues == null || endValues == null) {
            return null
        }

        // Here, you pull out the start and end text sizes and declare your View. Since you know
        // that this Transition only works with TextViews, you can just cast the View held in
        // endValues to a TextView.
        val startSize = startValues.values[textSizeProp] as Float
        val endSize = endValues.values[textSizeProp] as Float
        val view = endValues.view as TextView

        // Using a ValueAnimator to animate between the start and end text sizes.
        // In the updateListener, we set the text sizes of your TextView.
        return ValueAnimator.ofFloat(startSize, endSize).apply {
            addUpdateListener {
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.animatedValue as Float)
            }
        }
    }

    companion object {
        private const val textSizeProp = "transition:textsize"
    }
}