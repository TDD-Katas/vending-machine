package com.vocalink.interview.support;

import java.io.IOException;

public class SupplierInteraction {
    private VendingAppThread appThread;

    private SupplierInteraction(VendingAppThread appThread) {
        this.appThread = appThread;
    }

    public static SupplierInteraction with(VendingAppThread appThread) {
        return new SupplierInteraction(appThread);
    }

    public void stopMachine() throws IOException {
        appThread.send("supplierStop");
    }

    public void resetMachine() throws IOException {
        appThread.send("supplierReset");
    }
}
