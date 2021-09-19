package com.example.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.demo.databinding.ActivityRegistrationBinding
import com.example.demo.modal.User
import com.example.demo.room.UserSchema
import com.example.demo.room.UserViewModal

class RegistrationActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityRegistrationBinding
    lateinit var radioButton_gender: RadioButton
    lateinit var countries:Array<String>
    lateinit var city:String
    private val TAG = "RegistrationActivity"

    lateinit var viewModal:UserViewModal


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModal = ViewModelProvider(this)[UserViewModal::class.java]
        countries = resources.getStringArray(R.array.cities_array)
        binding.editextCity.onItemSelectedListener= this

        ArrayAdapter(this,android.R.layout.simple_spinner_item,countries)
            .also {
                    adapter ->  binding.editextCity.adapter = adapter
            }

        binding.buttonLogin.setOnClickListener {
            if(ValidationChecking()) {
                var gender= ""
                var updates = "No"
                if(binding.checkboxUpdates.isChecked) {
                    updates = "Yes"
                }
                val selectedId = binding.radioGroup.checkedRadioButtonId
                radioButton_gender = findViewById(selectedId)
                if (selectedId == -1) {
                    Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show()
                } else {
                    gender = radioButton_gender.text.toString()
                }
                println(" Name : ${binding.editextUsername.text}  \n Password: ${binding.editextPasssword.text}  \n Email : ${binding.editextEmailAddress.text}  \n City: $city \n want updates: $updates \n Gender: $gender")
                Toast.makeText(this," Name : ${binding.editextUsername.text}  \n Password: ${binding.editextPasssword.text}  \n Email : ${binding.editextEmailAddress.text}  \n City: $city \n want updates: $updates \n Gender: $gender",Toast.LENGTH_LONG).show()

                var obj = UserSchema(
                    username = binding.editextUsername.text.trim().toString(),
                    password = binding.editextPasssword.text.trim().toString(),
                    email = binding.editextEmailAddress.text.trim().toString())
                InsertIntoDatabase(obj= obj)
            }
        }

        binding.editextUsername.setOnClickListener {
            binding.editextPasssword.requestFocus()
        }
        binding.editextPasssword.setOnClickListener {
            binding.editextEmailAddress.requestFocus()
        }
        binding.editextEmailAddress.setOnClickListener {
            hideKeyboard(it)
            binding.editextCity.requestFocus()
        }
        binding.checkboxUpdates.setOnClickListener {
            binding.radioMale.requestFocus()
        }
        binding.radioMale.setOnClickListener {
            binding.buttonLogin.requestFocus()
        }
        binding.radioFemale.setOnClickListener {
            binding.buttonLogin.requestFocus()
        }
    }

    private fun InsertIntoDatabase(obj: UserSchema) {
        try {
          viewModal.addUser(obj)

        }catch (e:Exception) {
            Log.e(TAG,e.message.toString())
        }
        callingToAvtivity()
    }


    private fun callingToAvtivity() {
        var intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        this.city = countries[position]
        binding.checkboxUpdates.requestFocus()
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
        this.city= countries[0]
        binding.checkboxUpdates.requestFocus()
    }

    private fun hideKeyboard(v: View) {
        val inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }
    private fun ValidationChecking() :Boolean{
        if (binding.editextUsername.text.trim().isEmpty() || binding.editextUsername.text.trim().length<5) {
            Toast.makeText(this,"Enter username with minimum 5 character",Toast.LENGTH_LONG).show()
            return  false;
        }
        if(binding.editextPasssword.text.trim().isBlank() || binding.editextPasssword.text.isNullOrEmpty() ) {
            Toast.makeText(this,"Enter Password ",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.editextEmailAddress.text.trim().isBlank() || binding.editextEmailAddress.text.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.editextEmailAddress.text.trim()).matches()) {
            Toast.makeText(this,"Enter valid email Adreess ",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun inputCheck(firstName:String,lastName:String,age:Editable) :Boolean{
        // to convert to Int Integer.parseInt(age.toString())
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty() )
    }
}