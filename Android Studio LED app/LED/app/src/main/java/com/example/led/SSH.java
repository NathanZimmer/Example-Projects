package com.example.led;

import android.os.AsyncTask;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSH extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        String user = "pi";
        String password = "1234";
        String host = "192.168.1.69";
        int port = 22;

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setTimeout(10000);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(strings[0]);
            channel.connect();
            channel.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}

