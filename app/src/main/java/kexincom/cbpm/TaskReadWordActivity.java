package kexincom.cbpm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import kexincom.cbpm.entity.Image;
import kexincom.cbpm.entity.UploadTransport;
import kexincom.cbpm.fragment.ImageListFragment;


public class TaskReadWordActivity extends ActionBarActivity implements ImageListFragment.ImageListCallbacks{
    public String URL = "http://112.74.215.12:8000/disk/";//"http://image.baidu.com/pictureup/uploadshitu"
    public String KEY = "headImg";
    public String NAME = "应用：识字";
    public Map<String, String> PARAMS = new HashMap<String, String>();

    private static int REQUEST_CODE_CAPTURE_CAMEIA = 9;
    RelativeLayout relativeLayout = null;
    private FragmentManager fragmentManager;
    private ImageListFragment imageListFragment;
    private static String cameraImagePath;

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
        String dir = ((CBPMApplication) getApplication()).getCBPMPath();
        if (dir!=null) {
            String out_file_path = dir + File.separator + "Camera";
            File dirFile = new File(out_file_path);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            cameraImagePath = out_file_path + File.separator + System.currentTimeMillis() + ".jpg";
            Uri u = Uri.fromFile(new File(cameraImagePath));
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            i.putExtra(MediaStore.EXTRA_OUTPUT, u);
            this.startActivityForResult(i, REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                upload(this.cameraImagePath);
            }
        }
    }
}
