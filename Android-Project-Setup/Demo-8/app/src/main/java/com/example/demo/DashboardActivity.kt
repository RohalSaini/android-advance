package com.example.demo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.demo.Adapter.RecylerViewAdpater
import com.google.android.material.navigation.NavigationView
import com.example.demo.databinding.ActivityDashboardBinding
import com.example.demo.room.TodoSchema
import com.example.demo.room.TodoViewModal
import com.example.demo.room.UserSchema
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Exception


class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val TAG = "DASHBOARD ACTIVITY"
    lateinit var binding:ActivityDashboardBinding
    lateinit var user:UserSchema
    lateinit var view:DrawerLayout
    var todoList:MutableList<TodoSchema> = mutableListOf()
    lateinit var adapter:RecylerViewAdpater
    lateinit var modal: TodoViewModal
    var mlist:MutableList<TodoSchema> = mutableListOf()
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         binding = ActivityDashboardBinding.inflate(layoutInflater)
         view = binding.root
         setContentView(view)

         // Getting data from parser
         user = intent.extras?.getParcelable<UserSchema>("obj")!!
         modal = ViewModelProvider(this)[TodoViewModal::class.java]
         // Printing User Detail on Navigation View
         modal.viewModelScope.launch {
                modal.getUserWithEmail(email = user.email,index = 0,limit = 2).observe(this@DashboardActivity, Observer {
                     mlist -> Log.d("User List",mlist.toString())
                      todoList = mlist.toMutableList()
                 val staggeredGridLayoutManager = StaggeredGridLayoutManager(1,LinearLayoutManager.VERTICAL)
                 binding.emptyRecylerView.visibility = View.INVISIBLE
                 adapter = RecylerViewAdpater(this@DashboardActivity,list = todoList,listener = object :RecylerViewAdpater.OnClickListener{
                     override fun onDelete(todo:TodoSchema) {
                         modal.deleteTodoByID(todo = todo)
                     }

                     override fun onUpdate(todo: TodoSchema): TodoSchema {
                         try {
                             modal.updateTodoByID(todo)
                         }catch (error:Exception) {
                             Log.e(TAG,error.message.toString())
                         }
                        return todo
                     }
                 })
                 binding.baseAdpater.layoutManager = staggeredGridLayoutManager
                 binding.baseAdpater.adapter = adapter
             })
         }
         binding.navView.getHeaderView(0).findViewById<TextView>(R.id.right_side_menu).text = user.username

         // navigation open on Click
         binding.navView.setNavigationItemSelectedListener(this)
         binding.floatAddTodo.setOnClickListener {
             startActivity(Intent(this, TodoAddActivity::class.java).putExtra("obj", user))
         }
         binding.layputToolbar.expandedMenu.setOnClickListener {
             openDrawer(binding.drawerLayout)
         }
         binding.swiperefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
             modal.viewModelScope.launch {
                 // todo 4 -> 1,2,3,4
                 modal.getUserWithEmail(email = user.email,index = todoList.size,limit = 2).observe(this@DashboardActivity, Observer {
                         mlist -> Log.d("User List",mlist.toString())
                         for(item in mlist) {
                             todoList.add(item)
                         }
                     adapter.notifyDataSetChanged()
                 })
             }
                binding.swiperefreshLayout.isRefreshing = false
         })
     }


    private fun closeDrawer(drawerLayout: DrawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

    private fun openDrawer(drawerLayout: DrawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START)
    }
    private fun logout(mainActivity: DashboardActivity) {
                        AlertDialog.Builder(mainActivity)
                            .setTitle("Logout")
                            .setMessage("Are you sure you want to LogOut !!")
                            .setPositiveButton("Yes",DialogInterface.OnClickListener {
                                    dialog, i ->
                                            mainActivity.finish()
                                            var session = Session(this)
                                                session.setLoggedIn(
                                                    loggedIn = false,
                                                    email = " ",
                                                    password = "",
                                                    username = "",
                                                    id = "")
                                                session.removeAll()
                                                finish()
                             })
                            .setNegativeButton("No",DialogInterface.OnClickListener {
                                    dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }).show()

    }

    override fun onPause() {
        super.onPause()
        closeDrawer(binding.drawerLayout)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            binding.navView.menu.getItem(0).itemId ->  Snackbar.make(view,"Main View is called",Snackbar.LENGTH_SHORT).show()
            binding.navView.menu.getItem(1).itemId -> logout(this)
        }
        return true
    }
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage("Do you want to Exit?")
            .setPositiveButton("Yes") { _,_ -> finishAffinity() }
            .setNegativeButton("No") { dialog,_ -> dialog.cancel() }
            .create().show()
    }
}
