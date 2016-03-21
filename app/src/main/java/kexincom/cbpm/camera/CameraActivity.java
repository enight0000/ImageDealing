package kexincom.cbpm.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import kexincom.cbpm.R;
import kexincom.cbpm.annotation.NotProguard;

public class CameraActivity extends ActionBarActivity implements SurfaceHolder.Callback, PhotoCallBack.OnReturnCallback {
    private Camera camera;
    private boolean canCut = true;
    private DisplayMetrics metric;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageButton cutBtn;
    //进度条控件
    public View mProgressContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //隐藏进度条控件
        mProgressContainer = (View) findViewById(R.id.progressBar);
        //v.findViewById(R.id.camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView); // Camera interface to instantiate components
        surfaceHolder = surfaceView.getHolder(); // Camera interface to instantiate components
        surfaceHolder.addCallback(this); // Add a callback for the SurfaceHolder
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cutBtn = (ImageButton) findViewById(R.id.cutBtn);
        cutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canCut) {
                    takePic();
                }
            }
        });
    }

    private void takePic() {
        Lg.d("CameraFragment takePic start camera=" + camera);
        try {
            camera.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                    AudioManager meng = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);
                    if (volume != 0)
                    {
                        MediaPlayer shootMP = MediaPlayer.create(getApplicationContext(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
                        if(shootMP!=null) {
                            shootMP.start();
                        }
                    }
                    mProgressContainer.setVisibility(View.VISIBLE);
                }
            }, null, new PhotoCallBack(this));
        }catch(RuntimeException e){
            Lg.e(e.getMessage());
        }
        Lg.d("CameraFragment takePic down");

    }

    @Override
    public void onFinish(String filePath){
        mProgressContainer.setVisibility(View.INVISIBLE);
        Intent intent = new Intent();
        intent.putExtra("result", filePath);
        setResult(1001, intent);
        finish();
    }

    @NotProguard
    public void surfaceCreated(SurfaceHolder holder) {
        Lg.d("CameraFragment surfaceCallback====");
        try {
            camera = Camera.open(); // Turn on the camera
            camera.setPreviewDisplay(holder); // Set Preview
        }catch (NullPointerException e){
            error(e);
            surfaceDestroyed(holder);
        }catch (IOException e) {
            error(e);
            surfaceDestroyed(holder);
        }catch (Exception e){
            error(e);
            surfaceDestroyed(holder);
        }
    }

    @NotProguard
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Lg.d("CameraFragment ====surfaceChanged");
        if(camera==null){
            surfaceDestroyed(holder);
            return;
        }
        Camera.Parameters parameters = camera.getParameters(); // Camera parameters to obtain
        try {
            parameters.setPictureFormat(PixelFormat.JPEG);// Setting Picture Format
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//自动连续对焦
            parameters.setJpegQuality(90);
            //parameters.setRotation(90);
            Camera.Size[] size = getFitSize(parameters.getSupportedPreviewSizes(), parameters.getSupportedPictureSizes(), metric.widthPixels, metric.heightPixels);
            if (size != null) {
                parameters.setPreviewSize(size[0].width, size[0].height);
                parameters.setPictureSize(size[1].width, size[1].height);
                Lg.d("find suitable preview size w=" + size[0].width + " h=" + size[0].height + " r=" + (float) size[0].width / (float) size[0].height);
                Lg.d("find suitable picture size w=" + size[1].width + " h=" + size[1].height + " r=" + (float) size[1].width / (float) size[1].height);
            }

            //CameraFragment.camera.setDisplayOrientation(0);
            camera.setDisplayOrientation(90);

            camera.setParameters(parameters); // Setting camera parameters
        }catch (RuntimeException e){
            error(e);
            return;
        }
        camera.startPreview(); // Start Preview
        camera.cancelAutoFocus();// 使用连续的自动对焦

    }

    @NotProguard
    public void surfaceDestroyed(SurfaceHolder holder) {
        Lg.d("CameraFragment ====surfaceDestroyed");
        if(holder!=null)
            holder.removeCallback(this);
        if(camera==null)
            return;
        camera.setPreviewCallback(null);
        camera.stopPreview();// stop preview
        camera.lock();
        camera.release(); // Release camera resources
        camera = null;
    }

    private void error(Exception e){
        Lg.e(e.getMessage());
        camera.release();// release camera
        camera = null;
    }

    private Camera.Size[] getFitSize(List<Camera.Size> previewSizes, List<Camera.Size> pictureSizes, int screenW, int screenH) {
        Lg.d("CameraFragment getSupportedPictureSizes length=" + pictureSizes.size() + " getSupportedPreviewSizes length=" + previewSizes.size());
        Camera.Size[] size = new Camera.Size[2];//0 previewSizes 1 pictureSizes
        float ratio = (float) screenH / (float) screenW;
        Lg.d("screen ratio " + ratio);
        if (previewSizes.size() > 0 && pictureSizes.size() > 0) {
            CameraSizeComparator sizeComparator = new CameraSizeComparator();
            Collections.sort(previewSizes, sizeComparator);
            Collections.sort(pictureSizes, sizeComparator);
            Iterator<Camera.Size> itv = previewSizes.iterator();
            while (itv.hasNext()) {
                size[0] = itv.next();
                float f = (float) size[0].width / (float) size[0].height;
                if (size[0].height<=screenH && Math.abs(f - ratio) < 0.15) {
                    Lg.d("one suitable preview size w=" + size[0].width + " h=" + size[0].height + " r=" + (float) size[0].width / (float) size[0].height);
                    size[1] = getFitPictureSize(pictureSizes, f, 1280);
                    Lg.d("has suitable? picture size ? " + size[1]);
                    if (size[1] != null) {
                        //Lg.d( "suitable picture size w=" + size[1].width + " h=" + size[1].height + " r=" + (float) size[1].width / (float) size[1].height);
                        return size;
                    }
                }
            }
        }
        return null;
    }

    private Camera.Size getFitPictureSize(List<Camera.Size> pictureSizes, float ratio, int minWidth) {
        Camera.Size size = null;
        Iterator<Camera.Size> itp = pictureSizes.iterator();
        while (itp.hasNext()) {
            size = itp.next();
            float r = (float) size.width / (float) size.height;
            Lg.d("suitable picture size w=" + size.width + " h=" + size.height + " r=" + r);
            if (size.width >= minWidth && (Math.abs(r - ratio) < 0.001f)) {
                return size;
            }
        }
        return null;
    }



    class CameraSizeComparator implements Comparator<Camera.Size> {
        //按降序排列
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // TODO Auto-generated method stub
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return -1;
            } else {
                return 1;
            }
        }

    }


}
