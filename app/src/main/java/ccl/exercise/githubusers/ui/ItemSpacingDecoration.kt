package ccl.exercise.githubusers.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class ItemSpacingDecoration(
    private val spaceResId: Int,
    private val leadingSpaceResId: Int? = null,
    private val trailingSpaceResId: Int? = null,
    private val vertical: Boolean = true
) : RecyclerView.ItemDecoration() {

    private var space: Int? = null
    private var leadingSpace: Int = 0
    private var trailingSpace: Int = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (space == null) {
            view.context.resources.apply {
                space = getDimensionPixelSize(spaceResId)
                leadingSpaceResId?.let { leadingSpace = getDimensionPixelSize(it) }
                trailingSpaceResId?.let { trailingSpace = getDimensionPixelSize(it) }
            }
        }
        val position = parent.getChildAdapterPosition(view)
        val isLast = position == state.itemCount - 1
        val isFirst = position == 0

        if (vertical) {
            outRect.bottom = if (isLast) trailingSpace else space!!
            if (isFirst) {
                outRect.top = leadingSpace
            }
        } else {
            outRect.right = if (isLast) trailingSpace else space!!
            if (isFirst) {
                outRect.left = leadingSpace
            }
        }
    }
}
