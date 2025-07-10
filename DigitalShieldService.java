package com.example.digitalshield; // Aapke package ka naam yahan aayega

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.HashSet;
import java.util.Set;

public class DigitalShieldService extends AccessibilityService {

    private SharedPreferences prefs;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        prefs = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        Set<String> keywords = prefs.getStringSet(MainActivity.KEYWORDS_SET, new HashSet<>());

        if (keywords.isEmpty()) {
            return;
        }

        AccessibilityNodeInfo source = event.getSource();
        if (source != null) {
            checkNodeForKeywords(source, keywords);
            source.recycle();
        }
    }

    private void checkNodeForKeywords(AccessibilityNodeInfo node, Set<String> keywords) {
        if (node == null) {
            return;
        }

        // Node ka text ya content description check karein
        CharSequence text = node.getText();
        CharSequence contentDesc = node.getContentDescription();

        String nodeText = (text != null ? text.toString().toLowerCase() : "");
        String nodeContentDesc = (contentDesc != null ? contentDesc.toString().toLowerCase() : "");

        for (String keyword : keywords) {
            if (nodeText.contains(keyword) || nodeContentDesc.contains(keyword)) {
                triggerLock();
                return; // Jaise hi keyword mile, lock trigger karein aur loop se bahar aa jayein
            }
        }

        // Child nodes ko bhi check karein
        for (int i = 0; i < node.getChildCount(); i++) {
            checkNodeForKeywords(node.getChild(i), keywords);
        }
    }



    private void triggerLock() {
        Intent lockIntent = new Intent(this, LockActivity.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(lockIntent);
    }

    @Override
    public void onInterrupt() {
        // Service interrupt hone par
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);
    }
}
