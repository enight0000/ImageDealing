package kexincom.cbpm.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.File;

import kexincom.cbpm.CBPMApplication;
import kexincom.cbpm.R;
import kexincom.cbpm.control.ImageAdapter;
import kexincom.cbpm.entity.Album;
import kexincom.cbpm.entity.Image;

/**
 * Created by Leon on 2015/7/31 0031.
 */
public class ImageListFragment extends Fragment{
    public static String ALBUMPATH = "AlbumPath";
    public boolean local = true;
    private ImageAdapter imageAdapter;
    private GridView gridView;
    private TextView textView;
    private ImageButton imageButton1,imageButton2,imageButton3;

    public static ImageListFragment newInstance(boolean local) {
        ImageListFragment fragment = new ImageListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ALBUMPATH, local);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            local = getArguments().getBoolean(ALBUMPATH,true);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_imagelist, container, false);
        //String path = savedInstanceState.getString(ALBUMPATH);

        Log.e("Test", "album url:" + MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

        this.gridView = (GridView)rootView.findViewById(R.id.fragement_imagelist_gridview);
        this.textView = (TextView)rootView.findViewById(R.id.fragment_imageconfirm_textView);

        Album album = new Album();

        if(local) {
            getTabloidImageList(album);

        }else{
            getTabloidImageList1(album);

        }

        imageAdapter = new ImageAdapter(getActivity(),album);
        this.gridView.setAdapter(imageAdapter);
        this.textView.setText(album.getName());
        this.gridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Image image = imageAdapter.getItem(position);
                if(image!=null){
                    mCallbacks.onClickListener(ImageListCallbacks.SELECTOK, image);
                }
            }
        });

        this.imageButton1 = (ImageButton)rootView.findViewById(R.id.fragement_imagelist_btn1);
        this.imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onClickListener(ImageListCallbacks.OPENBACK, null);
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
        this.imageButton2 = (ImageButton)rootView.findViewById(R.id.fragement_imagelist_btn2);
        this.imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onClickListener(ImageListCallbacks.OPENCAMERA, null);
            }
        });
        this.imageButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.camera_on);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.camera);
                }
                return false;
            }
        });
        this.imageButton3 = (ImageButton)rootView.findViewById(R.id.fragement_imagelist_btn3);
        this.imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onClickListener(ImageListCallbacks.OPENALBUM, null);
                imageButton3.setEnabled(false);
            }
        });
        this.imageButton3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.album_on);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.album);
                }
                return false;
            }
        });

        return rootView;
    }



    public void getTabloidImageList(Album album) {
        album.setName("CBPM");
        // 获取ContentResolver对象
        ContentResolver resolver = this.getActivity().getContentResolver();
        // 获得外部存储卡上的图片缩略图
        String selection = MediaStore.Images.Media.DATA + " like ?";
        String path=((CBPMApplication)getActivity().getApplication()).getCBPMPath()+ File.separator + "Camera";
        //定义selectionArgs：
        String[] selectionArgs = {"%"+path+"%"};
        Cursor c = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                //MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                null, selection,
                selectionArgs,
                MediaStore.Images.Media.DATE_TAKEN+" DESC");
        // 为了for循环性能优化，用一变量存储数据条数
        int totalCount = c.getCount();
        if(totalCount==0){
            getTabloidImageList1(album);
        }
        // 将Cursor移动到第一位
        //c.moveToFirst();
        c.moveToLast();
        // 将缩略图数据添加到ArrayList中
        for (int i = 0; i < totalCount; i++) {
            Image image = new Image();
            int indexOfId = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int indexOfSrc = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String src = c.getString(indexOfSrc);
            int id = c.getInt(indexOfId);

            image.setSrc(src);
            image.setId(id);
            album.getList().add(image);
            //c.moveToNext();
            c.moveToPrevious();
        }
        // 关闭游标
        c.close();
    }

    public void getTabloidImageList1(Album album) {
        album.setName("相册");
        // 获取ContentResolver对象
        ContentResolver resolver = this.getActivity().getContentResolver();
        // 获得外部存储卡上的图片缩略
        Cursor c = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                //MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                null, null,
                null, null);
        // 为了for循环性能优化，用一变量存储数据条数
        int totalCount = c.getCount();
        // 将Cursor移动到第一位
        //c.moveToFirst();
        c.moveToLast();
        // 将缩略图数据添加到ArrayList中
        for (int i = 0; i < totalCount; i++) {
            Image image = new Image();
            int indexOfId = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int indexOfSrc = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String src = c.getString(indexOfSrc);
            int id = c.getInt(indexOfId);

            image.setSrc(src);
            image.setId(id);
            album.getList().add(image);
            //c.moveToNext();
            c.moveToPrevious();
        }
        // 关闭游标
        c.close();
    }



    private ImageListCallbacks mCallbacks;

    public static interface ImageListCallbacks {
        int OPENALBUM = 1;
        int OPENCAMERA = 2;
        int OPENBACK = 3;
        int SELECTOK = 4;
        void onClickListener(int action,Image image);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (ImageListCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ImageListCallbacks.");
        }
    }
}
