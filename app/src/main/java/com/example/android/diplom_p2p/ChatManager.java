
package com.example.android.diplom_p2p;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Handles reading and writing of messages with socket buffers. Uses a Handler
 * to post messages to UI thread for UI updates.
 */
public class ChatManager implements Runnable {

    private Socket socket = null;
    private Handler handler;

    public ChatManager(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    private InputStream iStream;
    private OutputStream oStream;
    private static final String TAG = "ChatHandler";

    @Override
    public void run() {
        try {

            iStream = socket.getInputStream();
            oStream = socket.getOutputStream();
            Log.d(TAG, "run: "+ socket.getInetAddress());
            byte[] buffer = new byte[1024];
            int bytes;
            handler.obtainMessage(WiFiServiceDiscoveryActivity.MY_HANDLE, this)
                    .sendToTarget();

            while (true) {
                try {
                    // Read from the InputStream
                    bytes = iStream.read(buffer);
                    if (bytes == -1) {
                        continue;
                    }

                    // Send the obtained bytes to the UI Activity
                    Log.d(TAG, "Rec:" + Arrays.toString(buffer));
                    handler.obtainMessage(WiFiServiceDiscoveryActivity.MESSAGE_READ,
                            bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String msg) {
        byte[] buffer = msg.getBytes();
        Thread thread = new Thread() {
            public void run() {
                try {
                    oStream.write(buffer);
                } catch (IOException e) {
                    Log.e(TAG, "Exception during write", e);
                }
            }
        };
        thread.start();
    }

}
