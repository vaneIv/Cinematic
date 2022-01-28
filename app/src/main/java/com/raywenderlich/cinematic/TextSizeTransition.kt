package com.raywenderlich.cinematic

import android.animation.Animator
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

class TextSizeTransition : Transition() {

    override fun captureStartValues(transitionValues: TransitionValues) {
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {

        return null
    }

    companion object {
        private const val textSizeProp = "transition:textsize"
    }
}