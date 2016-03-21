package kexincom.cbpm;

import java.util.List;

import kexincom.cbpm.entity.UploadTransport;

/**
 * Created by Leon on 2015/8/10 0010.
 */
public interface OnProgressListener {
    void onProgress(List<UploadTransport> list,int flag);
}