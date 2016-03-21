package kexincom.cbpm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import kexincom.cbpm.fragment.RollImageFragment;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends ActionBarActivity {
    public static String DIR = "CBPM";
    private FragmentManager fragmentManager;
    private MyOnTouchListener onTouchListeners =null;//动作监听器
    private RollImageFragment rf;
    private ImageButton imageButton1 = null;
    // private ImageButton imageButton2 = null;
    private ImageButton imageButton3 = null;
    private ImageButton setting = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        FrameLayout fl = (FrameLayout)findViewById(R.id.main_head);

        // imageButton2 = (ImageButton)findViewById(R.id.main_btn2);


        fragmentManager = getSupportFragmentManager();
        rf =  RollImageFragment.newInstance(1);
        fragmentManager.beginTransaction()
                .replace(R.id.main_scoreImage,rf)
                .commit();

        imageButton1 = (ImageButton)findViewById(R.id.main_btn1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TaskListActivity.class);
                startActivity(intent);
            }
        });
        imageButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.main_btn1_on);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.main_btn1);
                }
                return false;
            }
        });

        imageButton3 = (ImageButton)findViewById(R.id.main_btn3);
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DealListActivity.class);
                startActivity(intent);
            }
        });
        imageButton3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.main_btn3_on);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.main_btn3);
                }
                return false;
            }
        });

        setting = (ImageButton)findViewById(R.id.main_btns);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent();
                // intent.setClass(MainActivity.this, TaskListActivity.class);
                // startActivity(intent);
            }
        });
        setting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.setting_on);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.setting);
                }
                return false;
            }
        });





        Intent intent = new Intent();
        intent.setAction("kexincom.cbpm.LoadService");
        intent.setPackage(getPackageName());//这里你需要设置你应用的包名
        startService(intent);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(onTouchListeners!=null&&rf!=null){
            if(ev.getY()<=rf.getView().getHeight()&&ev.getY()>40) {
                return onTouchListeners.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);

    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        this.onTouchListeners = myOnTouchListener;
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        this.onTouchListeners = null;
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }



}
