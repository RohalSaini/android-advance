package com.example.notesapp.fragment.dashboard

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.databinding.FragmentDashboardBinding
import com.example.notesapp.fragment.dashboard.viewmodal.DashBoardViewModal
import com.example.notesapp.util.Session
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class DashboardFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener{
    lateinit var binding: FragmentDashboardBinding
    lateinit var modal:DashBoardViewModal
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modal = ViewModelProvider(this).get(DashBoardViewModal::class.java)
        binding.navView.setNavigationItemSelectedListener(this)
        var session:Session = Session(requireContext())
        Log.d("TAG","Email: ${session.getEmail()} Password: ${session.getPassword()} Username: ${session.getName()}")
        session.getEmail()
    }
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("TAG","it is clicked")
        when (item.itemId) {
            binding.navView.menu.getItem(0).itemId ->
                Snackbar.make(binding.root,"Main View is called", Snackbar.LENGTH_SHORT).show()
            binding.navView.menu.getItem(1).itemId -> logout(requireActivity())
        }
        return true
    }
    private fun logout(requireActivity: FragmentActivity) {
        AlertDialog.Builder(requireActivity)
            .setTitle("Logout")
            .setMessage("Are you sure you want to LogOut !!")
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, i ->
                var session = Session(requireContext())
                session.setLoggedIn(
                    loggedIn = false,
                    email = " ",
                    password = "",
                    username = "",
                    id = "")
                session.removeAll()
                requireActivity().finish()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialogInterface, _ ->
                dialogInterface.dismiss()
            }).show()

    }
}