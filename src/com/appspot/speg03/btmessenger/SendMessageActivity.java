package com.appspot.speg03.btmessenger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

public class SendMessageActivity extends Activity {

    private BluetoothAdapter mAdapter;
    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action);

        String senderAddress = getResources().getString(R.string.bluetooth_dest_address);
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mDevice = mAdapter.getRemoteDevice(senderAddress);

        try {
            String uuid = getResources().getString(R.string.bluetooth_uuid);
            mSocket = mDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
            mSocket.connect();
            OutputStream os = mSocket.getOutputStream();
            os.write("This message is a test by bluetooth.".getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            mSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
