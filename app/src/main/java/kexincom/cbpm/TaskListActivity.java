package kexincom.cbpm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import kexincom.cbpm.control.TaskListAdapter;
import kexincom.cbpm.entity.Function;


public class TaskListActivity extends ActionBarActivity {

    private ImageButton imageButton1 = null;
    private ImageButton imageButton2 = null;
    private GridView listView = null;
    private TaskListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_tasklist);
        imageButton1 = (ImageButton)findViewById(R.id.TaskList_btn1);
        imageButton2 = (ImageButton)findViewById(R.id.TaskList_btn2);
        listView = (GridView)findViewById(R.id.TaskList_list);
        //list.add
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageButton1.setOnTouchListener(new View.OnTouchListener() {
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

        adapter = new TaskListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Function t = adapter.getItem(position);
                Intent i = new Intent();
                switch(t.getId()){
                    case Function.COUNTMONEY:
                        i.setClass(TaskListActivity.this, TaskCountNumActivity.class);
                        break;
                    case Function.IDENTYWORDS:
                        i.setClass(TaskListActivity.this, TaskReadWordActivity.class);
                        break;
                    case Function.IDENTYIMAGE:
                        i.setClass(TaskListActivity.this, TaskReadPicActivity.class);
                        break;
                }
                startActivity(i);
            }
        });

    }


}
