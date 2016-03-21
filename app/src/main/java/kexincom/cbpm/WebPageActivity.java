package kexincom.cbpm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Leon on 2015/7/31 0031.
 */
public class WebPageActivity extends ActionBarActivity {
    public static String URL = "url";
    private TextView textView;
    private ImageButton imageButton1;
    private WebView web;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_web_page);
        this.textView = (TextView) findViewById(R.id.webpage_title);
        web = (WebView)findViewById(R.id.webView);

        Intent intent=getIntent();
        if(intent != null){
            String url = intent.getStringExtra(URL);
            web.loadUrl(url);
            //可触摸放大缩小
            web.getSettings().setBuiltInZoomControls(true);
            //双击变大，再双击后变小
            web.getSettings().setUseWideViewPort(true);
            //适应全屏
            web.setInitialScale(39);
            web.getSettings().setBlockNetworkImage(false);
        }

        this.imageButton1 = (ImageButton) findViewById(R.id.webpage_back);
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
    }



}

