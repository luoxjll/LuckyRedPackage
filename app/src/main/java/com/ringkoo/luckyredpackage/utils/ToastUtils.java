package com.ringkoo.luckyredpackage.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ringkoo.luckyredpackage.application.LPApplication;
import com.ringkoo.luckyredpackage.R;

/**
 * @author xj.luo
 * @email xj_luo@foxmail.com
 * @date Created on 2020/10/30
 */

public class ToastUtils {
    public static Toast mToast = null;

    public static void toast(String cs) {
        if (mToast == null) {
            mToast = makeText(LPApplication.getInstance(), cs);
        } else {
            ((TextView) mToast.getView().findViewById(R.id.TextViewInfo)).setText(cs);
        }
        mToast.show();
    }

    private static Toast makeText(Context context, String msg) {
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
        tv.getBackground().setAlpha(100);
        tv.setText(msg);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }
}
