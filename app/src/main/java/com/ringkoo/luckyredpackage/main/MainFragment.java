package com.ringkoo.luckyredpackage.main;

import androidx.lifecycle.ViewModelProviders;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.ringkoo.luckyredpackage.BuildConfig;
import com.ringkoo.luckyredpackage.R;
import com.ringkoo.luckyredpackage.utils.AccessibilityHelper;
import com.ringkoo.luckyredpackage.utils.ToastUtils;

import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private AccessibilityManager accessibilityManager;

    private LinearLayout accessibilityControlLayout;

    //开关切换按钮
    private TextView pluginStatusText;
    private ImageView pluginStatusIcon;
    private LinearLayout settingView;

    private AdView mAdView;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pluginStatusText = (TextView) view.findViewById(R.id.layout_control_accessibility_text);
        pluginStatusIcon = (ImageView) view.findViewById(R.id.layout_control_accessibility_icon);

        settingView = view.findViewById(R.id.layout_control_settings);
        settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings(v);
            }


        });

        accessibilityControlLayout = view.findViewById(R.id.layout_control_accessibility);

        accessibilityControlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibility(v);
            }
        });

        LinearLayout communityView = view.findViewById(R.id.layout_control_community);
        communityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_helpFragment);
            }
        });

        mAdView = view.findViewById(R.id.id_adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TextView versionView = view.findViewById(R.id.id_version_text);
        String version = BuildConfig.VERSION_NAME;
        versionView.setText("软件版本: " + version);
    }

    @Override
    public void onResume() {
        super.onResume();

        accessibilityManager = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(accessibilityStateChangeListener);

        updateServiceStatus();

        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //移除监听服务
        accessibilityManager.removeAccessibilityStateChangeListener(accessibilityStateChangeListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    private AccessibilityManager.AccessibilityStateChangeListener accessibilityStateChangeListener =
            new AccessibilityManager.AccessibilityStateChangeListener() {
                @Override
                public void onAccessibilityStateChanged(boolean enabled) {
                    updateServiceStatus();
                }
            };

    private void openSettings(View v) {
        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_settingFragment);
    }

    public void openAccessibility(View view) {
        try {
            Toast.makeText(view.getContext(), getString(R.string.turn_on_toast) + pluginStatusText.getText(), Toast.LENGTH_LONG).show();
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        } catch (Exception e) {
            Toast.makeText(view.getContext(), getString(R.string.turn_on_error_toast), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    /**
     * 更新当前 HongbaoService 显示状态
     */
    private void updateServiceStatus() {
        if (isServiceEnabled()) {
            pluginStatusText.setText(R.string.service_off);
            pluginStatusIcon.setBackgroundResource(R.mipmap.ic_stop);
        } else {
            pluginStatusText.setText(R.string.service_on);
            pluginStatusIcon.setBackgroundResource(R.mipmap.ic_start);
        }
    }

    /**
     * 获取 HongbaoService 是否启用状态
     *
     * @return
     */
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(this.getContext().getPackageName() + "/.service.AssistBackgroundService")) {
                return true;
            }
        }
        return false;
    }

}