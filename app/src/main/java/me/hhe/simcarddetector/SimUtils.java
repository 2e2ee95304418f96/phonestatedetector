package me.hhe.simcarddetector;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.PermissionChecker;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * author:hhe
 * email:hhecoder@gmail.com
 * createTime:2019/5/10
 * discription:
 */
public class SimUtils {

    public enum Operator {
        /**
         *
         */
        Telecom("中国电信"),
        Mobile("中国移动"),
        Unicom("中国联通"),
        None("未获取到");

        public String name;
        Operator(String name) {
            this.name=name;
        }
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

    public static String getMnc(Context context, int slotIndex) {
        List<SubscriptionInfo> list = getSimInfoBySubscriptionManager(context);
        if (slotIndex ==0 && list.size() > 0){
            return list.get(0).getMnc()+"";
        }
        if (slotIndex ==1 && list.size() > 1){
            return list.get(1).getMnc()+"";
        }
        return "";
    }

    public static Operator getOperator(Context context, int slotIndex) {
        String mnc = getMnc(context, slotIndex);
        switch (mnc){
            case "0":
            case "2":
            case "7":
                return Operator.Mobile;
            case "1":
            case "6":
                return Operator.Unicom;
            case "3":
            case "5":
            case "8":
            case "9":
            case "10":
            case "11":
                return Operator.Telecom;
            default:
                return Operator.None;


        }
    }

    public static boolean isMobile(Context context, int slotIndex){
        return getOperator(context,slotIndex)==Operator.Mobile;
    }
    public static boolean isUnicom(Context context, int slotIndex){
        return getOperator(context,slotIndex)==Operator.Unicom;
    }
    public static boolean isTelecom(Context context, int slotIndex){
        return getOperator(context,slotIndex)==Operator.Telecom;
    }
}
