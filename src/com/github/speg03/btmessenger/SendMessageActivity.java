package com.github.speg03.btmessenger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SendMessageActivity extends Activity {

    private static final String TAG = SendMessageActivity.class.getSimpleName();

    private BluetoothAdapter mAdapter;
    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate was called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action);

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null || !mAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not available.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String senderAddress = getResources().getString(R.string.bluetooth_dest_address);
        mDevice = mAdapter.getRemoteDevice(senderAddress);

        try {
            String uuid = getResources().getString(R.string.bluetooth_uuid);
            mSocket = mDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
            mSocket.connect();

            String message = "This message is a test by bluetooth.";
            Log.d(TAG, "send message: " + message);
            OutputStream os = mSocket.getOutputStream();
            os.write(message.getBytes());
        }
        catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy was called.");
        super.onDestroy();

        try {
            if (mSocket != null) {
                mSocket.close();
            }
        }
        catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

}
