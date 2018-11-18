package flickerimage.test.com.flickrimagesearch.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import flickerimage.test.com.flickrimagesearch.FlickerImageSearch;
import flickerimage.test.com.flickrimagesearch.utility.NetworkChangeCallBack;

/**
 * This is the Base activity which handles common activities across the application
 */
public abstract class SuperActivity extends AppCompatActivity implements NetworkChangeCallBack {

    private  boolean isOnline;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkCallBack(this);
    }

    private void registerNetworkCallBack(NetworkChangeCallBack networkCallBack) {
        FlickerImageSearch context = (FlickerImageSearch) getApplicationContext();
        context.setNetworkCallBack(networkCallBack);
    }

    @Override
    public void onNetWorkConnected(String msg, int networkCode) {
        isOnline = true;
    }

    @Override
    public void onNetWorkDisconnected(String msg) {
        isOnline = false;
        Toast.makeText(getApplicationContext(),"Network Unavailable, please try again later!", Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline(){

        return isOnline;
    }

}
