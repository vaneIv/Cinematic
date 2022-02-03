package com.raywenderlich.cinematic.popular

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class MyItemAnimator : DefaultItemAnimator() {
    //  1. First, instead of extending from RecyclerView.ItemAnimator, you extend
    // DefaultItemAnimator(). That way, you don’t have to provide all the animations
    // for all types of data set changes. It saves you a lot of work and gives you the
    // ability to override only the animations you want to customize.
    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        if (holder != null) {
            //  2. Next, since you’ll be scaling items up, you set the root View‘s scaleX and scaleY
            // properties to 0f, so the items don’t appear on the screen.
            holder.itemView.scaleX = 0f
            holder.itemView.scaleY = 0f
            //  3. You then start the animation, scaling back up to 1f, or full item scale, for 1000
            // milliseconds, or one second.
            holder.itemView.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .start()

            // 4. Finally, you return true to tell the animator it needs to apply these animations.
            return true
        }
        return super.animateAdd(holder)
    }
}