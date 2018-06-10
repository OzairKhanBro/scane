package com.example.uzair.scane;

import android.app.Application;

import net.doo.snap.ScanbotSDKInitializer;

/**
 * {@link ScanbotSDKInitializer} should be called
 * in {@code Application.onCreate()} method for RoboGuice modules initialization
 */
public class ExampleApplication extends Application {
    private final String licenseKey =
            "dJUbzZpdq0PPkQl+CnBbb7Cbmm0JUv" +
                    "SDmWEtfHz6Jh9EYwKNmgAC+DOqrdyK" +
                    "JTQDkO4cYNGljqzccPr4xO1Wi5eB6t" +
                    "UOK4CeLqvlV5J2jnT/OTueAiz9chHb" +
                    "kvugBzw6KYZp+C7UYpeUPHRY0sVqER" +
                    "tPrlgINtvHM/6MaYu21jZJ7ImzsV8h" +
                    "XzDSxQlD35ynv7vQM7UUk//meHJ+aG" +
                    "m5o9FEa+V5yvFIEqLWpyvg9Ib4mpaI" +
                    "8Q7zmQ9DZHgu7u51+TmJ6KnrhXOPQj" +
                    "fSDnkAY4ZX9bwVxzDUeF9dA0WlTnoK" +
                    "rdPd3vIb8rrPciQLKN43bxs3EUdu5M" +
                    "Xqv6ld5H0H4w==\nU2NhbmJvdFNESw" +
                    "pjb20uZXhhbXBsZS51emFpci5zY2Fu" +
                    "ZQoxNTMwNTc1OTk5CjU5MAoz\n";

    @Override
    public void onCreate() {
        new ScanbotSDKInitializer()
                // TODO add your license
                 .license(this, licenseKey)
                .initialize(this);

        super.onCreate();
    }
}
