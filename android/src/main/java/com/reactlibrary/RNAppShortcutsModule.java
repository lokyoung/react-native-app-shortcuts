package com.reactlibrary;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.app.Activity;
import android.util.Log;
import android.os.Build;

import java.util.Arrays;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNAppShortcutsModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNAppShortcutsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNAppShortcuts";
    }

    @ReactMethod
    public void handleShortcut(Callback successCallback) {
        if (Build.VERSION.SDK_INT < 25) return;

        Activity currentActivity = this.reactContext.getCurrentActivity();
        String shortCutId = currentActivity.getIntent().getStringExtra("shortcutId");
        if (shortCutId != null) {
            successCallback.invoke(shortCutId);
        }
    }

    @ReactMethod
    public void addShortcut(String id, String shortLabel, String longLabel, String iconFolderName, String iconName) {
        if (Build.VERSION.SDK_INT < 25) return;
        if (isShortcutExist(id)) return;

        ShortcutInfo shortcut = initShortcut(id, shortLabel, longLabel, iconFolderName, iconName);

        ShortcutManager shortcutManager = getShortCutManager();
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
    }

    @ReactMethod
    public void removeShortcut(String id) {
        if (Build.VERSION.SDK_INT < 25) return;

        ShortcutManager shortcutManager = getShortCutManager();
        shortcutManager.removeDynamicShortcuts(Arrays.asList(id));
    }

    @ReactMethod
    public void removeAllShortcuts() {
        if (Build.VERSION.SDK_INT < 25) return;

        ShortcutManager shortcutManager = getShortCutManager();
        shortcutManager.removeAllDynamicShortcuts();
    }

    @ReactMethod
    public void exists(String id, Callback successCallback, Callback failureCallback) {
        if (Build.VERSION.SDK_INT < 25) return;

        if (isShortcutExist(id)) {
            successCallback.invoke();
        } else {
            failureCallback.invoke();
        }
    }

    @ReactMethod
    public void updateShortcut(String id, String shortLabel, String longLabel, String iconFolderName, String iconName) {
        if (Build.VERSION.SDK_INT < 25) return;

        if (isShortcutExist(id)) {
            ShortcutInfo shortcut = initShortcut(id, shortLabel, longLabel, iconFolderName, iconName);

            ShortcutManager shortcutManager = getShortCutManager();
            shortcutManager.updateShortcuts(Arrays.asList(shortcut));
        } else {
            return;
        }
    }

    @Nullable
    private ShortcutInfo initShortcut(String id, String shortLabel, String longLabel, String iconFolderName, String iconName) {
        if (Build.VERSION.SDK_INT < 25) return null;

        Activity currentActivity = this.reactContext.getCurrentActivity();
        Intent intent = new Intent(currentActivity.getApplicationContext(), MainActivity.class);
        intent.putExtra("shortcutId", id);
        intent.setAction(Intent.ACTION_VIEW);

        Context currentContext = currentActivity.getApplicationContext();
        int iconId = currentContext.getResources().getIdentifier(iconName, iconFolderName, currentContext.getPackageName());

        ShortcutInfo shortcut = new ShortcutInfo.Builder(currentActivity, id)
                .setShortLabel(shortLabel)
                .setLongLabel(longLabel)
                .setIcon(Icon.createWithResource(currentActivity.getApplicationContext(), iconId))
                .setIntent(intent)
                .build();

        return shortcut;
    }

    private boolean isShortcutExist(String id) {
        if (Build.VERSION.SDK_INT < 25) return false;

        ShortcutManager shortcutManager = getShortCutManager();
        List<ShortcutInfo> shortcutInfoList = shortcutManager.getDynamicShortcuts();
        for (ShortcutInfo shortcutInfo : shortcutInfoList) {
            if (shortcutInfo.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private ShortcutManager getShortCutManager() {
        if (Build.VERSION.SDK_INT < 25) return null;

        Activity currentActivity = this.reactContext.getCurrentActivity();
        ShortcutManager shortcutManager = currentActivity.getSystemService(ShortcutManager.class);

        return shortcutManager;
    }

}