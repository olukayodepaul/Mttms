package com.mobbile.paul.mttms.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class Utils {

    companion object {

        const val USER_INFOS = "com.tm.mt.v3.1.2.user.info"
        const val CUSTOMERS_VISIT = "com.tm.visit.mt.v3.1.2.user.info"
        const val CUSTOMERS_INFORMATION = "com.tm.info.mt.v3.1.2.user.info"
        const val LOCAL_AND_REMOTE_CUSTOMERS = "com.tm.info.mt.local.remote.v3.1.2.user.info"

        @Suppress("DEPRECATION")
        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }
    }
}

