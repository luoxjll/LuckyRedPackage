package com.ringkoo.luckyredpackage.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import java.util.List;


/**
 * @author xj.luo
 * @email xj_luo@foxmail.com
 * @date Created on 2020/11/13
 */

public class AccessibilityHelper {

    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";
    private static final String WECHAT_LUCKMONEY_CHATTING_ACTIVITY = "ChattingUI";

    private static final String CHILD_TAG = AccessibilityHelper.class.getSimpleName();

    public static void testEventInfo(AccessibilityService service, AccessibilityEvent event) {
        try {

            //获取当前界面包名
            String packageName = event.getPackageName().toString();
            //获取当前类名
            String className = event.getClassName().toString();

            Logg.i(CHILD_TAG, "wechat layout current package name " + packageName);
            Logg.i(CHILD_TAG, "wechat layout current class name " + className);

            //获取当前界面父布局的控件
            AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();

            if (accessibilityNodeInfo != null) {
                int childCount = accessibilityNodeInfo.getChildCount();
                Logg.i(CHILD_TAG, "父布局下的子节点个数 " + childCount);

                CharSequence className1 = accessibilityNodeInfo.getClassName();
                Logg.i(CHILD_TAG, "父布局的类名  " + className1);

                CharSequence text = accessibilityNodeInfo.getText();
                Logg.i(CHILD_TAG, "父布局的text  " + text);

                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);

                        String childName = child.getClassName().toString();
                        Logg.i(CHILD_TAG, "父布局的子节点类名  " + childName);
                        boolean clickable = child.isClickable();
                        Logg.i(CHILD_TAG, "父布局的子节点  " + (clickable ? "可点击" : "不可点击"));

                        // 这个子节点是那个 X
                        if ("android.widget.ImageView".equals(childName)) {
                            // child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }

                        // 这个就是 开 的那个button
                        if ("android.widget.Button".equals(childName)) {
                            //  child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static AccessibilityNodeInfo findHongBaoNode2(AccessibilityService service, AccessibilityEvent event) {
        try {
            //获取当前界面父布局的控件
            AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();

            if (accessibilityNodeInfo != null) {
                String text = "微信红包";
                // 通过内容找到有微信红包的控件
                List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);

                if (nodeInfoList != null && nodeInfoList.size() > 0) {
                    for (int i = 0; i < nodeInfoList.size(); i++) {
                        AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);

                        Logg.i(CHILD_TAG, "通过内容找到的节点类名  " + nodeInfo.getClassName());
                    }

                    // 获取最后一个节点, 带这个信息的控件一般为红包（打开的 未打开的）
                    AccessibilityNodeInfo lastNode = getLastNode(nodeInfoList);
                    if (lastNode != null) {
                        AccessibilityNodeInfo parent = lastNode.getParent();
                        if (parent != null) {
                            int childCount = parent.getChildCount();
                            Logg.i(CHILD_TAG, "包含内容  " + text + " 的节点父节点下的子节点的个数 " + childCount);
                            if (childCount > 0) {
                                boolean isWeChatHongBao = false;
                                boolean isUnopened = true;
                                for (int i = 0; i < childCount; i++) {
                                    AccessibilityNodeInfo child = parent.getChild(i);
                                    if (child != null) {
                                        String className = child.getClassName().toString();
                                        Logg.i(CHILD_TAG, "包含内容  " + text + " 的节点父节点下的子节点的类名 " + className);

                                        if ("android.widget.TextView".equals(className)) {
                                            String contentText = child.getText().toString();
                                            Logg.i(CHILD_TAG, "包含内容  " + text + " 的节点父节点下的子节点的text为 " + contentText);

                                            if ("微信红包".equals(contentText)) {
                                                isWeChatHongBao = true;
                                            }
                                            if ("已领取".equals(contentText)) {
                                                isUnopened = false;
                                            }

                                            if ("已被领完".equals(contentText)) {
                                                isUnopened = false;
                                            }
                                            if ("已过期".equals(contentText)) {
                                                isUnopened = false;
                                            }
                                        }
                                    }
                                }

                                if (isWeChatHongBao && isUnopened) {
                                    return parent;
                                }

                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 有可能一个界面上，有多个红包，需要选择最后一个红包
    private static AccessibilityNodeInfo getLastNode(List<AccessibilityNodeInfo> nodeInfoList) {
        if (nodeInfoList == null) {
            return null;
        }
        if (nodeInfoList.size() > 0) {
            AccessibilityNodeInfo accessibilityNodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
            return accessibilityNodeInfo;
        }

        return null;
    }

    public static boolean clickHongbao(AccessibilityNodeInfo parent) {
        if (parent != null) {
            boolean clickable = parent.isClickable();
            String result = clickable ? "enable" : "disable";
            Logg.i(CHILD_TAG, "parent is click " + result);
            boolean success = parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);

            return success;
        }

        return false;
    }

    public static void openPackage(AccessibilityService service) {
        try {
            //获取当前界面父布局的控件
            AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();

            if (accessibilityNodeInfo != null) {
                int childCount = accessibilityNodeInfo.getChildCount();
                Logg.i(CHILD_TAG, "父布局下的子节点个数 " + childCount);

                CharSequence className1 = accessibilityNodeInfo.getClassName();
                Logg.i(CHILD_TAG, "父布局的类名  " + className1);

                CharSequence text = accessibilityNodeInfo.getText();
                Logg.i(CHILD_TAG, "父布局的text  " + text);

                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);

                        String childName = child.getClassName().toString();
                        Logg.i(CHILD_TAG, "父布局的子节点类名  " + childName);
                        boolean clickable = child.isClickable();
                        Logg.i(CHILD_TAG, "父布局的子节点  " + (clickable ? "可点击" : "不可点击"));

                        if ("android.widget.TextView".equals(childName)) {
                            String textContent = child.getText().toString();
                            Logg.i(CHILD_TAG, "text view content " + textContent);
                        }

                        // 这个子节点是那个 X
                        if ("android.widget.ImageView".equals(childName)) {
                            // child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }

                        // 这个就是 开 的那个button
                        if ("android.widget.Button".equals(childName)) {
                            child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //----------------------------------------手势实现---------------------------------

    /**
     * 屏幕上的手势
     *
     * @param service
     * @param path
     * @param startTime
     * @param duration
     * @param callback
     */
    protected void gestureOnScreen(AccessibilityService service, Path path, long startTime, long duration,
                                   AccessibilityService.GestureResultCallback callback) {

        GestureDescription.Builder builde = new GestureDescription.Builder();
        builde.addStroke(new GestureDescription.StrokeDescription(path, startTime, duration));

        GestureDescription gestureDescription = builde.build();
        service.dispatchGesture(gestureDescription, callback, null);
    }

    /**
     * 长按指定位置
     * 注意7.0以上的手机才有此方法，请确保运行在7.0手机上
     */
    @RequiresApi(24)
    public void dispatchGestureLongClick(AccessibilityService service, int x, int y) {
        Path path = new Path();
        path.moveTo(x - 1, y - 1);
        path.lineTo(x, y - 1);
        path.lineTo(x, y);
        path.lineTo(x - 1, y);
        service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription
                (path, 0, 1000)).build(), null, null);
    }

    /**
     * 立即发送移动的手势
     * 注意7.0以上的手机才有此方法，请确保运行在7.0手机上
     *
     * @param path  移动路径
     * @param mills 持续总时间
     */
    @RequiresApi(24)
    public void dispatchGestureMove(AccessibilityService service, Path path, long mills) {
        service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription
                (path, 0, mills)).build(), null, null);
    }

    /**
     * 由于太多,最好回收这些AccessibilityNodeInfo
     */
    public static void recycleAccessibilityNodeInfo(List<AccessibilityNodeInfo> listInfo) {

        if (listInfo == null || listInfo.size() == 0) {
            return;
        }

        for (AccessibilityNodeInfo info : listInfo) {
            info.recycle();
        }
    }

    public static void dispatchGestureLongClick(AccessibilityService service, AccessibilityNodeInfo nodeInfo) {

        Rect absXY = new Rect();
        nodeInfo.getBoundsInScreen(absXY);

        int x = absXY.left + (absXY.right - absXY.left) / 2;
        int y = absXY.top + (absXY.bottom - absXY.top) / 2;

        Logg.i(CHILD_TAG, " x   ------------     " + x);
        Logg.i(CHILD_TAG, " y   ------------     " + y);

        Path path = new Path();
        path.moveTo(x, y);

        GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription(path,
                0,
                500);

        GestureDescription gesture = new GestureDescription.Builder().addStroke(strokeDescription).build();
        boolean b = service.dispatchGesture(gesture,
                new AccessibilityService.GestureResultCallback() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        super.onCompleted(gestureDescription);

                        Logg.i(CHILD_TAG, "dispatch gesture success ");
                    }

                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {
                        super.onCancelled(gestureDescription);

                        Logg.i(CHILD_TAG, "dispatch gesture failure ");

                    }
                }, null);

        Logg.i(CHILD_TAG, "dispatch return is " + b);
    }

    /**
     * 长按指定位置
     * 注意7.0以上的手机才有此方法，请确保运行在7.0手机上
     */
    @RequiresApi(24)
    private static void dispatchGestureLongClick2(AccessibilityService service, int x, int y) {
        Path path = new Path();
        path.moveTo(x - 1, y - 1);
        path.lineTo(x, y - 1);
        path.lineTo(x, y);
        path.lineTo(x - 1, y);
        service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription
                (path, 0, 1000)).build(), null, null);
    }

    /**
     * 点击指定位置
     * 注意7.0以上的手机才有此方法，请确保运行在7.0手机上
     */
    @RequiresApi(24)
    public static void dispatchGestureClick(AccessibilityService service) {

        DisplayMetrics metrics = service.getApplicationContext().getResources().getDisplayMetrics();
        float dpi = metrics.densityDpi;
        Log.d(CHILD_TAG, "device screen dpi " + dpi);

        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;

        // width 1080   height 2250
        Logg.i(CHILD_TAG, "screen height pixels " + heightPixels);
        Logg.i(CHILD_TAG, "screen width pixels " + widthPixels);

        Path path = new Path();
        if (640 == dpi) { //1440
            path.moveTo(720, 1575);
        } else if (320 == dpi) {//720p
            path.moveTo(355, 780);
        } else if (480 == dpi) {//1080p

            path.moveTo(572, 1687);

        }

        GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription(path,
                0,
                300);
        GestureDescription gesture = new GestureDescription.Builder().addStroke(strokeDescription).build();
        boolean b = service.dispatchGesture(gesture,
                new AccessibilityService.GestureResultCallback() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        super.onCompleted(gestureDescription);

                        Logg.i(CHILD_TAG, "dispatch gesture success ");
                    }

                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {
                        super.onCancelled(gestureDescription);

                        Logg.i(CHILD_TAG, "dispatch gesture failure ");

                    }
                }, null);

        Logg.i(CHILD_TAG, "dispatch return is " + b);
    }

}
