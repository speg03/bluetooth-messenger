package com.github.speg03.btmessenger;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ReceiveMessageService extends Service {

    private static final String TAG = ReceiveMessageService.class.getSimpleName();

    private BluetoothAdapter mAdapter;
    private BluetoothServerSocket mServerSocket;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate was called.");
        super.onCreate();
        Toast.makeText(this, "ReceiveMessageService started.", Toast.LENGTH_SHORT).show();

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null || !mAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not available.", Toast.LENGTH_SHORT).show();
            stopSelf();
            return;
        }

        try {
            String uuid = getResources().getString(R.string.bluetooth_uuid);
            mServerSocket = mAdapter.listenUsingRfcommWithServiceRecord("BluetoothMessenger",
                    UUID.fromString(uuid));
        }
        catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BluetoothSocket mSocket = mServerSocket.accept();
                    InputStream is = mSocket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bytes;
                    bytes = is.read(buffer);

                    String message = new String(buffer, 0, bytes);
                    Log.d(TAG, "received message: " + message);
                }
                catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            }
            catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        Toast.makeText(this, "ReceiveMessageService stopped.", Toast.LENGTH_SHORT).show();
    }

}
