package com.ggcode.devknife.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import com.ggcode.devknife.base.component.Component;

/**
 * @author: zbb 33775
 * @date: 2019/5/28 10:56
 * @desc:
 */
public class AppUtils {

    private AppUtils() {
        throw new AssertionError("u can't instantiate me...");
    }

    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static boolean isAppDebug() {
        return isAppDebug(Component.app().getPackageName());
    }

    public static boolean isAppDebug(final String packageName) {
        if (isSpace(packageName)) {
            return false;
        }
        try {
            PackageManager pm = Component.app().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAppSystem() {
        return isAppSystem(Component.app().getPackageName());
    }

    public static boolean isAppSystem(final String packageName) {
        if (isSpace(packageName)) {
            return false;
        }
        try {
            PackageManager pm = Component.app().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Drawable getAppIcon(final String packageName) {
        if (isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = Component.app().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppPackageName() {
        return Component.app().getPackageName();
    }

    public static String getAppName(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Component.app().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAppPath(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Component.app().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAppVersionName(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Component.app().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static PackageInfo getPackageInfo(String packageName) {
        PackageInfo pi = null;
        try {
            PackageManager pm = Component.app().getPackageManager();
            pi = pm.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    public static int getAppVersionCode(final String packageName) {
        if (isSpace(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = Component.app().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Signature[] getAppSignature(final String packageName) {
        if (isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = Component.app().getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppSignatureSHA1(final String packageName) {
        return getAppSignatureHash(packageName, "SHA1");
    }

    public static String getAppSignatureSHA256(final String packageName) {
        return getAppSignatureHash(packageName, "SHA256");
    }

    public static String getAppSignatureMD5(final String packageName) {
        return getAppSignatureHash(packageName, "MD5");
    }

    private static String getAppSignatureHash(final String packageName, final String algorithm) {
        if (isSpace(packageName)) {
            return "";
        }
        Signature[] signature = getAppSignature(packageName);
        if (signature == null || signature.length <= 0) {
            return "";
        }
        return bytes2HexString(hashTemplate(signature[0].toByteArray(), algorithm))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        int len = bytes.length;
        if (len <= 0) {
            return "";
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getLauncherActivityName(final String packageName) {
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageName);
        PackageManager packageManager = Component.app().getPackageManager();
        List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);

        if (apps.size() != 0) {
            ResolveInfo ri = apps.get(0);
            if (ri != null) {
                return ri.activityInfo.name;
            }
        }
        return "无";
    }

    public static String getSdkVersionDesc(int sdkVersion) {
        String sdkVersionCode = null;
        String sdkVersionNmae = null;
        switch (sdkVersion) {
            case 17:
                sdkVersionCode = "4.2";
                sdkVersionNmae = "Jelly Bean";
                break;
            case 18:
                sdkVersionCode = "4.3";
                sdkVersionNmae = "Jelly Bean";
                break;
            case 19:
                sdkVersionCode = "4.4";
                sdkVersionNmae = "KitKat";
                break;
            case 20:
                sdkVersionCode = "4.4W";
                sdkVersionNmae = "KitKat Wear";
                break;
            case 21:
                sdkVersionCode = "5.0";
                sdkVersionNmae = "Lollipop";
                break;
            case 22:
                sdkVersionCode = "5.1";
                sdkVersionNmae = "Lollipop";
                break;
            case 23:
                sdkVersionCode = "6.0";
                sdkVersionNmae = "Marshmallow";
                break;
            case 24:
                sdkVersionCode = "7.0";
                sdkVersionNmae = "Nougat";
                break;
            case 25:
                sdkVersionCode = "7.1.1";
                sdkVersionNmae = "Nougat";
                break;
            case 26:
                sdkVersionCode = "8.0";
                sdkVersionNmae = "Oreo";
                break;
            case 27:
                sdkVersionCode = "8.1";
                sdkVersionNmae = "Oreo";
                break;
            case 28:
                sdkVersionCode = "9.0";
                sdkVersionNmae = "Pie";
                break;
            case 29:
                sdkVersionCode = "9.0";
                sdkVersionNmae = "Q";
                break;
            default:
                break;
        }
        if (sdkVersionCode == null) {
            return String.valueOf(sdkVersion);
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(sdkVersion)
                .append("(Android ")
                .append(sdkVersionCode)
                .append("，")
                .append(sdkVersionNmae)
                .append(")");
        return stringBuilder.toString();
    }

    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
