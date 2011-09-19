package com.appspot.speg03.btmessenger;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ReceiveMessageActivity extends Activity {

    private static final String TAG = ReceiveMessageActivity.class.getSimpleName();

    private BluetoothAdapter mAdapter;
    private BluetoothServerSocket mServerSocket;
    private BluetoothSocket mSocket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate was called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action);

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null || !mAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not available.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            String uuid = getResources().getString(R.string.bluetooth_uuid);
            mServerSocket = mAdapter.listenUsingRfcommWithServiceRecord("BluetoothMessenger",
                    UUID.fromString(uuid));
            mSocket = mServerSocket.accept();
            InputStream is = mSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            bytes = is.read(buffer);

            String message = new String(buffer, 0, bytes);
            Log.d(TAG, "received message: " + message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy was called.");
        super.onDestroy();

        try {
            mServerSocket.close();
            mSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
