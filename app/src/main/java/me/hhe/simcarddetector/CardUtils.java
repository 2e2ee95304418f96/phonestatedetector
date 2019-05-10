package me.hhe.simcarddetector;

import android.content.Context;
import android.telephony.TelephonyManager;


/**
 * author:hhe
 * createTime:2018/11/2
 * discription: 卡片相关
 */
public class CardUtils {
    /**
     * 获取Sim卡运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 移动网络运营商名称
     */
    public static String getSimOperatorNameBySystem(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm != null ? tm.getSimOperator() : null;
        if (operator == null) {
            return null;
        }
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
                return "中国移动";
            case "46001":
            case "46006":
                return "中国联通";
            case "46003":
            case "46005":
            case "46008":
            case "46009":
            case "46010":
            case "46011":
                return "中国电信";
            default:
                return operator;
        }
    }
    /**
     * 获取Sim卡运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 移动网络运营商名称
     */
    public static String getSimOperatorByMnc(String mnc) {
        switch (mnc) {
            case "00":
            case "02":
            case "07":
                return "中国移动";
            case "01":
            case "06":
                return "中国联通";
            case "03":
            case "05":
            case "08":
            case "09":
            case "10":
            case "11":
                return "中国电信";
            default:
                return "";
        }
    }


    /**
     * 获取Sim卡运营商名称
     * 注意：一些卡片返回为空或空字符串
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return sim卡运营商名称
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        return tm != null ? tm.getSimOperatorName() : null;
    }

    /**
     * 是否支持非官方的oma
     * @return
     */
    public static boolean isSupportOldOma(){
        try {
            Class.forName("org.simalliance.openmobileapi.SEService");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }
    /**
     * 是否支持官方的oma
     * @return
     */
    public static boolean isSupportGoogleOma(){
        try {
            Class.forName("android.se.omapi.SEService");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }


}
