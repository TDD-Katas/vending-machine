package com.vocalink.interview;

import com.vocalink.interview.states.ProductListState;
import com.vocalink.interview.states.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class VendingMachineApp {
    private static final Logger LOG = LoggerFactory.getLogger(VendingMachineApp.class);

    public static final String SCREEN_DELIMITER = "==================";

    public static void main(String[] args) {
        try {
            mainWithIO(System.in, System.out);
        } catch (IOException e) {
            LOG.error("Application was stopped due to an unrecoverable exception", e);
        }
    }

    public static void mainWithIO(InputStream in, PrintStream out) throws IOException {
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(in));

        State initialState = new ProductListState();
        State state = initialState;

        do {
            state.render(out);
            out.println(SCREEN_DELIMITER);

            String command = consoleIn.readLine();
            if (command == null) {
                state = State.STOP;
            } else if (command.startsWith("supplierStop")) {
                state = State.STOP;
            } else if (command.startsWith("supplierReset")) {
                state = initialState;
            } else {
                state = state.handleCommand(command);
            }

        } while (state != State.STOP);
    }
}
