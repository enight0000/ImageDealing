package kexincom.cbpm.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import kexincom.cbpm.R;
import kexincom.cbpm.entity.Function;

/**
 * Created by Leon on 2015/8/4 0004.
 */
public class TaskListAdapter extends BaseAdapter {
    private Context context;
    private List<Function> list;
    public TaskListAdapter(Context context){
        this.context = context;
        list = new ArrayList<Function>();
        Function t1 = new Function(Function.COUNTMONEY,"数数","自动识别钱的张数","assets://task1.jpg");
        list.add(t1);
        Function t2 = new Function(Function.IDENTYWORDS,"识字","自动识别文字符号","assets://task2.jpg");
        list.add(t2);
        Function t3 = new Function(Function.IDENTYIMAGE,"识图","自动识别图像意义","assets://task3.jpg");
        list.add(t3);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Function getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view =  LayoutInflater.from(context).inflate(
                    R.layout.layout_tasklist, null);
        } else {
            view =  LayoutInflater.from(context).inflate(
                    R.layout.layout_tasklist, null);
        }
        TextView textView1 = (TextView) view.findViewById(R.id.TaskList_itext);
        textView1.setText(getItem(position).getName());
        TextView textView2 = (TextView) view.findViewById(R.id.TaskList_idesc);
        textView2.setText(getItem(position).getDescription());

        ImageView imageView = (ImageView)view.findViewById(R.id.TaskList_image);
        ImageLoader.getInstance().displayImage(getItem(position).getPath(), imageView, options);
        return view;
    }

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
            .build();//构建完成

}
