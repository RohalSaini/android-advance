package com.example.demo.Adapter

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Qualifier

class Main @Inject constructor(
    @com.example.demo.Adapter.Module.FirstName var firstName:String,
    @com.example.demo.Adapter.Module.LastName var lastName:String){
 fun getName() {
    Log.d("main","my name is $firstName $lastName")
 }
}

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @FirstName
    fun firstName() :String = "Rohal"

    @LastName
    @Provides
    fun lastName() :String = "Saini"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FirstName

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LastName

}