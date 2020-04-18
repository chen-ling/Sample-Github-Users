package ccl.exercise.githubusers.ui.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import ccl.exercise.githubusers.databinding.ViewHolderUserBinding
import ccl.exercise.githubusers.model.User
import ccl.exercise.githubusers.ui.base.BaseRecyclerViewAdapter
import ccl.exercise.githubusers.ui.base.BaseViewHolder


class UserListAdapter(private val userEventDelegate: UserEventDelegate) :
    BaseRecyclerViewAdapter<User>() {
    companion object {
        const val ITEM_USER = 0
        const val ITEM_LOADING = 1
    }

    private var isLoading: Boolean = false

    override fun getItemCount(): Int = if (isLoading) items.size + 1 else items.size

    override fun getItemViewType(position: Int): Int = when {
        isLoadingItem(position) -> ITEM_LOADING
        else -> ITEM_USER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            ITEM_USER -> {
                val binding: ViewHolderUserBinding =
                    ViewHolderUserBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                UserViewHolder(binding, userEventDelegate)
            }
            else -> LoadingViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (isLoadingItem(position)) {
            return
        }
        val userViewHolder = holder as? UserViewHolder ?: return
        userViewHolder.onBind(items[position])
    }

    fun updateUsers(users: List<User>) {
        when {
            items.isEmpty() -> setItems(users)
            else -> appendItems(users.subList(items.size, users.size))
        }
    }

    fun updateLoadingStatus(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    private fun isLoadingItem(position: Int) = isLoading && position == itemCount - 1
}
