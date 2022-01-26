package com.raywenderlich.cinematic.details

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.raywenderlich.cinematic.R
import com.raywenderlich.cinematic.databinding.ViewFavoriteButtonBinding
import com.raywenderlich.cinematic.util.DisplayMetricsUtil

class FavoriteButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val binding: ViewFavoriteButtonBinding =
        ViewFavoriteButtonBinding.inflate(LayoutInflater.from(context), this)

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val padding = DisplayMetricsUtil.dpToPx(16)
        setPadding(padding, 0, padding, padding)
    }

    fun setOnFavoriteClickListener(listener: () -> Unit) {
        binding.favoriteButton.setOnClickListener {
            listener.invoke()
        }
    }

    fun setFavorite(isFavorite: Boolean) {
        binding.favoriteButton.apply {
            icon = if (isFavorite) {
                AppCompatResources.getDrawable(context, R.drawable.ic_baseline_favorite_24)
            } else {
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_baseline_favorite_border_24
                )
            }
            text = if (isFavorite) {
                context.getString(R.string.remove_from_favorites)
            } else {
                context.getString(R.string.add_to_favorites)
            }
        }

        hideProgress()
    }

    fun showProgress() {
        binding.progressBar.isVisible = true
        binding.favoriteButton.apply {
            icon = null
            text = null

            isClickable = false
            isFocusable = false
        }

        // animate button
        animateButton()
    }

    private fun animateButton() {
        // Setting the initialWidth to the measured width of the button and thefinalWidth
        // to the measured height. You want the button to animate from its initial width to
        // a final state where it becomes a circle. To convert a rectangle to a square, you
        // need to make the width and height the same. By that same logic, since the button
        // already has rounded corners, making the width and height the same makes it a
        // circle.
        val initialWidth = binding.favoriteButton.measuredWidth
        val finalWidth = binding.favoriteButton.measuredHeight

        // Instantiate a ValueAnimator using the static ofInt, then pass the intialWidth
        // and finalWidth to it.
        val widthAnimator = ValueAnimator.ofInt(
            initialWidth,
            finalWidth
        )

        // Assigning a 1,000 millisecond duration to the animator.
        widthAnimator.duration = 1000

        // Adding an updateListener to the animator and assign the animatedValue as the
        // width of the button.
        widthAnimator.addUpdateListener {
            binding.favoriteButton.updateLayoutParams {
                this.width = it.animatedValue as Int
            }
        }

        // Starting the animation.
        widthAnimator.start()
    }

    private fun hideProgress() {
        binding.progressBar.isVisible = false
        binding.favoriteButton.apply {
            extend()
            isClickable = true
            isFocusable = true
        }

        //TODO reverse the animations
    }

}