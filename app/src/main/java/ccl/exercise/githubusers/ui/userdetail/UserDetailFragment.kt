package ccl.exercise.githubusers.ui.userdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.fragment.findNavController
import ccl.exercise.githubusers.R
import ccl.exercise.githubusers.databinding.FragmentUserDetailBinding
import kotlinx.android.synthetic.main.fragment_user_detail.*
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class UserDetailFragment : Fragment() {

    companion object {
        const val STRING_NAME = "STRING_NAME"
    }

    private lateinit var viewModel: UserDetailViewModel
    private lateinit var binding: FragmentUserDetailBinding
    private var name: String? = null
    private val eventDelegate =object : UserDetailEventDelegate {
        override fun onClose() {
            findNavController().popBackStack()
        }
    }

    private val activityLifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun hideToolbar() {
            (activity as? AppCompatActivity)?.supportActionBar?.hide()
            activity?.lifecycle?.removeObserver(this)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(activityLifecycleObserver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(STRING_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        binding = FragmentUserDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            userDetailViewModel = viewModel
            userDetailEventDelegate = eventDelegate
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userNameText.text = name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showToolbar()
    }

    private fun initViewModel() {
        viewModel = get(parameters = { parametersOf(name) })
        with(viewModel) {
            error.observe(viewLifecycleOwner, Observer {
                Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            })
            fetchUserProfile()
        }
    }

    private fun showToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }
}