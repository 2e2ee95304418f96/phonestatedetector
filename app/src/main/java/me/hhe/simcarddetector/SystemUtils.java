package me.hhe.simcarddetector;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 系统工具类
 * Created by zhuwentao on 2016-07-18.
 */
public class SystemUtils {

    private static boolean hasPermission;

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getIMEI(Context context, boolean isSlot1) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "请在系统设置中打开天府通应用的读取电话状态权限", Toast.LENGTH_SHORT).show();
                return "";
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tm.getImei(isSlot1 ? 0 : 1);
            } else {
                return tm.getDeviceId();
            }
        }
        return null;
    }

    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String imsi = mTelephonyMgr.getSubscriberId();
            return imsi;
        } else {
            return "";
        }
    }

    /**
     * 获取iccid，此方法获取的是流量卡的id，底层源码是用subscription_id(IMEI)获取的对应的iccid，所以和getImei一样，是获取到的流量卡的值
     *
     * @param context
     * @return ""代表无权限，null代表无sim卡
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            String simSerialNumber = tm.getSimSerialNumber();
            return simSerialNumber;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static List<SubscriptionInfo> getSimInfoBySubscriptionManager(Context context) {
        List<SubscriptionInfo> iccidList = new ArrayList<>();

        if (PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return iccidList;
        }
        iccidList = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
        if (iccidList == null){
            return new ArrayList<>();
        }
        // 排序，保证按卡槽排序
        Collections.sort(iccidList, new Comparator<SubscriptionInfo>() {
            @Override
            public int compare(SubscriptionInfo subscriptionInfo, SubscriptionInfo t1) {
                return subscriptionInfo.getSimSlotIndex() - t1.getSimSlotIndex();
            }
        });
        return iccidList;
    }


}
