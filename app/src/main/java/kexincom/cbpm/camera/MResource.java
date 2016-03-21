package kexincom.cbpm.camera;

/**
 * 作者：Leon Xie
 * 时间： 2015/11/5 0005
 * 邮箱：xiezhixuan@cbpm-kexin.com
 */

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * 根据资源的名字获取其ID值
 * @author mining
 *
 */

public class MResource {
    public static int getIdByName(Context context, String className, String name) {
        String packageName = context.getPackageName();
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            CrashReport.postCatchedException(new Throwable("MresourceError",e));
        } catch (IllegalArgumentException e) {
            CrashReport.postCatchedException(new Throwable("MresourceError", e));
        } catch (SecurityException e) {
            CrashReport.postCatchedException(new Throwable("MresourceError", e));
        } catch (IllegalAccessException e) {
            CrashReport.postCatchedException(new Throwable("MresourceError", e));
        } catch (NoSuchFieldException e) {
            CrashReport.postCatchedException(new Throwable("MresourceError", e));
        }
        Lg.d("MResource "+name+"  :"+id);
        return id;
    }
}
