package com.example.demo.Adapter

import android.util.Log
import com.example.demo.ActivityModule
import javax.inject.Inject

//class Car @Inject constructor(@ActivityModule.FirstName var firstName:String, @ActivityModule.LastName val lastNameString: String, var engine: Engine) {
//    fun getName() {
//        engine.getEngine()
//        Log.d("car","my name is $firstName $lastNameString")
//    }
//}

//class Engine @Inject constructor() :Company{
//    override fun getEngine() {
//        Log.d("engine","interface engine")
//    }
//}
//
//interface  Company {
//    fun getEngine()
//}

class Car @Inject constructor(@ActivityModule.FirstName var firstName:String, @ActivityModule.LastName var lastName:String, var engine:Engine){
    fun  getName() {
        engine.getEngineName()
        Log.d("car","my car name is $firstName $lastName")
    }
}

class Engine @Inject constructor(){
    fun getEngineName() {
        Log.d("car","my car engine name is 1235678")
    }
}