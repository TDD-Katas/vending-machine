package com.vocalink.interview.support;

import java.io.IOException;

public class UserInteraction {
    private VendingAppThread appThread;
    private String currentScreen;

    private UserInteraction(VendingAppThread appThread) {
        this.appThread = appThread;
    }

    public static UserInteraction with(VendingAppThread appThread) throws IOException {
        UserInteraction instance = new UserInteraction(appThread);
        instance.refreshScreen();
        return instance;
    }

    private void refreshScreen() throws IOException {
        currentScreen = appThread.readScreenFromReader();
        System.out.println(currentScreen);
    }

    private void sendCommand(String type, String content) throws IOException {
        String command = type + " " + content;
        appThread.send(command);
        System.out.println(">>> "+command);
    }

    //~~~~ Read

    public String readScreen() {
        return currentScreen;
    }

    public String rereadScreen() throws IOException {
        refreshScreen();
        return currentScreen;
    }

    public void selectItem(String itemCode) throws IOException {
        sendCommand("keypad", itemCode);
        refreshScreen();
    }

    public void insertCoin(String coin) throws IOException {
        sendCommand("coin", coin);
        refreshScreen();
    }

    public void requestRefund() throws IOException {
        sendCommand("refund", "");
        refreshScreen();
    }
}
