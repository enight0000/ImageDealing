package kexincom.cbpm.camera;

import com.tencent.bugly.crashreport.BuglyLog;

/**
 * 作者：Leon Xie
 * 时间： 2015/11/2 0002
 * 邮箱：xiezhixuan@cbpm-kexin.com
 */
public class Lg {

    private static final String TAG = "CBPM";

    public static void e(String info) {
        //if (CODE.MODE > 0)
            BuglyLog.e(TAG, info);
       // else
          //  Log.e(TAG, info);
    }

    public static void d(String info) {
      //  if (CODE.MODE > 0)
            BuglyLog.d(TAG, info);
       // else
         //  Log.d(TAG, info);
    }

    public static void v(String info) {
      //  if (CODE.MODE > 0)
            BuglyLog.v(TAG, info);
       // else
          //  Log.v(TAG, info);
    }

}
