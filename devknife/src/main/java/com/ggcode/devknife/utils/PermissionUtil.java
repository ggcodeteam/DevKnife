package com.ggcode.devknife.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.ggcode.devknife.R;
import com.ggcode.devknife.base.component.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: zbb 33775
 * @date: 2019/5/16 10:59
 * @desc:
 */
public class PermissionUtil {

    private static final String TAG = "PermissionUtil";

    private static final int OP_SYSTEM_ALERT_WINDOW = 24;

    public static void requestDrawOverlays(Context context) {
        if (!canDrawOverlays(context)) {
            Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION",
                    Uri.parse("package:" + context.getPackageName()));
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                ToastUtil.show(UIUtils.getString(R.string.dk_float_permission_toast));
                context.startActivity(intent);
            } else {
                LogUtils.e(TAG, "No activity to handle intent");
            }
        }
    }

    private static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return checkAndroidOP(context, OP_SYSTEM_ALERT_WINDOW);
    }

    private static boolean checkAndroidOP(Context context, int op) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class clazz = AppOpsManager.class;
            try {
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method
                        .invoke(manager, op, Process.myUid(), context.getPackageName());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean requestPermission(Activity context, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    public static boolean onPermissionResult(Activity context, int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        return onPermissionResult(context, requestCode, permissions, grantResults,
                Component.app().getString(R.string.dk_deny_permission_toast));
    }

    public static boolean onPermissionResult(Activity activity, int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults, @Nullable String content) {
        int num = 0;//禁止且不再弹窗个数
        int not_granted_num = 0;//未授权个数
        boolean granted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                not_granted_num++;
                granted = false;
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                    num++;
                }
            }
        }
        if (granted) {
            return true;
        }
        //此时需要弹窗去授权
        if (num == not_granted_num) {
            ToastUtil.show(content);
            return false;
        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
        return false;
    }

    public static boolean hasPermission(Context context, String permission) {
        if (context == null || permission == null || permission.length() == 0) {
            return false;
        }
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
