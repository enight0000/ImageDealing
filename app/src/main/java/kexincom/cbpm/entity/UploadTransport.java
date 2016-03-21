package kexincom.cbpm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by Leon on 2015/8/3 0003.
 */
public class UploadTransport implements Serializable {
    public final static int WAIT_TO_UPLOAD = 0;
    public final static int IS_UPLOADING = 1;
    public final static int UPLOAD_DOWN = 2;
    public final static int WAIT_TO_DOWN = 3;
    public final static int IS_DOWNING = 4;
    public final static int FINISH = 5;
    public final static int PAUSE = 8;
    public final static int UPLOAD_FAIL = 9;
    public final static int DOWN_FAIL = 10;

    protected int id;
    protected long time;
    protected String name;
    protected String url;
    protected String uploadFileKey;
    protected String uploadFile;
    protected Map<String,String> uploadValues;
    protected String response;
    protected int flag = 0;
    protected int percent = 0;

    public UploadTransport(){
        time = new Date().getTime();
    }

    public UploadTransport(String name,String url,String uploadFileKey,String uploadFilePath){
        time = new Date().getTime();
        this.name = name;
        this.url = url;
        this.uploadFileKey = uploadFileKey;
        this.uploadFile = uploadFilePath;
    }

    public UploadTransport(String name,String url,String uploadFileKey,String uploadFilePath,Map uploadValues){
        time = new Date().getTime();
        this.name = name;
        this.url = url;
        this.uploadFileKey = uploadFileKey;
        this.uploadFile = uploadFilePath;
        this.uploadValues = uploadValues;
    }

    public String getUploadFileKey() {
        return uploadFileKey;
    }

    public void setUploadFileKey(String uploadFileKey) {
        this.uploadFileKey = uploadFileKey;
    }

    public String getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getUploadValues() {
        return uploadValues;
    }

    public void setUploadValues(Map<String, String> uploadValues) {
        this.uploadValues = uploadValues;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
