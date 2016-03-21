package kexincom.cbpm;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.io.File;

import io.yunba.android.manager.YunBaManager;

/**
 * Created by Leon on 2015/8/5 0005.
 */
public class CBPMApplication extends Application {

    private String DIR = Environment.getExternalStorageDirectory().toString() + File.separator + "CBPM";
    private String TAG = "CBPM";
    public DbUtils db;
    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
        sql();
        initImageLoader();
        yunba();
    }

    public String getCBPMPath() {
        String sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = DIR;//获取跟目录
        }
        return sdDir;
    }

    public boolean initLog() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        BuglyLog.setCache(6 * 1024);
        strategy.setAppChannel("Version1.2");
        strategy.setDeviceID(android.os.Build.MODEL);
        CrashReport.initCrashReport(this, "900008765", false, strategy);
        return true;
    }

    public boolean sql(){
        //Xutils
        db = DbUtils.create(this.getBaseContext(), "CBPM.db");
        return true;
    }

    public boolean initImageLoader(){
        File cacheFile = StorageUtils.getOwnCacheDirectory(this, DIR + File.separator + "Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(cacheFile)) // default
                .diskCacheFileCount(100)
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
        return true;
    }

    public boolean yunba(){
        //YunBa
        YunBaManager.start(getApplicationContext());
        YunBaManager.subscribe(getApplicationContext(), new String[]{"news"}, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken arg0) {
                Log.d(TAG, "Subscribe topic succeed");
            }

            @Override
            public void onFailure(IMqttToken arg0, Throwable arg1) {
                Log.d(TAG, "Subscribe topic failed");
            }
        });
        String alias =  (android.os.Build.MODEL+((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId()).replace(" ","_");
        YunBaManager.setAlias(getApplicationContext(),alias ,null);
        return true;
    }
}
