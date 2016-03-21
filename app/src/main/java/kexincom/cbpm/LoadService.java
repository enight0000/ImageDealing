package kexincom.cbpm;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kexincom.cbpm.entity.UploadTransport;

public class LoadService extends Service {
    private DbUtils db;
    private HttpUtils http;
    private List<UploadTransport> list;
    private boolean waiting = true;
    private UploadTransport currentTask;
    public OnProgressListener mListener;

    public LoadService() {
        http = new HttpUtils();
        http.configResponseTextCharset("UTF-8");

    }

    @Override
    public void onCreate() {
        Log.e("Test", " Service onCreate() ");
        super.onCreate();
        try {
            db = ((CBPMApplication) getApplication()).db;
            list = db.findAll(Selector.from(UploadTransport.class).where("flag", "between", new int[]{UploadTransport.WAIT_TO_UPLOAD, UploadTransport.IS_UPLOADING}));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list == null) {
            list = new ArrayList<>();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Test", "Service onStartCommand() ");
        if (waiting && list.size() > 0) {
            Log.e("Test", "wait="+waiting+"\tlist.size="+list.size());
            start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Test", " onBind() ");
        return new MsgBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("Test", " onUnBind() ");
        return true;
    }

    public class MsgBinder extends Binder {
        public LoadService getService(OnProgressListener listener){
            LoadService.this.mListener = listener;
            return LoadService.this;
        }
        public void unBind(){
            LoadService.this.mListener = null;
        }
    }

    @Override
    public void onDestroy() {
        Log.e("Test", "Service onDestroy() ");
        try {
            if (list.size() > 0) {
                for (UploadTransport t : list) {
                    t.setFlag(UploadTransport.WAIT_TO_UPLOAD);
                    db.update(t);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }finally{
            list = null;
        }
    }

    public UploadTransport addTpList(UploadTransport ut){
        try {
            ut.setFlag(UploadTransport.WAIT_TO_UPLOAD);
            db.saveOrUpdate(ut);
            ut = db.findFirst(Selector.from(UploadTransport.class).where("time","=",""+ut.getTime()));
        } catch (DbException e) {
            e.printStackTrace();
        }
        list.add(ut);
        start();
        return ut;
    }

    private void start() {
        if (!waiting || list.size() < 1) {
            return;
        }
        waiting = false;
        currentTask = list.get(0);
        RequestParams params = new RequestParams();
        params.addBodyParameter("title", "mobile phone");
        params.addBodyParameter(currentTask.getUploadFileKey(), new File(currentTask.getUploadFile()));

        http.send(HttpRequest.HttpMethod.POST,
                currentTask.getUrl(),
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        Log.e("Test", "current:" + current + "/" + total);
                        if (total > 0) {
                            int percent = (int)(current*100/total);
                            currentTask.setPercent(percent);
                        }
                        if(mListener!=null){
                            mListener.onProgress(list,UploadTransport.IS_UPLOADING);
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("Test", "Service:" + responseInfo.statusCode + " " + responseInfo.result);
                        //responseInfo.result
                        currentTask.setFlag(UploadTransport.FINISH);
                        currentTask.setResponse(responseInfo.result);
                        currentTask.setPercent(100);
                        try {
                            ((CBPMApplication) getApplication()).db.update(currentTask);
                            Log.e("Test", "SQL execute ok!");
                        } catch (DbException e) {
                            Log.e("Test", "SQL error:" + e.getMessage());
                            e.printStackTrace();
                        }
                        list.remove(currentTask);
                        waiting = true;
                        if(mListener!=null){
                            mListener.onProgress(list,UploadTransport.UPLOAD_DOWN);
                        }
                        start();
                    }

                    @Override
                    public void onStart() {
                        currentTask.setFlag(UploadTransport.IS_UPLOADING);
                        if(mListener!=null){
                            mListener.onProgress(list,UploadTransport.IS_UPLOADING);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.e("Test", "Service Fail:" + error + " \n" + msg);
                        currentTask.setFlag(UploadTransport.UPLOAD_FAIL);
                        currentTask.setPercent(0);
                        currentTask.setResponse(msg);
                        try {
                            ((CBPMApplication) getApplication()).db.update(currentTask);
                            Log.e("Test", "SQL execute ok!");
                        } catch (DbException e) {
                            Log.e("Test", "SQL error:" + e.getMessage());
                            e.printStackTrace();
                        }
                        list.remove(currentTask);
                        waiting = true;
                        if(mListener!=null){
                            mListener.onProgress(list,UploadTransport.UPLOAD_FAIL);
                        }
                        start();
                    }
                });
    }


}
