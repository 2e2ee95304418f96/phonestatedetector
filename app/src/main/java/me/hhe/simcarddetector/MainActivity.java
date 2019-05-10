package me.hhe.simcarddetector;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvPermission;
    private TextView tvdevice;
    private TextView tvserialnumber;
    private TextView tviccid1;
    private TextView tviccid2;
    private TextView tvimei1;
    private TextView tvimei2;
    private TextView tvimsi;
    private TextView tvsimoperator1;
    private TextView tvsimoperator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadData();
    }

    private void initView() {
        tvPermission = findViewById(R.id.tv_permission);
        tvdevice = findViewById(R.id.tv_device);
        tvserialnumber = findViewById(R.id.tv_serialnumber);
        tviccid1 = findViewById(R.id.tv_iccid1);
        tviccid2 = findViewById(R.id.tv_iccid2);
        tvimei1 = findViewById(R.id.tv_imei1);
        tvimei2 = findViewById(R.id.tv_imei2);
        tvimsi = findViewById(R.id.tv_imsi);
        tvsimoperator1 = findViewById(R.id.tv_simoperator1);
        tvsimoperator2 = findViewById(R.id.tv_simoperator2);

        tvPermission.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                loadData();
            }
        });


        setOnLongClickCopy(tvdevice);
        setOnLongClickCopy(tvserialnumber);
        setOnLongClickCopy(tviccid1);
        setOnLongClickCopy(tviccid2);
        setOnLongClickCopy(tvimei1);
        setOnLongClickCopy(tvimei2);
        setOnLongClickCopy(tvimsi);
        setOnLongClickCopy(tvsimoperator1);
        setOnLongClickCopy(tvsimoperator2);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void loadData() {
        tvPermission.setText("");
        tvdevice.setText("");
        tvserialnumber.setText("");
        tviccid1.setText("");
        tviccid1.setText("");
        tvimei1.setText("");
        tvimei2.setText("");
        tvimsi.setText("");
        tvsimoperator1.setText("");
        tvsimoperator2.setText("");

        boolean b = PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PermissionChecker.PERMISSION_GRANTED;
        tvPermission.setText(b ? "有权限，点击再次查询" : "无权限");
        if (!b) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1001);
            return;
        }

        String deviceInfo = new StringBuilder()
                .append("手机厂商: ")
                .append(SystemUtils.getDeviceBrand())
                .append("，手机型号: ")
                .append(SystemUtils.getSystemModel())
                .append("，手机系统版本:  ")
                .append(SystemUtils.getSystemVersion())
                .toString();
        tvdevice.setText(deviceInfo);
        tvserialnumber.setText(SystemUtils.getSimSerialNumber(this));
        List<SubscriptionInfo> iccidList = SimUtils.getSimInfoBySubscriptionManager(this);
        tviccid1.setText(iccidList.size() > 0 ? iccidList.get(0).getIccId(): "");
        tviccid2.setText(iccidList.size() > 1 ? iccidList.get(1).getIccId(): "");
        tvimei1.setText(SystemUtils.getIMEI(this, true));
        tvimei2.setText(SystemUtils.getIMEI(this, false));
        tvimsi.setText(SystemUtils.getIMSI(this));
        tvsimoperator1.setText(SimUtils.getOperator(this,0).name);
        tvsimoperator2.setText(SimUtils.getOperator(this,1).name);
    }

    /**
     * 设置长按复制
     */
    private void setOnLongClickCopy(final TextView textView) {
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String text = textView.getText().toString();

                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(MainActivity.this, "内容为空", Toast.LENGTH_SHORT).show();
                    return true;
                }
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", text);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(MainActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }
}
