package com.ddmeng.androidutils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ddmeng.androidutils.Constants.KNOWN_ROOT_APPS_PACKAGES;

// From: https://github.com/scottyab/rootbeer
public class RootUtils {

    private Context context;

    public RootUtils(Context context) {
        this.context = context;
    }

    public boolean isDeviceRooted() {
        return checkForBinary("su") || detectRootManagementApps()
                || detectTestKeys() || checkForBinary("magisk");
    }

    /**
     * Using the PackageManager, check for a list of well known root apps. @link {Const.KNOWN_ROOT_APPS_PACKAGES}
     *
     * @return true if one of the apps it's installed
     */
    public boolean detectRootManagementApps() {

        ArrayList<String> packages = new ArrayList<>(Arrays.asList(KNOWN_ROOT_APPS_PACKAGES));
        return isAnyPackageFromListInstalled(packages);
    }

    /**
     * Check if any package in the list is installed
     *
     * @param packages - list of packages to search for
     * @return true if any of the packages are installed
     */
    private boolean isAnyPackageFromListInstalled(List<String> packages) {
        boolean result = false;

        PackageManager pm = context.getPackageManager();

        for (String packageName : packages) {
            try {
                // Root app detected
                pm.getPackageInfo(packageName, 0);
                Log.d("Root", packageName + " ROOT management app detected!");
                result = true;
                break;
            } catch (PackageManager.NameNotFoundException e) {
                // Exception thrown, package is not installed into the system
            }
        }

        return result;
    }

    /**
     * @param filename - check for this existence of this file
     * @return true if found
     */
    public boolean checkForBinary(String filename) {

        String[] pathsArray = Constants.SU_PATHS;

        boolean result = false;

        for (String path : pathsArray) {
            String completePath = path + filename;
            File f = new File(path, filename);
            boolean fileExists = f.exists();
            if (fileExists) {
                Log.v("Root", completePath + " binary detected!");
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Release-Keys and Test-Keys has to do with how the kernel is signed when it is compiled.
     * Test-Keys means it was signed with a custom key generated by a third-party developer.
     *
     * @return true if signed with Test-keys
     */
    public boolean detectTestKeys() {
        String buildTags = android.os.Build.TAGS;

        boolean testKeysDetected = buildTags != null && buildTags.contains("test-keys");
        if (testKeysDetected) {
            Log.d("Root", "Test keys detected!");
        }
        return testKeysDetected;
    }

}
