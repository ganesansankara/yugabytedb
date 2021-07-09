package com.ganesan;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.configuration.ProfileManager;

@QuarkusMain
public class MainApplication {
    public static void main (String ... args) {
        System.out.println ( "*** RUNNNIG MAIN METHOD");
        ProfileManager.setLaunchMode(LaunchMode.DEVELOPMENT);
        System.out.printf ( "*** QUARKUS LAUNCH MODE=%s", ProfileManager.getLaunchMode());
    }
}
