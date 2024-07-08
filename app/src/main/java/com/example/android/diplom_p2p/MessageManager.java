package com.example.android.diplom_p2p;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageManager {

    private static final String FILE_NAME = "messages.txt";
    private Context context;

    public MessageManager(Context context) {
        this.context = context;
    }

    public void saveMessage(String sender, String message) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write(sender + ";" + timestamp + ";" + message);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            Log.e("MessageManager", "Error saving message", e);
        }
    }

    public List<String> loadMessages() {
        List<String> messages = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) {
                return messages;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                messages.add(line);
            }
            reader.close();
        } catch (IOException e) {
            Log.e("MessageManager", "Error loading messages", e);
        }
        return messages;
    }

    public void deleteAllMessages() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            Log.e("MessageManager", "Error deleting messages", e);
        }
    }
}
