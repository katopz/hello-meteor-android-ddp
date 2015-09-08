package net.pirsquare.hello_meteor_android_ddp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;

public class MainActivity extends AppCompatActivity implements MeteorCallback {

    private String TAG = "MainActivity.Meteor";
    private Meteor mMeteor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMeteor = new Meteor(this, "ws://192.168.2.133:3000/websocket");
        mMeteor.setCallback(this);

        // use this if you want to register and then log in
        //registerAndLogin();

        // use this if you already register
        login();
    }

    public void onConnect(boolean signedInAutomatically) {
        Log.i(TAG, "onConnect");
    }

    private void registerAndLogin() {
        mMeteor.registerAndLogin("john", "john.doe@example.com", "123456", new ResultListener() {

            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "userId : " + mMeteor.getUserId());
                getFirebaseToken(mMeteor.getUserId());
            }

            @Override
            public void onError(String s, String s1, String s2) {
                Log.i(TAG, "onError : " + s);
            }
        });
    }

    private void login() {
        mMeteor.loginWithEmail("john.doe@example.com", "123456", new ResultListener() {
            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "userId : " + mMeteor.getUserId());
                getFirebaseToken(mMeteor.getUserId());
            }

            @Override
            public void onError(String s, String s1, String s2) {
                Log.i(TAG, "onError : " + s);
            }
        });
    }

    private void getFirebaseToken(String userId) {
        // To prove call server function is working
        mMeteor.call("getFirebaseToken", new Object[]{userId}, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "getFirebaseToken : " + s);

                logInFirebase(s);
            }

            @Override
            public void onError(String s, String s1, String s2) {
                Log.i(TAG, "onError : " + s);
            }
        });
    }

    private void logInFirebase(String token) {
        // TODO : https://www.firebase.com/docs/web/guide/login/custom.html#section-authenticating-clients
    }

    public void onDisconnect(int code, String reason) {
        Log.i(TAG, "onDisconnect : " + reason);
    }

    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        Log.i(TAG, "onDataAdded");
    }

    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        Log.i(TAG, "onDataChanged");
    }

    public void onDataRemoved(String collectionName, String documentID) {
        Log.i(TAG, "onDataRemoved");
    }

    public void onException(Exception e) {
        Log.i(TAG, "onException");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
