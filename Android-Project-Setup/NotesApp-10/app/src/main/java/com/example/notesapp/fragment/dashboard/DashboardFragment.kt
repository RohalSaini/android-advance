package com.example.notesapp.fragment.dashboard

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import kotlinx.coroutines.flow.collect

class DashboardFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: FragmentDashboardBinding
    lateinit var modal: DashBoardViewModal
    lateinit var user: Data
    lateinit var adapter: RecylerViewAdapter
    var todoList: MutableList<TodoData> = mutableListOf()
    lateinit var session: Session
    var currentItem: Int = 0
    var totalItem: Int = 0
    var Scrollout: Int = 0
    var isScroll = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modal = ViewModelProvider(this).get(DashBoardViewModal::class.java)
        session = Session(requireContext())
        user = arguments?.getParcelable("user")!!
        session.setDataFromSharedPreferences(list = mutableListOf<TodoData>())
        binding.navView.setNavigationItemSelectedListener(this)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.right_side_menu).text = user.username
        binding.floatAddTodo.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("user", user)
            var fragment = AddTodoFragment()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack("dashboardfragment")
            transaction.commit()
        }
        binding.swipeRefreshlayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            refreshList()
            binding.swipeRefreshlayout.isRefreshing = false
        })
        initRecyclerView()
    }

    private fun refreshList() {
        try {
            modal.viewModelScope.launch(Dispatchers.Main) {
                user.limit =7
                user.skip = 0
                var response: Either<Exception, Todo> = TodoRepository().postGetAllJobs(obj = user)
                when (response) {
                    is Either.Left -> {
                        val failure: Exception = response.a
                        Log.d("TAG", failure.toString())

                    }
                    is Either.Right -> {
                        val todo = response.b
                        todo.data?.let {
                            session.setDataFromSharedPreferences(it)
                            todoList = session.getDataFromSharedPreferences()
                            adapter.setData(session.getDataFromSharedPreferences())
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            Log.d("TAG", exception.message.toString())
        }
    }
    fun fetchMoreData() {
        try {
            modal.viewModelScope.launch(Dispatchers.Main) {
                user.limit =5
                user.skip = todoList.size
                if(todoList.size!=0) {
                    var response: Either<Exception, Todo> = TodoRepository().postGetAllJobs(obj = user)
                    when (response) {
                        is Either.Left -> {
                            val failure: Exception = response.a
                            Log.d("TAG", failure.toString())

                        }
                        is Either.Right -> {
                            val todo = response.b
                            todo.data?.let {
                                for(item in it) {
                                    todoList.add(item)
                                }
                                adapter.setData(todoList)
                            }
                        }
                    }
                }

            }
        } catch (exception: Exception) {
            Log.d("TAG", exception.message.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("TAG", "it is clicked")
        when (item.itemId) {
            binding.navView.menu.getItem(0).itemId ->
                Snackbar.make(binding.root, "Main View is called", Snackbar.LENGTH_SHORT).show()
            binding.navView.menu.getItem(1).itemId -> logout(requireActivity())
        }
        return true
    }

    private fun logout(requireActivity: FragmentActivity) {
        AlertDialog.Builder(requireActivity)
            .setTitle("Logout")
            .setMessage("Are you sure you want to LogOut !!")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                var session = Session(requireContext())
                session.setLoggedIn(
                    loggedIn = false,
                    email = " ",
                    password = "",
                    username = "",
                    id = ""
                )
                session.removeAll()
                requireActivity().finish()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
            }).show()

    }

    fun initRecyclerView() {
        val manager= GridLayoutManager(context,1)
        pullFromBottomToFetchData(manager = manager)
        binding.emptyRecylerView.visibility = View.INVISIBLE
        adapter = RecylerViewAdapter(listener = object : RecylerViewAdapter.OnClickListener {
            override fun onDelete(todo: TodoData) {
                Log.d("DEBUG", "onDelete is called")
            }

            override fun onUpdate(todo: TodoData): TodoData {
                Log.d("DEBUG", "onUpdate is called")
                return todo
            }

        })
        binding.baseAdpater.layoutManager = manager
        binding.baseAdpater.adapter = adapter
        adapter.setData(todoList)
    }

    private fun pullFromBottomToFetchData(manager: GridLayoutManager) {
        binding.baseAdpater.addOnScrollListener(object : RecyclerView.OnScrollListener () {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScroll=true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItem=manager.childCount
                totalItem=manager.itemCount
                Scrollout=manager.findFirstVisibleItemPosition()
                if(isScroll && (currentItem+Scrollout==totalItem)) {
                    // progressBar.visibility=View.VISIBLE
                    isScroll=false
                   fetchMoreData()
                }
            }
        })
    }
}


/*
1. diffUitl
2. pagong 2 ,2,2,2
3. swiperefrshlayout start

4. update
5. deletion
 */