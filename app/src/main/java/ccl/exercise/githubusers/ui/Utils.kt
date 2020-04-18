package ccl.exercise.githubusers.ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import ccl.exercise.githubusers.image.GlideApp
import com.airbnb.lottie.LottieAnimationView


@BindingAdapter("visibleOnTrue")
fun displayViewOnTrue(view: View, shouldDisplay: Boolean) {
    view.visibility = if (shouldDisplay) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOnTextNotEmpty")
fun displayOnTextNotEmpty(view: View, text: String?) {
    view.visibility = if (text?.isNotEmpty() == true) View.VISIBLE else View.GONE
}

@BindingAdapter("circleImage")
fun ImageView.setCircleImage(imageUrl: String?) {
    GlideApp.with(context).apply {
        clear(this@setCircleImage)
        if (imageUrl?.isNotEmpty() == true) {
            load(imageUrl)
                .circleCrop()
                .into(this@setCircleImage)
        }
    }
}

@BindingAdapter("animationOnTrue")
fun LottieAnimationView.displayAnimationOnTrue(shouldDisplay: Boolean) {
    if (shouldDisplay && !isAnimating) {
        playAnimation()
    }
    if (!shouldDisplay) {
        cancelAnimation()
        visibility = View.GONE
    }
}