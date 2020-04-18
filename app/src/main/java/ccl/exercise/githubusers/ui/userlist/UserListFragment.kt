package ccl.exercise.githubusers.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ccl.exercise.githubusers.R
import ccl.exercise.githubusers.ui.ItemSpacingDecoration
import ccl.exercise.githubusers.ui.userdetail.UserDetailFragment.Companion.STRING_NAME
import ccl.exercise.githubusers.ui.userlist.UserListViewModel.Companion.LOAD_MORE_THRESHOLD
import kotlinx.android.synthetic.main.fragment_user_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {
    private val viewModel by viewModel<UserListViewModel>()
    private var userListAdapter: UserListAdapter? = null
    private val eventDelegate = object : UserEventDelegate {
        override fun onClickUser(name: String) {
            val bundle = Bundle().apply { putString(STRING_NAME, name) }
            findNavController(this@UserListFragment).navigate(
                R.id.action_userListFragment_to_userDetailFragment,
                bundle
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
        with(viewModel) {
            usersLiveData.observe(viewLifecycleOwner, Observer {
                if (it.isEmpty()) return@Observer
                userListAdapter?.updateUsers(it)
                userListAdapter?.notifyDataSetChanged()
            })
            error.observe(viewLifecycleOwner, Observer {
                it ?: return@Observer
                Toast.makeText(
                    context,
                    getString(if (it is UnsupportedOperationException) R.string.reached_limit else R.string.something_went_wrong),
                    Toast.LENGTH_SHORT
                ).show()
                userListAdapter?.updateLoadingStatus(false)
                userListAdapter?.notifyItemRemoved(userListAdapter!!.itemCount - 1)
                error.value = null
            })
            isLoading.observe(viewLifecycleOwner, Observer {
                userListAdapter?.updateLoadingStatus(it)
                userListAdapter?.notifyDataSetChanged()
            })
            fetchUsers()
        }
    }

    private fun initRecyclerView() {
        userListAdapter = UserListAdapter(eventDelegate)
        usersRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (viewModel.isLoading.value == true || viewModel.reachFetchLimit()) {
                        return
                    }
                    val itemNotViewed =
                        linearLayoutManager.itemCount - linearLayoutManager.findLastVisibleItemPosition()
                    if (itemNotViewed <= LOAD_MORE_THRESHOLD) {
                        viewModel.loadMore()
                    }
                }
            }
            adapter = userListAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addOnScrollListener(scrollListener)
            addItemDecoration(ItemSpacingDecoration(R.dimen.dp_10, R.dimen.dp_5))
        }
    }
}