package ccl.exercise.githubusers.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ccl.exercise.githubusers.R
import ccl.exercise.githubusers.ui.userlist.UserListViewModel.Companion.LOAD_MORE_THRESHOLD
import kotlinx.android.synthetic.main.user_list_fragment.*
import org.koin.android.ext.android.inject

class UserListFragment : Fragment() {

    private val userListAdapter = UserListAdapter()
    private val viewModel: UserListViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        TODO enable navigation onItemClicked
//        navToUserDetailText.setOnClickListener {
//            findNavController(this).navigate(R.id.action_userListFragment_to_userDetailFragment)
//        }
        initRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.usersLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) return@Observer
            userListAdapter.updateUsers(it)
            userListAdapter.notifyDataSetChanged()
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            userListAdapter.updateLoadingStatus(it)
            userListAdapter.notifyDataSetChanged()
        })
        viewModel.fetchUsers()
    }

    private fun initRecyclerView() {
        usersRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (viewModel.isLoading.value == true) {
                        return
                    }
                    val itemNotViewed =
                        linearLayoutManager.itemCount - linearLayoutManager.findLastVisibleItemPosition()
                    if (itemNotViewed <= LOAD_MORE_THRESHOLD) {
                        viewModel.fetchUsers()
                    }
                }
            }
            adapter = userListAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addOnScrollListener(scrollListener)
        }
    }
}