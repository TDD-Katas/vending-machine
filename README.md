# Design a Vending Machine

## Problem statement

You need to design a Vending Machine which follows following requirements
- Accepts coins of 1, 5, 20, 50 pence well as one-pound coin
- Allow user to select products in pence e.g. CANDY (10), SNACK (50), NUTS (75), Coke (150), Bottle Water (100)
- Allow user to take refund by cancelling the request.
- Return selected product and remaining change if any
- Allow reset operation for vending machine supplier

## The approach

The current application is a minimum viable product that demonstrates the interaction between the user and the vending machine.

I have used Acceptance Test Driven Development in order to maximise the return on the time invested.
The tests are only focused on the paths that bring the most value to the business.

Future tests need to be written at a lower level in order to capture more complex edge cases and negative scenarios.

To run the acceptance tests:
```
./gradlew test
```

## Running the app

In a real vending machine we have signal lines that are coming in from keypad, coin reader, supplier buttons.
We also have output lines for the display, product release and coin release.

In the context of this console app, I have described the different input signals as text commands:
- `keypad 100` - select product 100 using keypad
- `coin 50` - insert a 50p coin
- `supplierReset` - press the supplier reset button
- `supplierStop` - press the supplier stop button

To keep the application simple I have only used the display as output.

To run the console app:
```
./gradlew run --console plain
```

## Next steps

The are many things that need to be improved and I will only name few of them:
- Implement acceptance tests for the scenarios where the supplier might lose money (see Disabled tests)
- Abstractions need to be defined for the input and output signals and unit tests need to capture the complexity of the state transitions
- The Products can be modeled as a repository and separated from the state processing logic
- The Coin types are also a form of data and should be injected at runtime
- The Command parsing and handling needs to be consolidated so that is should be easy for a State to define a mapping between a command and an action
- Introduce a better way of retrieving and working with products and coins




