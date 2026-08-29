package com.example.data.api

import android.util.Log
import com.example.data.RideRepository
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class CoRideRealtimeClient(private val rideRepository: RideRepository) {
    private val serverRepository = CoRideServerRepository(rideRepository)
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isRunning = false

    fun start() {
        if (isRunning) return
        isRunning = true

        connectWebSocket()

        // Background polling ticker (every 3 seconds) to ensure real-time instant updates without manual refresh
        scope.launch {
            while (isRunning) {
                try {
                    serverRepository.syncRidesFromServer()
                } catch (e: Exception) {
                    // Ignore network blips
                }
                delay(3000)
            }
        }
    }

    private fun connectWebSocket() {
        val url = "wss://qfmwippvbbyhjhtoumyw.supabase.co/realtime/v1/websocket?apikey=sb_publishable_0B-kYCNmO_z5W3txRpa3mw_obfp6MpV&vsn=1.0.0"
        val request = Request.Builder().url(url).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("CoRideRealtime", "WebSocket connected, joining channel...")
                val joinMsg = JSONObject().apply {
                    put("topic", "realtime:public:co_ride")
                    put("event", "phx_join")
                    put("payload", JSONObject())
                    put("ref", "1")
                }
                webSocket.send(joinMsg.toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val json = JSONObject(text)
                    val event = json.optString("event")
                    if (event == "postgres_changes" || event == "phx_reply") {
                        scope.launch {
                            serverRepository.syncRidesFromServer()
                            Log.d("CoRideRealtime", "Realtime event received, synced rides from server.")
                        }
                    }
                } catch (e: Exception) {
                    Log.w("CoRideRealtime", "Error handling realtime message: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.w("CoRideRealtime", "WebSocket failure: ${t.message}, reconnecting in 5s...")
                reconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("CoRideRealtime", "WebSocket closed: $reason, reconnecting...")
                reconnect()
            }
        })
    }

    private fun reconnect() {
        if (!isRunning) return
        scope.launch {
            delay(5000)
            if (isRunning) {
                connectWebSocket()
            }
        }
    }

    fun stop() {
        isRunning = false
        webSocket?.close(1000, "Client stopped")
        scope.cancel()
    }
}
