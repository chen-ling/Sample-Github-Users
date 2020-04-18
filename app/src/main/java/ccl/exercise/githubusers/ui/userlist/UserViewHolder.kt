package ccl.exercise.githubusers.ui.userlist

import ccl.exercise.githubusers.databinding.ViewHolderUserBinding
import ccl.exercise.githubusers.image.GlideApp
import ccl.exercise.githubusers.model.User
import ccl.exercise.githubusers.ui.base.BaseViewHolder

class UserViewHolder(
    private val binding: ViewHolderUserBinding
) : BaseViewHolder<User>(binding.root) {

    override fun onBind(item: User) {
        binding.user = item
        binding.executePendingBindings()
        GlideApp.with(binding.root.context).apply {
            clear(binding.avatarImage)
            if (item.avatarUrl?.isNotEmpty() == true) {
                load(item.avatarUrl)
                    .circleCrop()
                    .into(binding.avatarImage)
            }
        }

    }
}