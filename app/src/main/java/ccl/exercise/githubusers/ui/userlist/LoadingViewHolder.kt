package ccl.exercise.githubusers.ui.userlist

import android.view.ViewGroup
import ccl.exercise.githubusers.R
import ccl.exercise.githubusers.ui.base.BaseViewHolder

class LoadingViewHolder(parent: ViewGroup) : BaseViewHolder<Unit>(parent, R.layout.view_holder_loading) {
    override fun onBind(item: Unit) = Unit
}