package kexincom.cbpm.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.text.format.DateFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import kexincom.cbpm.CBPMApplication;

/**
 * 作者：Leon Xie
 * 时间： 2015.9
 * 邮箱：xiezhixuan@cbpm-kexin.com
 */

public class PhotoCallBack implements Camera.PictureCallback {
    private CameraActivity activity;
    private final int JPEGQuality = 90;
    public PhotoCallBack(CameraActivity activity){
        this.activity = activity;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        String path = ((CBPMApplication)activity.getApplication()).getCBPMPath()+File.separator+"Camera";
        String name = DateFormat.format("yyyyMMddhhmmss", new Date()).toString() + ".jpg";
        try {
            dealing(data, camera,path,name);
        } catch (OutOfMemoryError e) {
            activity.onFinish(null);
           // Lg.e("get memory:" + DealUtils.getmem_UNUSED(activity));
        }
        activity.onFinish(path+File.separator+name);
    }

    final float x = 25f/96f,y = 21f/128f,h = 50f/96f;
    private void dealing(byte[] data, Camera camera,String path,String name)throws OutOfMemoryError{
        Camera.Size size = camera.getParameters().getPictureSize();
        float scale = 1f;
        while(size.height/scale>=600){
            scale += 0.1;
        }
        Bitmap mBitmap = null;

        Lg.e("data length=" + data.length);
        Lg.e("scale= "+scale);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig =  Bitmap.Config.RGB_565;
        options.outWidth = (int)(size.width/scale);
        options.outHeight = (int)(size.height/scale);
        options.inPreferQualityOverSpeed = true;
        mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        saveToSDCard(mBitmap, path, "1-" + name);
        Matrix m = new Matrix();
        m.setRotate(90);
        mBitmap = Bitmap.createBitmap(mBitmap,
                (int)(size.width*x),
                (int)(size.height*y),
                (int)((size.width)*(0.5f-y)),
                (int)(size.height*h)
                ,m,true);
        saveToSDCard(mBitmap, path, name);
    }


    private boolean delete(String path) {
        if(path==null)
            return true;
        File picture = new File(path);
        if (picture.exists()) {
            return picture.delete();
        }
        return true;
    }

    private void saveToSDCard(byte[] data, String path, String name) {
        File picture = makeDir(path, name);
        try {
            FileOutputStream fos = new FileOutputStream(picture); // Get file output stream
            fos.write(data, 0, data.length);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Lg.d(e.getMessage());
        }
    }

    private void saveToSDCard(Bitmap bitmap, String path, String name) {
        File picture = makeDir(path, name);
        try {
            FileOutputStream fos = new FileOutputStream(picture); // Get file output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, JPEGQuality, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Lg.d(e.getMessage());
        }
    }

    private File makeDir(String path, String name) {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        return new File(path +File.separator+ name);
    }

    public interface OnReturnCallback {
        void onFinish(String filePath);
    }

}
