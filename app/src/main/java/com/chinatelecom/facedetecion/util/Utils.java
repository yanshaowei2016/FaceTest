package com.chinatelecom.facedetecion.util;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class Utils {
	/**
	 * 将金额从以元为单位转换以为分为单位
	 *
	 * @param yuan
	 * @return fen
	 */
	public static long convertYuanToFen(String yuan) {
		long fen = 0;
		if (yuan.startsWith(".")) {
			yuan = "0" + yuan;
		}
		BigDecimal base = new BigDecimal(yuan);
		BigDecimal multiple = new BigDecimal(100);
		fen = base.multiply(multiple).longValue();
		return fen;
	}

	/**
	 * 将金额从以元为单位转换以为分为单�??
	 *
	 * @param yuan
	 * @return fen
	 */
	public static String yuanToFen(String yuan) {

		if (yuan.startsWith(".")) {
			yuan = "0" + yuan;
		}
		BigDecimal bd = new BigDecimal(yuan);
		String currency = bd.multiply(new BigDecimal("100")).toString();
		if (currency.indexOf(".") > 0) {
			currency = currency.substring(0, currency.indexOf("."));
		}
		return currency;
	}

	/**
	 * 将金额从以分为单位转换为以元为单位
	 *
	 * @param fen
	 * @return yuan
	 */
	public static String fenToYuan(String fen) {
		// 防止空
		if (TextUtils.isEmpty(fen)) {
			return "0";
		}
		BigDecimal bd = new BigDecimal(fen);
		String fenStr = bd.divide(new BigDecimal("100")).toPlainString();
		return fenStr;
	}

	/**
	 *
	 * @Method_name: dp2px
	 * @Description: TODOdp转换为像素px
	 * @param context
	 * @param dp
	 * @return
	 * @return_type: int
	 * @throws
	 * @author 对应开发人员
	 */
	public static int dp2px(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		return (int) ((dp * displayMetrics.density) + 0.5);
	}

	/**
	 *
	 * @Method_name: px2sp
	 * @Description: TODO像素px转换为sp
	 * @param context
	 * @param pxValue
	 * @return
	 * @return_type: int
	 * @throws
	 * @author 对应开发人员：yansw
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 获取WIfi MAC 地址
	 *
	 * @param ctx
	 * @return
	 */
	public static String getWifiMac(Context ctx) {
		WifiManager wifi = (WifiManager) ctx
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 获得蓝牙MAC
	 *
	 * @return
	 */
	public static String getBTMac() {
		return "";
	}

	public static String getSysVersion() {
		return Build.VERSION.RELEASE;
	}

	public static String getImsi(Context ctx) {
		TelephonyManager teleM = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = teleM.getSubscriberId();
		if (imsi == null) {
			imsi = "";
		}
		return imsi;
	}

	public static String getImei(Context ctx) {
		TelephonyManager teleM = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = teleM.getDeviceId();
		if (imei == null) {
			imei = "";
		}
		return imei;
	}

	public static String getDeviceName() {
		return Build.MODEL;
	}

	public static String getAppVersion(Context ctx) {
		// 获取当前版本号
		String currentVersion = "0.0.0";
		try {
			currentVersion = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName
					.toString();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentVersion;
	}

	public static boolean getNetworkIsAvailabe(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo nf = cm.getActiveNetworkInfo();
		if (nf == null) {
			return false;
		}
		if (nf.isConnected()) {
			return true;
		}
		return false;
	}
}
