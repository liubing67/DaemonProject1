package example.fussen.daemon;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import example.fussen.daemon.utils.RequestPermissionsUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (RequestPermissionsUtil.requestPer(MainActivity.this, Manifest.permission.RECEIVE_BOOT_COMPLETED, 1)) {
            promptNoPermission(R.string.btn_setting);
        } else {
            promptNoPermission(R.string.btn_setting);
        }

    }


    //权限请求回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                RequestPermissionsUtil.onRequestPermissionsResult(MainActivity.this, Manifest.permission.RECEIVE_BOOT_COMPLETED, requestCode, permissions, grantResults);
                break;
            default:
                break;
        }

    }

    /**
     * 请求权限是底部弹出提示
     */

    private void promptNoPermission(int res) {
        Snackbar.make(MainActivity.this.findViewById(android.R.id.content), res, Snackbar.LENGTH_LONG).setAction(R.string.btn_setting, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                        Uri.parse("package:" + MainActivity.this.getPackageName()));
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this.getPackageName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
                intent.setComponent(comp);
                startActivity(intent);
            }
        }).show();
    }
    /**
     * 防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
     */
    @Override
    public void onBackPressed() {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(launcherIntent);
    }
}
