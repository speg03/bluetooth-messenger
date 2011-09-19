package com.appspot.speg03.btmessenger;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.widget.Toast;

public class ReceiveMessageActivity extends Activity {

    private BluetoothAdapter mAdapter;
    private BluetoothServerSocket mServerSocket;
    private BluetoothSocket mSocket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action);

        mAdapter = BluetoothAdapter.getDefaultAdapter();
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
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
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
