
package com.example.android.diplom_p2p;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The implementation of a ServerSocket handler. This is used by the wifi p2p
 * group owner.
 */
public class GroupOwnerSocketHandler extends Thread {

    ServerSocket socket = null;
    private final int THREAD_COUNT = 10;

    private final List<ChatManager> clients = new ArrayList<>();
    private Handler handler;
    private static final String TAG = "GroupOwnerSocketHandler";

    public GroupOwnerSocketHandler(Handler handler) throws IOException {
        try {
            //???
            if (socket == null) {
                socket = new ServerSocket(4545);
            }
            this.handler = handler;
            Log.d("GroupOwnerSocketHandler", "Socket Started");
        } catch (IOException e) {
            e.printStackTrace();
            pool.shutdownNow();
            throw e;
        }

    }
    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public void run() {
        while (true) {
            try {
                pool.execute(new ChatManager(socket.accept(), handler));
                Log.d(TAG,"ip =" + socket.getInetAddress().toString());
                Log.d(TAG, "Launching the I/O handler");

            } catch (IOException e) {
                try {
                    if (socket != null && !socket.isClosed())
                        socket.close();
                } catch (IOException ioe) {

                }
                e.printStackTrace();
                pool.shutdownNow();
                break;
            }
        }
    }

    public void broadcastMessage(String message) {
        for (ChatManager client : clients) {
            client.write(Arrays.toString(message.getBytes()));
        }
    }

}
