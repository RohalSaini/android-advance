package com.example.demo


import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton
import com.example.demo.Adapter.Car as Car1

/* SingletonComponent  -> @Singleton ->Application Level -> (allows : inject -> activity,fragment,viewModal,view)*/
/* ActivityComponent  ->  @ActivityScoped  ->Activity Level -> (allows : inject -> activity)*/
/* FragmentComponent  ->  @FragmentScoped  ->fragment Level -> (allows : inject -> fragment)*/
/* ServiceComponent  ->  @ServiceScoped  -> Service Level -> (allows : inject -> service)*/
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named("string1")
    fun provideString() = "this is a dagger injection "

    @Singleton
    @Provides
    @Named("session")
    fun getSessionObj(
        @ApplicationContext context:Context):Session = Session(context)
}

@Module
@InstallIn(ActivityComponent::class)
object  ActivityModule {
    @ActivityScoped
    @Provides
    @Named("activity")
    fun getActivity() = " it allow only in activity"

    @ActivityScoped
    @Provides
    @Named("app_name")
    fun getAppName(
        @ApplicationContext context: Context,
        @Named("activity") str:String,
        @Named("string1") str2:String) : String = "${context.getString(R.string.app_name)}  $str $str2"

    @Provides
    @FirstName
    fun firstName() :String = "Android"


    @Provides
    @LastName
    fun lastName() :String = "Studio"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FirstName

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LastName
}
