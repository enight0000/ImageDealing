package kexincom.cbpm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import kexincom.cbpm.camera.CameraActivity;
import kexincom.cbpm.entity.Image;
import kexincom.cbpm.entity.UploadTransport;
import kexincom.cbpm.fragment.ImageListFragment;


public class TaskReadPicActivity extends ActionBarActivity implements ImageListFragment.ImageListCallbacks{
    public String URL = "http://112.74.215.12:8000/disk/";//"http://image.baidu.com/pictureup/uploadshitu"
    public String KEY = "headImg";
    public String NAME = "应用：识图";
    public Map<String, String> PARAMS = new HashMap<String, String>();



    private static int REQUEST_CODE_CAPTURE_CAMEIA = 9;
    RelativeLayout relativeLayout = null;
    private FragmentManager fragmentManager;
    private ImageListFragment imageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_task_count_num);
        relativeLayout = (RelativeLayout) findViewById(R.id.Task_count_main);
        fragmentManager = getSupportFragmentManager();
        imageListFragment = ImageListFragment.newInstance(true);
        fragmentManager.beginTransaction()
                .replace(R.id.Task_count_main, imageListFragment)
                .commit();
        //
        PARAMS.put("title", "哇哈哈哈");
    }

    @Override
    public void onClickListener(int action, Image image) {
        switch (action) {
            case ImageListFragment.ImageListCallbacks.OPENALBUM:
                imageListFragment = ImageListFragment.newInstance(false);
                fragmentManager.beginTransaction()
                        .replace(R.id.Task_count_main, imageListFragment)
                        .commit();
                return;
            case ImageListFragment.ImageListCallbacks.OPENCAMERA:
                callCamera();
                return;
            case ImageListFragment.ImageListCallbacks.OPENBACK:
                this.finish();
                return;
            case ImageListFragment.ImageListCallbacks.SELECTOK:
                upload(image.getSrc());
                return;
            default:
                Toast.makeText(this, "wrong! " + action, Toast.LENGTH_SHORT);
        }
    }

    private void callCamera() {
        Intent i = new Intent(TaskReadPicActivity.this, CameraActivity.class);
        this.startActivityForResult(i, 1000);
    }

    private void upload(String path){
        UploadTransport ut = new UploadTransport(NAME,URL,KEY,path,PARAMS);
        Intent intent = new Intent();
        intent.setClass(this, UploadActivity.class);
        intent.putExtra(UploadActivity.UPLOADTASK, ut);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1001) {
            if (requestCode == 1000) {
                String dir = data.getStringExtra("result");
                if(dir!=null)
                    upload(dir);
                else{
                    Toast.makeText(this, "拍摄失败，请重新拍照！ ", Toast.LENGTH_SHORT);
                }
            }
        }
    }
}
