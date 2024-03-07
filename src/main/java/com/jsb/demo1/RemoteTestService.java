package com.jsb.demo1;

import bayern.steinbrecher.jsch.*;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class RemoteTestService {
    private static final String LOCAL_FOLDER_PATH = "/path/to/local/folder";
    private static final String REMOTE_FOLDER_PATH = "/path/to/remote/folder";
    private static final String REMOTE_USER = "remote_user";
    private static final String REMOTE_HOST = "remote_host";
    private static final int SSH_PORT = 22;
    private static final String SSH_PASSWORD = "remote_password";

    public void transferFilesToRemoteServer() {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(REMOTE_USER, REMOTE_HOST, SSH_PORT);
            session.setPassword(SSH_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("exec");
            ChannelExec channelExec = (ChannelExec) channel;
            channelExec.setCommand("scp -r -p -t " + REMOTE_FOLDER_PATH);
            channelExec.setErrStream(System.err);

            // Get the output stream of the channel and connect it to the input stream of the file
            channelExec.setOutputStream(System.out);

            // Connect the channel
            channelExec.connect();

            // Transfer each file from the local folder
            File localFolder = new File(LOCAL_FOLDER_PATH);
            File[] files = localFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        transferFile(file, channelExec);
                    }
                }
            }

            // Disconnect the channel and session
            channelExec.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
    }

    private void transferFile(File file, ChannelExec channelExec) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        OutputStream out = channelExec.getOutputStream();
        out.write(("C0644 " + file.length() + " " + file.getName() + "\n").getBytes());
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) break;
            out.write(buf, 0, len);
        }
        out.write(0);
        out.flush();
        fis.close();
    }
}

