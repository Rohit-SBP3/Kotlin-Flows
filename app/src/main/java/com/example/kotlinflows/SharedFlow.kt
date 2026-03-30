package com.example.kotlinflows

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import android.util.Log
import kotlinx.coroutines.flow.SharedFlow

class SharedFlow{

    // Shared Flow are Hot Stream (2nd consumer will not get from starting of stream)
    // replay buffer the last values.

    fun consumeSharedFlow(){
        val job = mutableSharedFlowProducer()
        GlobalScope.launch {
            job.collect {
                Log.d("SharedFlow",it.toString())
            }
        }
    }

    fun mutableSharedFlowProducer(): Flow<Int> {
        val mutableSharedFlow = MutableSharedFlow<Int>(replay = 2)
        GlobalScope.launch(Dispatchers.IO) {
            val list = listOf<Int>(1, 2, 3, 4, 5)
            list.forEach {
                mutableSharedFlow.emit(it)
                delay(1000)
            }
        }
        return mutableSharedFlow
    }

}