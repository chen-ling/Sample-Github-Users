package ccl.exercise.githubusers.ui.userlist

import ccl.exercise.githubusers.databinding.ViewHolderUserBinding
import ccl.exercise.githubusers.model.User
import ccl.exercise.githubusers.ui.base.BaseViewHolder

class UserViewHolder(
    private val binding: ViewHolderUserBinding,
    private val userEventDelegate: UserEventDelegate
) : BaseViewHolder<User>(binding.root) {
    init {
        binding.root.setOnClickListener {
            val name = binding.user?.loginName ?: return@setOnClickListener
            userEventDelegate.onClickUser(name)
        }
    }

    override fun onBind(item: User) {
        binding.user = item
        binding.executePendingBindings()
    }
}

interface UserEventDelegate {
    fun onClickUser(name: String)
//    fun onClickTag(name: String)
}