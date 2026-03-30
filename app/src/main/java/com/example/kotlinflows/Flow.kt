package com.example.kotlinflows

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


class FlowEX {

    fun consumeFlow() {
        // Single try catch can work for both emitter and collector but
        // if we want to segregate errors then we can use
        try {
            val job = producer()
            GlobalScope.launch(Dispatchers.Main) {
                // above part on the thread mentioned in the flowon
                job
                    .map {
                        delay(1000)
                        it * 2
                        Log.d("Flow Class", "Map Thread:- ${Thread.currentThread().name}")
                    }
                    .flowOn(Dispatchers.Main)
                    .filter {
                        delay(500)
                        Log.d("Flow Class", "Filter Thread:- ${Thread.currentThread().name}")
                        it > 8
                    }
                    // above part on the thread mentioned in the flowon
                    .flowOn(Dispatchers.IO)
                    // below part mentioned in the global coroutine scope
                    .collect {
                        Log.d("Flow Class", "Collector Thread: $it  ${Thread.currentThread().name}")
                        //throw Exception("An error occurred in collector")
                    }
            }
        }catch (e: Exception){
            Log.d("Exception Occurred",e.message.toString())
        }
    }

    // Flows preserve their coroutine context, they except the emittion
    // and collection on the same thread, but if you want to use diff thread contexts then
    // you have to explicitly tell the flow about it using flowon
    private fun producer(): Flow<Int> {
        return flow<Int> {
            /*withContext(Dispatchers.IO){*/
                val list = listOf<Int>(1, 2, 3, 4, 5, 6);
                list.forEach {
                    delay(1000)
                    throw Exception("An Error Occurred on Api")
                    Log.d("Flow Class", "Emitter Thread:- ${Thread.currentThread().name}")
                    emit(it)
                }
            /*}*/
        }.catch {
            // we can catch the exceptions here and also use callback elements
            emit(-100)
            Log.d("Flow Class", "Emitter Thread Exception:- ${it.message}")
        }
    }

}