package kexincom.cbpm.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import kexincom.cbpm.R;
import kexincom.cbpm.entity.Album;
import kexincom.cbpm.entity.Image;


/**
 * Created by Leon on 2015/8/5 0005.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private Album album;

    public ImageAdapter(Context context, Album album) {
        this.context = context;
        this.album = album;

    }

    @Override
    public int getCount() {
        return album.getList().size();
    }

    @Override
    public Image getItem(int position) {
        return album.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.layout_image, null);
        } else {
            view = (View) convertView;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.layout_image);
        imageView.setMaxHeight(180);
        imageView.setPadding(1, 1, 1, 1);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageLoader.getInstance().displayImage("file://"+getItem(position).getSrc(), imageView, options);

        return view;
    }


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
            .build();//构建完成
}
