package com.ringkoo.luckyredpackage.service;

import android.accessibilityservice.AccessibilityService;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.ringkoo.luckyredpackage.config.ConfigManger;
import com.ringkoo.luckyredpackage.utils.AccessibilityHelper;
import com.ringkoo.luckyredpackage.utils.Logg;
import com.ringkoo.luckyredpackage.utils.ScreenUtil;


/**
 * @author xj.luo
 * @email xj_luo@foxmail.com
 * @date Created on 2020/11/13
 */

public class AssistBackgroundService extends AccessibilityService {

    private static final String CHILD_TAG = AssistBackgroundService.class.getSimpleName();

    /**
     * 红包弹出的class的名字
     */
    private static final String ACTIVITY_DIALOG_LUCKYMONEY = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";

    /**
     * 红包详情页
     */
    private static String LUCKY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";

    /**
     * 聊天列表页 class name
     */
    private static String CHAT_LIST_PAGE = " com.tencent.mm.ui.LauncherUI";

    /**
     * 获取屏幕宽高
     */
    private int screenWidth = ScreenUtil.SCREEN_WIDTH;
    private int screenHeight = ScreenUtil.SCREEN_HEIGHT;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    private boolean isHongBaoOpen = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 每当界面改变的时候就会回调这个方法，通过event我们就可以获取到界面的信息包括界面上的控件

        //当前类名
        String className = event.getClassName().toString();
        Logg.i(CHILD_TAG, "当前类名为  " + className);

        //当前为红包弹出窗
        if (className.equals(ACTIVITY_DIALOG_LUCKYMONEY)) {
            Logg.i(CHILD_TAG, "当前为红包弹出框页面");
            int delay = ConfigManger.getInstance().getOpenDelayTime();
            Logg.i(CHILD_TAG, "当前为红包弹出框页面,延时 " + delay + "毫秒点击 开 ");
            SystemClock.sleep(delay);
            AccessibilityHelper.openPackage(this);
            isHongBaoOpen = true;
            return;
        }

        //红包领取后的详情页面，自动返回
        if (className.equals(LUCKY_MONEY_DETAIL)) {
            //返回聊天界面
            if (isHongBaoOpen) {
                SystemClock.sleep(1000);
                performGlobalAction(GLOBAL_ACTION_BACK);
                isHongBaoOpen = false;
            }
            return;
        }

        AccessibilityNodeInfo hongBaoParent = AccessibilityHelper.findHongBaoNode2(this, event);
        if (hongBaoParent != null) {
            Logg.i(CHILD_TAG, "找到红包，点击红包");
            boolean success = AccessibilityHelper.clickHongbao(hongBaoParent);
            Logg.i(CHILD_TAG, success ? "红包被点击了 " : "红包没有被点击 ");
        }
    }

    @Override
    public void onInterrupt() {

    }

}
