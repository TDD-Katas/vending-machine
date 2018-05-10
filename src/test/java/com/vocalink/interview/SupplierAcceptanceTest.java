package com.vocalink.interview;

import com.vocalink.interview.support.SupplierInteraction;
import com.vocalink.interview.support.UserInteraction;
import com.vocalink.interview.support.VendingAppThread;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("The vending machine should ")
class SupplierAcceptanceTest {
    private static final int SHORT_RUNNING_TIMEOUT = 5000;
    private UserInteraction user;
    private SupplierInteraction supplier;

    @BeforeEach
    void setUp() throws IOException {
        VendingAppThread vendingAppThread = VendingAppThread.createAndStartWithTimeout(SHORT_RUNNING_TIMEOUT);
        user = UserInteraction.with(vendingAppThread);
        supplier = SupplierInteraction.with(vendingAppThread);
    }

    @AfterEach
    void tearDown() throws IOException {
        supplier.stopMachine();
    }


    @Test
    @DisplayName("allow the supplier to reset the machine to its initial state")
    void reset() throws IOException {
        assertThat(user.readScreen(), containsString("Please select the item you wish to purchase:"));

        String itemCodeForCandy = "10";
        user.selectItem(itemCodeForCandy);
        assertThat(user.readScreen(), containsString("Insert:"));

        supplier.resetMachine();
        assertThat(user.rereadScreen(), containsString("Please select the item you wish to purchase:"));
    }

    @Test
    @Disabled("Not implemented")
    @DisplayName("not release an item if insufficient coins have been inserted")
    void holdBack() { }

    @Test
    @Disabled("Not implemented")
    @DisplayName("not accept invalid coins")
    void retryCoin() { }

    @Test
    @Disabled("Not implemented")
    @DisplayName("not accept invalid products")
    void retryProduct() { }
}