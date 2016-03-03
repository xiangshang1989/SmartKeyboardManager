package geeklub.org.messageboarddemo.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by HelloVass on 16/3/3.
 */
public class ScreenUtil {
  private static final String TAG = ScreenUtil.class.getSimpleName();

  private ScreenUtil() {

  }

  public static int getScreenWidth(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    return displayMetrics.widthPixels;
  }

  public static int getScreenHeight(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    return displayMetrics.heightPixels;
  }
}
