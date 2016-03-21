package kexincom.cbpm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.polites.android.GestureImageView;

import java.util.List;

import kexincom.cbpm.entity.UploadTransport;


public class UploadActivity extends ActionBarActivity implements OnProgressListener {
    public static String UPLOADTASK = "UploadTask";

    private GestureImageView view;
    private TextView textView;
    private ImageButton imageButton1,imageButton3;
    private Button button;
    private ProgressBar bar;
    private UploadTransport ut;
    private LoadService bindService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Test", " onCreate() ");
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_upload);
        Log.e("Test", " intent() ");
        Intent intent=getIntent();
        if(intent != null){
            ut = (UploadTransport) intent.getSerializableExtra(UPLOADTASK);
        }
        Log.e("Test", " view() ");
        this.view = (GestureImageView) findViewById(R.id.image);
        view.setMinScale(0.75f);
        view.setMaxScale(5.0f);
        Log.e("Test", " ImageLoader() ");
        ImageLoader.getInstance().displayImage("file://" + ut.getUploadFile(), view, options);
        this.textView = (TextView) findViewById(R.id.fragment_imageconfirm_textView);
        this.bar = (ProgressBar)findViewById(R.id.progressBar);
        this.textView.setText(ut.getName());
        //Bitmap bm = BitmapFactory.decodeFile(ut.getUploadFile());
        //this.imageView.setImageBitmap(bm);


        this.imageButton1 = (ImageButton) findViewById(R.id.fragement_imageconfirm_btn1);
        this.imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.imageButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.back_on);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.back);
                }
                return false;
            }
        });
        this.button = (Button) findViewById(R.id.fragement_imageconfirm_btn2);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ut = bindService.addTpList(ut);
                button.setVisibility(Button.INVISIBLE);
                bar.setMax(100);
                bar.setProgress(0);
                bar.setVisibility(ProgressBar.VISIBLE);
            }
        });

        this.imageButton3 = (ImageButton) findViewById(R.id.fragement_imageconfirm_btn3);
        this.imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this,DealListActivity.class);
                startActivity(intent);
            }
        });
        this.imageButton3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.upload_on);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.upload);
                }
                return false;
            }
        });
    }

    @Override
    public void onProgress(List<UploadTransport> list,int flag) {
        Log.e("Test","Upload notice flag:"+flag+"  percent:"+ut.getPercent());
        switch (ut.getFlag()) {
            case UploadTransport.WAIT_TO_UPLOAD:
            case UploadTransport.IS_UPLOADING:
                bar.setProgress(ut.getPercent());
                break;
            case UploadTransport.UPLOAD_DOWN:
            case UploadTransport.FINISH:
                bar.setVisibility(ProgressBar.INVISIBLE);
                button.setText("点击查看结果");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(UploadActivity.this,ResultActivity.class);
                        i.putExtra(UploadActivity.UPLOADTASK, ut);
                        startActivity(i);
                        finish();
                    }
                });
                button.setVisibility(Button.VISIBLE);
                break;
            case UploadTransport.UPLOAD_FAIL:
                Toast.makeText(UploadActivity.this,"上传失败！",Toast.LENGTH_SHORT);
                bar.setVisibility(ProgressBar.INVISIBLE);
                button.setVisibility(Button.VISIBLE);
        }
    }



    @Override
    protected void onResume(){
        Log.e("Test", "Activity Bind Servce");
        Intent intent = new Intent();
        intent.setAction("kexincom.cbpm.LoadService");
        intent.setPackage(getPackageName());//这里你需要设置你应用的包名
        Log.e("Test", "falg:"+bindService(intent, conn, Context.BIND_AUTO_CREATE));
        startService(intent);
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        unbindService(conn);
        ImageLoader.getInstance().cancelDisplayTask(this.view);
        //ImageLoader.getInstance().clearMemoryCache();
        super.onDestroy();
    }

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
            .build();//构建完成
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("Test","onServiceDisconnected");
            Intent intent = new Intent(UploadActivity.this,DealListActivity.class);
            startActivity(intent);
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Test", " onServiceConnected() ");
            //返回一个MsgService对象
            bindService = ((LoadService.MsgBinder)service).getService(UploadActivity.this);
        }
    };
}
