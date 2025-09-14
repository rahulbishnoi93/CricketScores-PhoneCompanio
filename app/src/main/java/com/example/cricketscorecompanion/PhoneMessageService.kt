package com.example.cricketscorecompanion

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PhoneMessageService : WearableListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val json = Json { ignoreUnknownKeys = true }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("PhoneMessageService", "Message received: ${messageEvent.path}")

        scope.launch {
            val api = RetrofitClient.apiService
            val response: String = try {
                when {
                    messageEvent.path == "/get_live" -> {
                        val liveMatches = api.getLiveMatches()
                        json.encodeToString(liveMatches)
                    }
                    messageEvent.path == "/get_recent" -> {
                        val recentMatches = api.getRecentMatches()
                        json.encodeToString(recentMatches)
                    }
                    messageEvent.path == "/get_schedule" -> {
                        val schedule = api.getSchedule()
                        json.encodeToString(schedule)
                    }
                    messageEvent.path.startsWith("/get_match_details/") -> {
                        val id = messageEvent.path.substringAfterLast("/")
                        val details = api.getMatchDetails(id)
                        json.encodeToString(details)
                    }
                    else -> {
                        "Unknown request: ${messageEvent.path}"
                    }
                }
            } catch (e: Exception) {
                Log.e("PhoneMessageService", "Error fetching data", e)
                "Error: ${e.localizedMessage}"
            }

            // send response back
            try {
                val nodeClient = Wearable.getNodeClient(this@PhoneMessageService)
                val nodes = nodeClient.connectedNodes.await()
                val messageClient = Wearable.getMessageClient(this@PhoneMessageService)

                nodes.forEach { node ->
                    messageClient.sendMessage(node.id, "/response${messageEvent.path}", response.toByteArray()).await()
                    Log.d("PhoneMessageService - response",  response)
                }
                Log.d("PhoneMessageService", "Response sent to wear")
            } catch (e: Exception) {
                Log.e("PhoneMessageService", "Failed to send response", e)
            }
        }
    }
}
