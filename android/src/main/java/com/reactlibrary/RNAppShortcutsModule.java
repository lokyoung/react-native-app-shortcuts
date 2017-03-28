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
      Activity currentActivity = this.reactContext.getCurrentActivity();
      String shortCutId = currentActivity.getIntent().getStringExtra("shortcutId");
      if (shortCutId != null) {
          successCallback.invoke(shortCutId);
      }
  }

  @ReactMethod
  public void setShortcut(String id, String shortLabel, String longLabel) {
      Activity currentActivity = this.reactContext.getCurrentActivity();
      Intent intent = new Intent(currentActivity.getApplicationContext(), MainActivity.class);
      intent.putExtra("shortcutId", id);
      intent.setAction(Intent.ACTION_VIEW);

      if (Build.VERSION.SDK_INT < 25) return;

      ShortcutManager shortcutManager = currentActivity.getSystemService(ShortcutManager.class);

      ShortcutInfo shortcut = new ShortcutInfo.Builder(currentActivity, id)
          .setShortLabel(shortLabel)
          .setLongLabel(longLabel)
          .setIcon(Icon.createWithResource(currentActivity.getApplicationContext(), R.drawable.icon))
          .setIntent(intent)
          .build();

      shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
  }

}