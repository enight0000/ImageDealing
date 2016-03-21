package kexincom.cbpm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.polites.android.GestureImageView;

import java.text.SimpleDateFormat;

import kexincom.cbpm.entity.UploadTransport;


public class ResultActivity extends ActionBarActivity {

    private UploadTransport ut;
    private ImageButton back;
    private GestureImageView init,result;
    private TextView title,resultText,createTime,dealTime,funcName;
    public SimpleDateFormat time=new SimpleDateFormat("MM月dd日 HH:mm");
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
            .build();//构建完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_result);
        this.back = (ImageButton)this.findViewById(R.id.result_back);
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this,DealListActivity.class);
                startActivity(i);
                finish();
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
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

        this.init = (GestureImageView)findViewById(R.id.result_init);
        this.result = (GestureImageView)findViewById(R.id.result_resultImage);
        this.title = (TextView)findViewById(R.id.result_title);
        this.resultText = (TextView)findViewById(R.id.result_resultText);
        this.createTime = (TextView)findViewById(R.id.result_createTime);
        this.dealTime = (TextView)findViewById(R.id.result_dealTime);
        this.funcName = (TextView)findViewById(R.id.result_func);

        Intent intent=getIntent();
        if(intent != null){
            ut = (UploadTransport) intent.getSerializableExtra(UploadActivity.UPLOADTASK);
            title.setText(ut.getName());
            funcName.setText(ut.getName());
            funcName.setVisibility(TextView.VISIBLE);
            createTime.setText("创建时间："+time.format(ut.getTime()));
            createTime.setVisibility(TextView.VISIBLE);
            resultText.setText(ut.getResponse());
            resultText.setVisibility(TextView.VISIBLE);
            ImageLoader.getInstance().displayImage("file://" + ut.getUploadFile(), init,options);
        }

    }
/*
    @Override
    public void onBackPressed(){
        Intent i = new Intent(ResultActivity.this,DealListActivity.class);
        startActivity(i);
        finish();
    }
*/

}
