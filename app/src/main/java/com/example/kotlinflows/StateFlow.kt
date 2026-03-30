package com.example.kotlinflows

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StateFlow{

    // StateFlow = a hot flow that always holds and emits the latest state.
   /* 🔥 What is StateFlow?
    A part of Kotlin Coroutines Flow
    Used to store and observe state
    Always has a current value

    👉 Think: “observable variable”

    ⚡ Key properties
      ✅ Hot flow (always active)
      ✅ Always has a value
      ✅ Emits latest value to new collectors
      ✅ State holder

      🆚 StateFlow vs SharedFlow (real difference)
      Feature	        StateFlow	SharedFlow
      Holds value	    ✅ Yes	    ❌ No (by default)
      Default replay	1	        0
      Use case	        UI state	Events
      Initial value	    Required	Not required
    */

    // Even after the consumer came after the streaming he will
    // get the last value stored by the state.

    fun stateFlowConsumer(){
        val job = StateFlowProducer()
        GlobalScope.launch {
            job.collect {
                Log.d("State Flow",it.toString())
            }
            Log.d("State Flow",job.value.toString())
        }
    }

    fun StateFlowProducer(): StateFlow<Char>{
        val stateFlow = MutableStateFlow('Q')
        GlobalScope.launch {
            val list = listOf<Char>('A','B','C','D','E')
            list.forEach {
                delay(2000)
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }

    /*

    ⚔️ Side-by-side comparison
    Feature	             LiveData	               StateFlow
    Type	             Android component	       Kotlin Flow
    Lifecycle aware	     ✅ Yes (auto handles)	   ❌ No (you handle it)
    Initial value	     ❌ Not required	       ✅ Required
    Null handling	     Allows null	           Needs explicit handling
    Threading	         Main thread mostly	       Works with coroutines
    Cold/Hot	         Hot	                   Hot
    Latest value	     ✅ Yes	                   ✅ Yes
    Coroutines support	 ❌ Limited	               ✅ Native
    Boilerplate	Less	 Slightly                  more

    */


}