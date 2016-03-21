package kexincom.cbpm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import kexincom.cbpm.control.DealListAdapter;
import kexincom.cbpm.entity.UploadTransport;


public class DealListActivity extends ActionBarActivity implements OnProgressListener {

    private ImageButton imageButton1 = null;
    private ImageButton imageButton2 = null;
    private GridView listView = null;
    private DealListAdapter adapter = null;
    private LoadService bindService;
    //private ModeCallback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_deal_list);
        imageButton1 = (ImageButton)findViewById(R.id.webpage_back);
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
        imageButton2 = (ImageButton)findViewById(R.id.DealList_btn2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });
        imageButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //v.setBackgroundResource(R.drawable.back_on);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //v.setBackgroundResource(R.drawable.back);
                }
                return false;
            }
        });
        listView = (GridView)findViewById(R.id.DealList_list);
        adapter = new DealListAdapter(this,getAll());
        listView.setAdapter(adapter);

        //mCallback = new ModeCallback();
        //listView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        //listView.setMultiChoiceModeListener(mCallback);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UploadTransport ut = adapter.getItem(position);
                Intent i = new Intent();
                switch (ut.getFlag()) {
                    case UploadTransport.DOWN_FAIL:
                    case UploadTransport.IS_UPLOADING:
                    case UploadTransport.IS_DOWNING:
                    case UploadTransport.UPLOAD_FAIL:
                    case UploadTransport.PAUSE:
                    case UploadTransport.WAIT_TO_DOWN:
                    case UploadTransport.WAIT_TO_UPLOAD:
                        i.putExtra(UploadActivity.UPLOADTASK, ut);
                        i.setClass(DealListActivity.this, UploadActivity.class);
                        break;
                    case UploadTransport.FINISH:
                    case UploadTransport.UPLOAD_DOWN:
                        i.putExtra(UploadActivity.UPLOADTASK, ut);
                        i.setClass(DealListActivity.this, ResultActivity.class);
                        break;
                }
                startActivity(i);
            }
        });
    }

    public List<UploadTransport> getAll(){
        List<UploadTransport> list = null;
        try {
            list = ((CBPMApplication) getApplication()).db.findAll(Selector.from(UploadTransport.class).orderBy("time", true));
            //list = ((CBPMApplication) getApplication()).db.findAll(UploadTransport.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        /*
        if(list!=null) {
            Log.e("Test", "select num=" + list.size());
            for (Iterator<UploadTransport> it = list.iterator(); it.hasNext(); ) {
                UploadTransport u = it.next();
                Log.e("DB", "getId:" + u.getId());
                Log.e("DB", "getName:" + u.getName());
                Log.e("DB", "getFlag:" + u.getFlag());
                Log.e("DB", "getUploadFile:" + u.getUploadFile());
                Log.e("DB", "getPercent:" + u.getPercent());
                Log.e("DB", "getResponse:" + u.getResponse());
                Log.e("DB", "getUploadFileKey:" + u.getUploadFileKey());
                Log.e("DB", "======================================");

            }
        }*/
        return list;
    }
    @Override
    public void onProgress(List<UploadTransport> list,int flag) {}

    @Override
    protected void onResume(){
        Log.e("Test", "Activity DealList Bind Servce");
        Intent intent = new Intent();
        intent.setAction("kexincom.cbpm.LoadService");
        intent.setPackage(getPackageName());//这里你需要设置你应用的包名
        Log.e("Test", "flag:"+bindService(intent, conn, Context.BIND_AUTO_CREATE));
        startService(intent);
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Test", " onServiceConnected() ");
            bindService = ((LoadService.MsgBinder)service).getService(DealListActivity.this);
        }
    };

/*
    private class ModeCallback implements ListView.MultiChoiceModeListener {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            Log.e("Test","onDestroyActionMode "+listView.getCheckedItemCount());

            listView.clearChoices();
        }
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            mode.invalidate();
            adapter.notifyDataSetChanged();
        }
    }
    */
}
