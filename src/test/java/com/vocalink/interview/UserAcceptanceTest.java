package com.vocalink.interview;

import com.vocalink.interview.support.SupplierInteraction;
import com.vocalink.interview.support.VendingAppThread;
import com.vocalink.interview.support.UserInteraction;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("User should be able to ")
class UserAcceptanceTest {
    private static final int APP_TIMEOUT = 5000;
    private UserInteraction user;
    private SupplierInteraction supplier;

    @BeforeEach
    void setUp() throws IOException {
        VendingAppThread vendingAppThread = VendingAppThread.createAndStartWithTimeout(APP_TIMEOUT);
        user = UserInteraction.with(vendingAppThread);
        supplier = SupplierInteraction.with(vendingAppThread);
    }

    @AfterEach
    void tearDown() throws IOException {
        supplier.stopMachine();
    }

    @Test
    @DisplayName("see the products on sale")
    void list() {
        String screenContents = user.readScreen();

        assertThat(screenContents, containsString("Please select the item you wish to purchase:"));
        for (String item : Arrays.asList(
                "CANDY (10)",
                "SNACK (50)",
                "NUTS (75)",
                "Coke (150)",
                "Bottle Water (100)")) {
            assertThat(screenContents, containsString(item));
        }
    }

    @Test
    @DisplayName("purchase an available item with the exact amount")
    void purchaseWithExactAmount() throws IOException {
        String itemCodeForCandy = "10";
        user.selectItem(itemCodeForCandy);
        assertThat(user.readScreen(), containsString("Insert: 10 pence"));

        user.insertCoin("5");
        assertThat(user.readScreen(), containsString("Insert: 5 pence"));

        user.insertCoin("5");
        assertThat(user.readScreen(), containsString("Please collect your item: CANDY"));
    }

    @Test
    @DisplayName("purchase an available item and get correct change back")
    void purchase() throws IOException {
        String itemCodeForNuts = "75";
        user.selectItem(itemCodeForNuts);
        assertThat(user.readScreen(), containsString("Insert: 75 pence"));

        user.insertCoin("100");
        assertThat(user.readScreen(), containsString("Please collect your item: NUTS"));
        assertThat(user.readScreen(), containsString("Please pick up your change: 25 pence"));
    }


    @Test
    @DisplayName("cancel a purchase and get a refund")
    void refund() throws IOException {
        String itemCodeForNuts = "75";
        user.selectItem(itemCodeForNuts);
        assertThat(user.readScreen(), containsString("Insert: 75 pence"));

        user.insertCoin("50");
        user.requestRefund();
        assertThat(user.readScreen(), containsString("Purchase canceled: NUTS"));
        assertThat(user.readScreen(), containsString("Please pick up your refund: 50 pence"));
    }
}