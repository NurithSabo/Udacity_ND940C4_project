package com.udacity.project4.locationreminders.reminderslist

import android.content.Intent
import android.os.Bundle
import android.text.BoringLayout.make
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar.make
import com.udacity.project4.R
import com.udacity.project4.authentication.AuthenticationActivity
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentRemindersBinding
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import com.udacity.project4.utils.setTitle
import com.udacity.project4.utils.setup
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : BaseFragment() {
    //use Koin to retrieve the ViewModel instance
    override val _viewModel: RemindersListViewModel by viewModel()
    private lateinit var binding: FragmentRemindersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_reminders, container, false
            )
        binding.viewModel = _viewModel

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(false)
        setTitle(getString(R.string.app_name))

        binding.refreshLayout.setOnRefreshListener { _viewModel.loadReminders() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
        binding.addReminderFAB.setOnClickListener {
            navigateToAddReminder()
        }
    }

    override fun onResume() {
        super.onResume()
        //load the reminders list on the ui
        _viewModel.loadReminders()
    }

    private fun navigateToAddReminder() {
        //use the navigationCommand live data to navigate between the fragments
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                ReminderListFragmentDirections.toSaveReminder()
            )
        )
    }

    private fun setupRecyclerView() {
        val adapter = RemindersListAdapter {
        }
//      setup the recycler view using the extension function
        binding.remindersRecyclerView.setup(adapter)
    }

    private fun navigateBackToLoginScreen() {
        val intent =
            Intent(activity, AuthenticationActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Toast.makeText(getActivity(),"You're logged out",Toast.LENGTH_SHORT).show()
        startActivity(intent)
        requireActivity().finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
//              _TODO: add the logout implementation
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        navigateBackToLoginScreen()
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
//        display logout as menu item
        inflater.inflate(R.menu.main_menu, menu)
    }

}