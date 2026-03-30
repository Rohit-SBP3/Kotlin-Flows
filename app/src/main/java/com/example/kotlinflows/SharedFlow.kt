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

    /*
      SharedFlow in Kotlin = a hot stream that broadcasts values to
      multiple collectors at the same time.

      🔥 What is SharedFlow?
         A part of Kotlin Coroutines Flow
         Used to emit values to multiple observers
         Works like a live event bus

         👉 If 3 collectors are listening → all 3 get the same data according to time of joining

       ⚡ Key characteristics
         ✅ Hot flow → starts emitting even if no one is collecting
         ✅ Multiple collectors supported
         ❌ Does NOT hold state by default
         ❌ New collectors don’t get old values (unless configured)

      */

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