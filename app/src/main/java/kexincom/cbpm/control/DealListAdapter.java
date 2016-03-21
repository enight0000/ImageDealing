package kexincom.cbpm.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kexincom.cbpm.R;
import kexincom.cbpm.entity.UploadTransport;

/**
 * Created by Leon on 2015/8/4 0004.
 */
public class DealListAdapter extends BaseAdapter {
    private Context context;
    private List<UploadTransport> list;
    private List<UploadTransport> select;
    private boolean selectMode = false;
    public SimpleDateFormat time=new SimpleDateFormat("MM月dd日\nHH时mm分");
    public DealListAdapter(Context context,List<UploadTransport> list){
        this.context = context;
        if(list!=null){
            this.list = list;
        }else{
            this.list = new ArrayList<>();
        }
        select = new ArrayList<UploadTransport>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public UploadTransport getItem(int position) {
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
                    R.layout.layout_deallist2, null);
        }
        TextView textView1 = (TextView) view.findViewById(R.id.deal_textView1);
        textView1.setText(getItem(position).getName());
        TextView textView2 = (TextView) view.findViewById(R.id.deal_textView2);
        textView2.setText(time.format(new Date(getItem(position).getTime())));
        TextView textView3 = (TextView) view.findViewById(R.id.deal_textView3);
        switch (getItem(position).getFlag()){
            case UploadTransport.WAIT_TO_UPLOAD:
                textView3.setText("等待中");
                textView3.setTextColor(Color.GRAY);
                break;
            case UploadTransport.IS_UPLOADING:
                textView3.setText("上传中");
                textView3.setTextColor(Color.CYAN);
                break;
            case UploadTransport.UPLOAD_DOWN:
                textView3.setText("上传完毕");
                textView3.setTextColor(Color.GREEN);
                break;
            case UploadTransport.WAIT_TO_DOWN:
                textView3.setText("等待中");
                textView3.setTextColor(Color.GRAY);
                break;
            case UploadTransport.IS_DOWNING:
                textView3.setText("下载中");
                textView3.setTextColor(Color.GREEN);
                break;
            case UploadTransport.FINISH:
                textView3.setText("执行完毕");
                textView3.setTextColor(Color.GREEN);
                break;
            case UploadTransport.UPLOAD_FAIL:
                textView3.setText("上传失败");
                textView3.setTextColor(Color.RED);
                break;
            case UploadTransport.DOWN_FAIL:
                textView3.setText("下载失败");
                textView3.setTextColor(Color.RED);
                break;
        }

        //TextView textView4 = (TextView) view.findViewById(R.id.deal_textView4);
        //textView4.setText(getItem(position).getResponse());

        //RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.deallist_layout);
        //idp = (idp+ (new Random().nextInt(bg.length-2)+1))%bg.length;
        //srl.setBackgroundResource(bg[idp]);
        ImageView imageView = (ImageView)view.findViewById(R.id.deal_image);
        //imageView.setScaleType(ImageView.ScaleType.MATRIX);
        ImageLoader.getInstance().displayImage("file://" + getItem(position).getUploadFile(), imageView, options);
        return view;
    }

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
            .displayer(new RoundedBitmapDisplayer(10))
            .build();//构建完成
}