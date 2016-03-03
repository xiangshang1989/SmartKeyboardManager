package geeklub.org.messageboarddemo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by HelloVass on 16/3/2.
 */
public class SupportSoftKeyboardUtil {

  private static final String TAG = SupportSoftKeyboardUtil.class.getSimpleName();

  private final static String NAME_PREF_SOFT_KEYBOARD = "name_pref_soft_keyboard";

  private static final String KEY_PREF_SOFT_KEYBOARD_HEIGHT = "key_pref_soft_keyboard_height";

  // 颜文字键盘默认高度 240dp
  private static final int DEFAULT_SOFT_KEYBOARD_HEIGHT = 240;

  private Activity mActivity;

  private SharedPreferences mSoftKeyboardSharedPreferences;

  private static SupportSoftKeyboardUtil mSupportSoftKeyboardUtil;

  private SupportSoftKeyboardUtil(Activity activity) {
    mActivity = activity;
    mSoftKeyboardSharedPreferences =
        activity.getSharedPreferences(NAME_PREF_SOFT_KEYBOARD, Context.MODE_PRIVATE);
  }

  public static SupportSoftKeyboardUtil getInstance(Activity activity) {
    if (mSupportSoftKeyboardUtil == null) {
      mSupportSoftKeyboardUtil = new SupportSoftKeyboardUtil(activity);
    }
    return mSupportSoftKeyboardUtil;
  }

  // 得到用户软键盘高度
  public int getSupportSoftKeyboardHeight() {

    int softKeyboardHeight = getCurrentSoftInputHeight();

    // 如果当前的键盘高度大于零，赶紧保存下来
    if (softKeyboardHeight > 0) {
      mSoftKeyboardSharedPreferences.edit()
          .putInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT, softKeyboardHeight)
          .apply();
    }

    // 如果当前键盘高度等于零，可能是被隐藏了，也可能是我的锅，那就使用之前的键盘高度或者默认键盘高度
    if (softKeyboardHeight == 0) {
      softKeyboardHeight = mSoftKeyboardSharedPreferences.getInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT,
          DensityUtil.dip2px(mActivity, DEFAULT_SOFT_KEYBOARD_HEIGHT));
    }

    return softKeyboardHeight;
  }

  // 用户的软键盘是否显示
  public boolean isSoftKeyboardShown() {
    return getCurrentSoftInputHeight() != 0;
  }

  /**
   * 得到虚拟按键的高度
   *
   * @return 虚拟按键的高度
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private int getNavigationBarHeight() {

    WindowManager windowManager =
        (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
    // 获取可用的高度
    DisplayMetrics defaultDisplayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(defaultDisplayMetrics);
    int usableHeight = defaultDisplayMetrics.heightPixels;

    // 获取实际的高度
    DisplayMetrics realDisplayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getRealMetrics(realDisplayMetrics);
    int realHeight = realDisplayMetrics.heightPixels;

    return realHeight > usableHeight ? realHeight - usableHeight : 0;
  }

  /**
   * 得到当前软键盘的高度
   *
   * @return 软键盘的高度
   */
  public int getCurrentSoftInputHeight() {

    Rect rect = new Rect();

    mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

    int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();

    int softInputHeight = screenHeight - rect.bottom;

    // Android LOLLIPOP 以上的版本才有"虚拟按键"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      softInputHeight -= getNavigationBarHeight();
    }

    // excuse me?
    if (softInputHeight < 0) {
      Log.e(TAG, "excuse me，键盘高度小于0？");
    }

    return softInputHeight;
  }
}