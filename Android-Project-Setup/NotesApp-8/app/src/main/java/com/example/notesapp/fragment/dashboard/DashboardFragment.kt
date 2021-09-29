package com.example.notesapp.fragment.dashboard

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.R
import com.example.notesapp.Repository.Todo.TodoRepository
import com.example.notesapp.Repository.User.Either
import com.example.notesapp.databinding.FragmentDashboardBinding
import com.example.notesapp.fragment.addtodo.AddTodoFragment
import com.example.notesapp.fragment.dashboard.recyclerview.RecylerViewAdapter
import com.example.notesapp.fragment.dashboard.viewmodal.DashBoardViewModal
import com.example.notesapp.modal.Data
import com.example.notesapp.modal.Todo
import com.example.notesapp.modal.TodoData
import com.example.notesapp.util.Session
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class DashboardFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener{
    lateinit var binding: FragmentDashboardBinding
    lateinit var modal:DashBoardViewModal
    lateinit var user:Data
    lateinit var adapter:RecylerViewAdapter
    var todoList:MutableList<TodoData> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modal = ViewModelProvider(this).get(DashBoardViewModal::class.java)
        binding.navView.setNavigationItemSelectedListener(this)
        user= arguments?.getParcelable("user")!!
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.right_side_menu).text = user.username
        collectingDataFromFlow()
        settingDataIntoRecyclerView(todoList)
        binding.floatAddTodo.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("user",user)
            var fragment = AddTodoFragment()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack("dashboardfragment")
            transaction.commit()
        }

    }

    private fun collectingDataFromFlow() {
        try {
            modal.viewModelScope.launch(Dispatchers.Main) {
                delay(4000)
                var response: Either<Exception, Todo> = TodoRepository().postGetAllJobs(obj = user)
                when (response) {
                    is Either.Left -> {
                        val failure: Exception =response.a
                       // Snackbar.make(binding.root,failure.toString(),Snackbar.LENGTH_LONG).show()
                        Log.d("TAG",failure.toString())

                    }
                    is Either.Right -> {
                        val todo = response.b
                        Log.d("TAG",todo.data.toString())
                        todo.data?.let { refreshRecyclerView(it) }
                        //Snackbar.make(binding.root,countries.toString(),Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }catch (exception: Exception) {
            Log.d("TAG",exception.message.toString())
        }
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
    fun settingDataIntoRecyclerView(todoList: MutableList<TodoData>) {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        binding.emptyRecylerView.visibility = View.INVISIBLE
        adapter = RecylerViewAdapter(requireContext(),list = todoList,listener = object : RecylerViewAdapter.OnClickListener {
            override fun onDelete(todo: TodoData) {
                TODO("Not yet implemented")
            }

            override fun onUpdate(todo: TodoData): TodoData {
                TODO("Not yet implemented")
            }

        })
        binding.baseAdpater.layoutManager = staggeredGridLayoutManager
        binding.baseAdpater.adapter = adapter
    }
    fun refreshRecyclerView(todolist:MutableList<TodoData>) {
        for (job in todolist) {
            todoList.add(job)
        }
        adapter.notifyDataSetChanged()
    }
}

/*
1. diffUitl
2. pagong 2 ,2,2,2
3. swiperefrshlayout start

4. update
5. deletion
 */