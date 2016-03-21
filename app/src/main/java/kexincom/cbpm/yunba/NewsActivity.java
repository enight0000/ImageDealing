package kexincom.cbpm.yunba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import io.yunba.android.manager.YunBaManager;
import kexincom.cbpm.MainActivity;
import kexincom.cbpm.R;

public class NewsActivity extends ActionBarActivity {

    private ImageButton back,setting;
    private TextView title,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        this.back = (ImageButton)this.findViewById(R.id.new_back);
        this.setting = (ImageButton)this.findViewById(R.id.new_setting);
        this.title = (TextView)findViewById(R.id.new_title);
        this.msg = (TextView)findViewById(R.id.new_msg);

        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, MainActivity.class);
                startActivity(intent);
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
        Intent intent=getIntent();
        if(intent != null){
            title.setText(intent.getStringExtra(YunBaManager.MQTT_TOPIC));
            msg.setText(intent.getStringExtra(YunBaManager.MQTT_MSG));
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(NewsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
