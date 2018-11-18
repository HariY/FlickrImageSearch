package flickerimage.test.com.flickrimagesearch.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;



public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = -1;
    private static final int MAX_CONNECTION_TRIES = 3;
    private static Context mContext;

    public static int getConnectivityStatus(Context context) {
        mContext=context;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }
        }

        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(int code) {
        String status = null;

        if (code == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
        }
        else if (code == NetworkUtil.TYPE_MOBILE) {
            status=getMobileNetworkType(mContext);
            //status = "Mobile data enabled";
        }
        else if (code == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }

        return status;
    }

    public static boolean isOnline(Context context) {
        int i = 0;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        for (i = 0; i < MAX_CONNECTION_TRIES; i++) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if ((netInfo != null) && (netInfo.isConnectedOrConnecting())) {
                break;
            }
        }

        if (MAX_CONNECTION_TRIES == i) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * To get device consuming netowkr type is 2g,3g,4g
     *
     * @param context
     * @return "2g","3g","4g" as a String based on the network type
     */
    public static String getMobileNetworkType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2g";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                /**
                 From this link https://goo.gl/R2HOjR ..NETWORK_TYPE_EVDO_0 & NETWORK_TYPE_EVDO_A
                 EV-DO is an evolution of the CDMA2000 (IS-2000) standard that supports high data rates.

                 Where CDMA2000 https://goo.gl/1y10WI .CDMA2000 is a family of 3G[1] mobile technology standards for sending voice,
                 data, and signaling data between mobile phones and cell sites.
                 */
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                //Log.d("Type", "3g");
                //For 3g HSDPA , HSPAP(HSPA+) are main  networktype which are under 3g Network
                //But from other constants also it will 3g like HSPA,HSDPA etc which are in 3g case.
                //Some cases are added after  testing(real) in device with 3g enable data
                //and speed also matters to decide 3g network type
                //http://goo.gl/bhtVT
                return "3g";
            case TelephonyManager.NETWORK_TYPE_LTE:
                //No specification for the 4g but from wiki
                //I found(LTE (Long-Term Evolution, commonly marketed as 4G LTE))
                //https://goo.gl/9t7yrR
                return "4g";
            default:
                return "Notfound";
        }
    }
}
