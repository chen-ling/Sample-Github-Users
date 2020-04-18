package ccl.exercise.githubusers.ui

import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("visibleOnTrue")
fun displayViewOnTrue(view: View, shouldDisplay: Boolean) {
    view.visibility = if (shouldDisplay) View.VISIBLE else View.GONE
}