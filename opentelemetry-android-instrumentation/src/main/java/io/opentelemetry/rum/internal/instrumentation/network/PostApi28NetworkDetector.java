/*
 * Copyright Splunk Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.rum.internal.instrumentation.network;

import static io.opentelemetry.rum.internal.instrumentation.network.CurrentNetworkProvider.NO_NETWORK;
import static io.opentelemetry.rum.internal.instrumentation.network.CurrentNetworkProvider.UNKNOWN_NETWORK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.telephony.TelephonyManager;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

@RequiresApi(api = Build.VERSION_CODES.P)
class PostApi28NetworkDetector implements NetworkDetector {
    private final ConnectivityManager connectivityManager;
    private final TelephonyManager telephonyManager;
    private final CarrierFinder carrierFinder;
    private final Context context;

    PostApi28NetworkDetector(
            ConnectivityManager connectivityManager,
            TelephonyManager telephonyManager,
            CarrierFinder carrierFinder,
            Context context) {
        this.connectivityManager = connectivityManager;
        this.telephonyManager = telephonyManager;
        this.carrierFinder = carrierFinder;
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    @Override
    public CurrentNetwork detectCurrentNetwork() {
        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if (capabilities == null) {
            return NO_NETWORK;
        }
        String subType = null;
        Carrier carrier = carrierFinder.get();
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            // If the app has the permission, use it to get a subtype.
            if (hasPermission(Manifest.permission.READ_PHONE_STATE)) {
                subType = getDataNetworkTypeName(telephonyManager.getDataNetworkType());
            }
            return CurrentNetwork.builder(NetworkState.TRANSPORT_CELLULAR)
                    .carrier(carrier)
                    .subType(subType)
                    .build();
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return CurrentNetwork.builder(NetworkState.TRANSPORT_WIFI).carrier(carrier).build();
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
            return CurrentNetwork.builder(NetworkState.TRANSPORT_VPN).carrier(carrier).build();
        }
        // there is an active network, but it doesn't fall into the neat buckets above
        return UNKNOWN_NETWORK;
    }

    // visible for testing
    boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    private String getDataNetworkTypeName(int dataNetworkType) {
        switch (dataNetworkType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "EHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO_A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO_B";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_GSM:
                return "GSM";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPAP";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "IDEN";
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return "IWLAN";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_NR:
                return "NR";
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return "SCDMA";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "UNKNOWN";
        }
        return "UNKNOWN";
    }
}
