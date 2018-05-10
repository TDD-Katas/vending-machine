package com.vocalink.interview.states;

import java.io.PrintStream;

public interface State {
    State STOP = null;

    void render(PrintStream out);

    State handleCommand(String command);
}
