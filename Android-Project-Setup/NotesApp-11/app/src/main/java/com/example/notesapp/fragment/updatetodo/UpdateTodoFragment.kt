package com.example.notesapp.fragment.updatetodo

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentDeleteTodoBinding
import com.example.notesapp.fragment.dashboard.DashboardFragment
import com.example.notesapp.fragment.updatetodo.viewmodal.UpdateViewModal
import com.example.notesapp.modal.Data
import com.example.notesapp.modal.TodoData
import com.example.notesapp.webserver.ApiStatus
import com.google.android.material.snackbar.Snackbar
import io.ktor.client.features.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UpdateTodoFragment : Fragment() {
    lateinit var user:Data
    lateinit var todo:TodoData
    lateinit var binding:FragmentDeleteTodoBinding
    lateinit var modal: UpdateViewModal
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentDeleteTodoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable("user")!!
        todo = arguments?.getParcelable("todo")!!
        modal = ViewModelProvider(this)[UpdateViewModal::class.java]
        binding.editextDescription.text = todo.description.toEditable()
        binding.editextJobName.text = todo.job.toEditable()
        binding.btnAdd.setOnClickListener {
            todo.description = binding.editextDescription.text.toString()
            todo.job = binding.editextJobName.text.toString()
            modal.postUpdateTodo(obj = todo )
            callToCoroutine()
        }
    }
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun callToCoroutine() {
        modal.viewModelScope.launch(Dispatchers.IO) {
            modal.apiStateFlow.collect {
                    status ->
                when(status) {
                    is ApiStatus.Error -> {
                        //binding.loader.visibility = View.INVISIBLE
                        Snackbar.make(binding.root, "Error: ${status.message}", Snackbar.LENGTH_LONG).show()
                        Log.e("Error from Registration Fragment",status.message)
                        modal.setValueToDefault()
                    }
                    is ApiStatus.Success -> {
                        Log.d("Success",status.data.toString())
                        //binding.loader.visibility = View.INVISIBLE
                        modal.setValueToDefault()
                        //modal.saveUserToSession(response = status.data,context = requireContext())
                        Snackbar.make(binding.root, "Success: ${status.data}", Snackbar.LENGTH_LONG).show()
                        val bundle = Bundle()
                        bundle.putParcelable("user",status.data.data)
                        //var fragment = DashboardFragment()
                        //fragment.arguments = bundle
                        //val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        //transaction.replace(R.id.container,fragment)
                        //transaction.commit()
                        requireActivity().onBackPressed()
                    }
                    is ApiStatus.NetworkError -> {
                        //binding.loader.visibility = View.INVISIBLE
                       // modal.setValueToDefault()
                        Snackbar.make(binding.root, "Error: ${status.message}", Snackbar.LENGTH_LONG).show()
                    }
                    is ApiStatus.Loading -> Unit
                    is ApiStatus.Empty -> Unit
                }
            }
        }
    }
}