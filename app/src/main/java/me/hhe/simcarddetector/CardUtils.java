package me.hhe.simcarddetector;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


/**
 * author:hhe
 * createTime:2018/11/2
 * discription: 卡片相关
 */
public class CardUtils {
    private static final String EMPTY_CARD_NUMBER = "0000000000000000";


    /**
     * 获取Sim卡运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 移动网络运营商名称
     */
    public static String getSimOperatorByMnc(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        // getSimOperator()
        // Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the
        // SIM.
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

    public static boolean isNFCSupport(PackageManager packageManager) {
        boolean isSupport = false;
        if (packageManager != null) {
            isSupport = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
        }
        return isSupport;
    }

    /**
     * nfc功能是否打开
     */
    public static boolean isNFCOpen(Context context){
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter!=null&&nfcAdapter.isEnabled()){
            return true;
        }
        return false;
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

    /**
     * 是否支持oma，不论官方or非官方
     * @return
     */
    public static boolean isSupportOma(){
        return isSupportOldOma()||isSupportGoogleOma();
    }

//    public static boolean isHuaweiPhone(){
//        String brand =SystemUtils.getDeviceBrand();
//        brand=brand.toLowerCase();
//        if (TFTUtils.isProductEnvironment()){
//            TUtils.showShort(brand);
//        }
//        return brand.equals("honor")
//                || brand.equals("huawei")
//                || brand.equals("nova");
//    }


}
