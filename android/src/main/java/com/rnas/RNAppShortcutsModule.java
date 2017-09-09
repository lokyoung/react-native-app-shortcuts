package com.rnas;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;

public class RNAppShortcutsModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final String SHORTCUT_NOT_EXIST = "SHORTCUT_NOT_EXIST";
    private final String DEFAULT_ACTIVITY = "MainActivity";
    private final String ID_KEY = "id";
    private final String SHORT_LABEL_KEY = "shortLabel";
    private final String LONG_LABEL_KEY = "longLabel";
    private final String ICON_FOLDER_KEY = "iconFolderName";
    private final String ICON_NAME_KEY = "iconName";
    private final String ACTIVITY_NAME_KEY = "activityName";
    private final String ORDER_KEY = "order";

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
    public void exists(String id, Promise promise) {
        if (Build.VERSION.SDK_INT < 25) return;

        if (isShortcutExist(id)) {
            promise.resolve(null);
        } else {
            promise.reject(SHORTCUT_NOT_EXIST, "Not found this app shortcut");
        }
    }

    @ReactMethod
    public void addShortcut(ReadableMap shortcutDetails) {
        if (Build.VERSION.SDK_INT < 25) return;
        if (isShortcutExist(shortcutDetails.getString(ID_KEY))) return;

        ShortcutInfo shortcut = initShortcut(shortcutDetails);

        ShortcutManager shortcutManager = getShortCutManager();
        List<ShortcutInfo> shortcuts = shortcutManager.getDynamicShortcuts();

        if (shortcuts.size() >= shortcutManager.getMaxShortcutCountPerActivity() &&
                shortcuts.size() > 0) {
            shortcuts.remove(shortcuts.size() - 1);
            shortcuts.add(0, shortcut);
            shortcutManager.setDynamicShortcuts(shortcuts);
        } else {
            shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcut));
        }
    }

    @ReactMethod
    public void updateShortcut(ReadableMap shortcutDetail) {
        if (Build.VERSION.SDK_INT < 25) return;

        if (isShortcutExist(shortcutDetail.getString(ID_KEY))) {
            String activityName = DEFAULT_ACTIVITY;
            if (shortcutDetail.getString(ACTIVITY_NAME_KEY) != null) {
                activityName = shortcutDetail.getString(ACTIVITY_NAME_KEY);
            }

            ShortcutInfo shortcut = initShortcut(shortcutDetail);

            ShortcutManager shortcutManager = getShortCutManager();
            shortcutManager.updateShortcuts(Arrays.asList(shortcut));
        } else {
            return;
        }
    }

    @Nullable
    private ShortcutInfo initShortcut(ReadableMap shortcutDetail) {
        if (Build.VERSION.SDK_INT < 25) return null;

        String activityName = DEFAULT_ACTIVITY;
        try {
            activityName = shortcutDetail.getString(ACTIVITY_NAME_KEY);
        } catch (Exception e) {

        }
        Activity currentActivity = this.reactContext.getCurrentActivity();
        Intent intent = new Intent(currentActivity.getApplicationContext(), currentActivity.getClass());
        intent.putExtra("shortcutId", shortcutDetail.getString(ID_KEY));
        intent.setAction(Intent.ACTION_VIEW);

        Context currentContext = currentActivity.getApplicationContext();
        int iconId = currentContext.getResources().getIdentifier(shortcutDetail.getString(ICON_NAME_KEY), shortcutDetail.getString(ICON_FOLDER_KEY), currentContext.getPackageName());

        ShortcutInfo shortcut = new ShortcutInfo.Builder(currentActivity, shortcutDetail.getString(ID_KEY))
                .setShortLabel(shortcutDetail.getString(SHORT_LABEL_KEY))
                .setLongLabel(shortcutDetail.getString(LONG_LABEL_KEY))
                .setRank(shortcutDetail.getInt(ORDER_KEY))
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
