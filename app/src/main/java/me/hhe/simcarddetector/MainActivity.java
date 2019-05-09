package me.hhe.simcarddetector;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvPermission;
    private TextView tvdevice;
    private TextView tviccid;
    private TextView tvimei1;
    private TextView tvimei2;
    private TextView tvimsi;
    private TextView tvsimoperator;
    private TextView tvoperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1001:
                loadData();
                break;
            default:
                break;
        }
    }

    private void initView() {
        tvPermission = findViewById(R.id.tv_permission);
        tvdevice = findViewById(R.id.tv_device);
        tviccid = findViewById(R.id.tv_iccid);
        tvimei1 = findViewById(R.id.tv_imei1);
        tvimei2 = findViewById(R.id.tv_imei2);
        tvimsi = findViewById(R.id.tv_imsi);
        tvsimoperator = findViewById(R.id.tv_simoperator);
        tvoperator = findViewById(R.id.tv_operator);

        tvPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1001);
            }
        });


        setOnLongClickCopy(tvdevice);
        setOnLongClickCopy(tviccid);
        setOnLongClickCopy(tvimei1);
        setOnLongClickCopy(tvimei2);
        setOnLongClickCopy(tvimsi);
        setOnLongClickCopy(tvsimoperator);
        setOnLongClickCopy(tvoperator);
    }


    private void loadData() {
        boolean b = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PermissionChecker.PERMISSION_GRANTED;
        tvPermission.setText(b ? "有权限" : "无权限");

        if (!b) {
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
        tviccid.setText(SystemUtils.getICCID(this));
        tvimei1.setText(SystemUtils.getIMEI(this, true));
        tvimei2.setText(SystemUtils.getIMEI(this, false));
        tvimsi.setText(SystemUtils.getIMSI(this));
        tvsimoperator.setText(CardUtils.getSimOperatorByMnc(this));
        tvoperator.setText(CardUtils.getSimOperatorName(this));
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
