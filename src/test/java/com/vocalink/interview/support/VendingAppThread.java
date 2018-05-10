package com.vocalink.interview.support;

import com.vocalink.interview.VendingMachineApp;

import java.io.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.vocalink.interview.VendingMachineApp.SCREEN_DELIMITER;

public class VendingAppThread extends Thread {

    //To app
    private final PipedInputStream threadToApp;
    private final PipedOutputStream clientToThread;

    //From app
    private final PipedInputStream threadToClient;
    private final PipedOutputStream appToThread;

    //Buffered Streams
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private VendingAppThread() {
        super("CommandLineApp Thread");
        try {
            // client writes -> thread <- app reads
            this.threadToApp = new PipedInputStream();
            this.clientToThread = new PipedOutputStream(threadToApp);

            // client reads -> thread <- app writes
            this.threadToClient = new PipedInputStream();
            this.appToThread = new PipedOutputStream(threadToClient);

            // Buffered streams
            reader = new BufferedReader(new InputStreamReader(threadToClient));
            writer = new BufferedWriter(new OutputStreamWriter(clientToThread));
        } catch ( IOException e ){
            throw new IllegalStateException("Failed to create streams");
        }
    }

    public static VendingAppThread createAndStartWithTimeout(int timeoutInMillis) {
        VendingAppThread appThread = new VendingAppThread();
        appThread.start();
        appThread.startAutoDestroyTimer(timeoutInMillis,
                () -> System.err.println("Warning ! The AppThread self destruct timer was triggered."));
        return appThread;
    }

    public void run() {
        try {
            PrintStream out = new PrintStream(appToThread);
            String[] args = {"--no-colors"};
            System.out.println("Starting app with: " + Arrays.toString(args));
            VendingMachineApp.mainWithIO(threadToApp, out);
        } catch (Exception e) {
            System.err.println("Top level exception in CommandLineApp: ");
            e.printStackTrace();
        }
    }

    private void destroyForcibly(EventNotifier destructionNotifier) {
        //Close all streams and interrupt thread
        try {
            threadToApp.close();
            appToThread.close();
            clientToThread.close();
            threadToClient.close();
        } catch (IOException e) {
            System.err.println("Exception when closing stream for: "+VendingAppThread.this.getName());
            e.printStackTrace();
        }

        if (VendingAppThread.this.isAlive()) {
            VendingAppThread.this.interrupt();
            destructionNotifier.eventHappened();
        }
    }

    private void startAutoDestroyTimer(Integer gracePeriodMillis, EventNotifier destructionNotifier) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                VendingAppThread.this.destroyForcibly(destructionNotifier);
                timer.cancel();
            }
        };
        timer.schedule(task, gracePeriodMillis);
    }


    //~~~~~~~ Interaction ~~~~~~~

    String readScreenFromReader() throws IOException {
        StringBuilder screen = new StringBuilder();

        String line = "";
        while((line != null) &&
                (!SCREEN_DELIMITER.equals(line))) {
            line = reader.readLine();
            screen.append(line).append("\n");
        }

        return screen.toString();
    }

    void send(String text) throws IOException {
        writer.write(text+"\n");
        writer.flush();
    }
}
