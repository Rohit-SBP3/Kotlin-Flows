package com.example.kotlinflows

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kotlinflows.FlowEX
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinflows.ui.theme.KotlinFlowsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

/*

Kotlin has asynchronous stream support using channels and flows.
1. Channel (Send and receive).
🔥 Hot → works even without receiver
🧵 Used for communication between coroutines
⚙️ More control (buffer, capacity, etc.)
❗ Manual handling (close, errors, etc.)
🧨 Easy to mess up if not careful

2. Flow (Emit and Collect).
❄️ Cold → nothing happens until collect()
🔄 Automatically handles lifecycle
🧼 Cleaner & easier to use
🧵 Built on coroutines
📦 Supports operators like map, filter, debounce
It has two type of operators
1. Terminal operators - with suspend functions
2. Non-Terminal operators - without suspend function returns flow
   ex. map, filter
 */

class MainActivity : ComponentActivity() {

    val channel = Channel<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KotlinFlowsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        val fl = FlowEX()
        fl.consumeFlow()

        /*producer()
        consumer()*/

        // consuming flow
        /*val job = GlobalScope.launch {

            val data: Flow<Int> = producer()
            // Buffer
            data.buffer(2).map {
                it * 2
            }.filter {
                it < 8
            }.collect {
                Log.d("flow1",it.toString())
            }

            val res = data.toList()
            Log.d("flow1",res.toString())
        }*/

        /*GlobalScope.launch(Dispatchers.Main) {
            producer()
                .onStart {
                    Log.d("flow2","Flow begin")
                    // manually
                    emit(-1)
                }
                .onCompletion {
                    Log.d("flow2","Flow ended")
                    emit(-2)
                }
                .onEach {
                    Log.d("flow2","Dracareys - $it")
                }
                .collect {
                    Log.d("flow2",it.toString())
                }
        }*/

        // canceling the job so there is no receiver to listen to emitter.
        /*GlobalScope.launch {
            delay(4500)
            job.cancel()
        }

        GlobalScope.launch(Dispatchers.IO) {
            getNotes().map {
                FormattedNotes(it.isActive, it.title.uppercase(),it.description)
            }.filter {
                isActive
            }.collect {
                Log.d("Practical Use",it.toString())
            }
        }*/

        /* Normal suspend and launch
        CoroutineScope(Dispatchers.IO).launch {
            getUserName().forEach {
                Log.d("Rohit Singh",it)
            }
        }*/

    }

    data class Notes(val id: Int, val isActive: Boolean, val title: String, val description: String)
    data class FormattedNotes(val isActive: Boolean, val title: String, val description: String)
    private fun getNotes(): Flow<Notes>{
        val list = listOf<Notes>(
            Notes(1,true,"rohit","singh"),
            Notes(2,true,"mohit","singh"),
            Notes(1,true,"sobhit","singh")
        )
        // Convert list to Flow
        return list.asFlow()
    }

    // Producer Consumer using flow
    // A flow can have multiple consumers and everyone will get fresh data from starting.
    fun producer() = flow<Int> {
        val list = listOf<Int>(1,2,3,4,5,6,7,8,9,0)
        list.forEach {
            delay(1000)
            emit(it)
        }
    }

    /* Normal producer consumer scenario

    private fun producer(){
        CoroutineScope(Dispatchers.IO).launch {
            channel.send(100)
            channel.send(200)
        }
    }

    private fun consumer(){
        CoroutineScope(Dispatchers.IO).launch{
            Log.d("Rohit Singh",channel.receive().toString())
            Log.d("Rohit Singh",channel.receive().toString())
        }
    }

    */
}


private suspend fun getUserName(): List<String> {
    val list = mutableListOf<String>()
    list.add(getUser(1))
    list.add(getUser(2))
    list.add(getUser(3))
    return list
}

private suspend fun getUser(id: Int): String{
    delay(2000)
    return "$id"
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinFlowsTheme {
        Greeting("Android")
    }
}