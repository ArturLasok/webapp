package com.arturlasok.feature_core.util

import android.app.Application
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class isOnline
@Inject
constructor(
    application: Application,
) {

    val validNetworks: MutableSet<Network> = HashSet()
    val isNetworkAvailable = mutableStateOf(false)
    val cm = application.getSystemService(ComponentActivity.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager

    fun runit()

    {
        CoroutineScope(Dispatchers.IO).launch {

            lateinit var networkCallback: android.net.ConnectivityManager.NetworkCallback



            fun ncal() = object : android.net.ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    Log.d(TAG_NETWORK, "onAvailable network: ")


                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {


                    if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                        Log.d(TAG_NETWORK, "${network.hashCode()} -> VALIDATED")
                        validNetworks.add(network)
                        if (validNetworks.size > 0) {
                            isNetworkAvailable.value = true
                        }
                        Log.d(TAG_NETWORK, "AVA: ${validNetworks.size} net ${network.hashCode()} ADD")
                    } else {
                        validNetworks.remove(network)
                        Log.d(TAG_NETWORK, "${network.hashCode()} -> NOT!!!! VALIDATED")
                        Log.d(TAG_NETWORK, "AVA: ${validNetworks.size} net ${network.hashCode()} LOST VALIDATE")
                    }
                }

                override fun onLost(network: Network) {
                    Log.d(TAG_NETWORK, "onLost:")
                    validNetworks.remove(network)
                    Log.d(TAG_NETWORK, "AVA: ${validNetworks.size} net ${network.hashCode()} LOST")

                    isNetworkAvailable.value = validNetworks.size > 0
                }
            }

            networkCallback = ncal()
            cm.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build(), networkCallback
            )


        }
    }
}