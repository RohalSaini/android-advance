package com.example.notesapp.fragment.addtodo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentAddTodoBinding
import com.example.notesapp.databinding.FragmentDashboardBinding
import com.example.notesapp.fragment.addtodo.viewmodal.AddTodoViewModal
import com.example.notesapp.modal.Data
import com.example.notesapp.modal.TodoData
import com.example.notesapp.webserver.ApiStatus
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AddTodoFragment : Fragment() {
    private lateinit var binding:  FragmentAddTodoBinding
    private lateinit var modal :AddTodoViewModal
    private lateinit var user:Data
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View? {
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modal = ViewModelProvider(this).get(AddTodoViewModal::class.java)
        user= arguments?.getParcelable("user")!!
        binding.btnAdd.setOnClickListener {
            var job:String = binding.editextJobName.text.trim().toString()
            var description:String = binding.editextDescription.text.trim().toString()
            var obj = TodoData(_id = "0",job=job,description=description,email = user.email )
            modal.postAddJob(todo= obj)
        }
        collectingDataFromFlow()
    }

    private fun collectingDataFromFlow() {
        modal.viewModelScope.launch(Dispatchers.IO) {
            modal.apiStateFlow.collect {
                    status ->
                when(status) {
                    is ApiStatus.Error -> {
                        Snackbar.make(binding.root, "Error: ${status.message}", Snackbar.LENGTH_LONG).show()
                        Log.e("Error from Registration Fragment",status.message)
                        modal.setStateFlowToDefault()
                    }
                    is ApiStatus.Success -> {
                        Log.d("Success",status.data.toString())
                        Snackbar.make(binding.root, "Success: ${status.data}", Snackbar.LENGTH_LONG).show()
                        modal.setStateFlowToDefault()
                        this@AddTodoFragment.requireActivity().finish()
                    }
                    is ApiStatus.NetworkError -> {
                        modal.setStateFlowToDefault()
                        Snackbar.make(binding.root, "Error: ${status.message}", Snackbar.LENGTH_LONG).show()
                    }
                    is ApiStatus.Loading -> Unit
                    is ApiStatus.Empty -> Unit
                }
            }
        }
    }

}