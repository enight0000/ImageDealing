package kexincom.cbpm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import kexincom.cbpm.MainActivity;
import kexincom.cbpm.R;
import kexincom.cbpm.WebPageActivity;

/**
 * Created by Leon on 2015/7/31 0031.
 */
public class RollImageFragment extends Fragment implements android.view.GestureDetector.OnGestureListener{

    private String imgs[] = new String[5];
    //private int[] imgs = {,R.drawable.title2, R.drawable.title3, R.drawable.title4};//, R.drawable.title3, R.drawable.title4, R.drawable.title5
    private GestureDetector gestureDetector = null;
    private ViewFlipper viewFlipper = null;
    private Activity mActivity = null;
    private MainActivity.MyOnTouchListener myOnTouchListener;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
            .build();//构建完成

    public static RollImageFragment newInstance(int sectionNumber) {
        RollImageFragment fragment = new RollImageFragment();
        return fragment;
    }

    public RollImageFragment() {
        imgs[0] = "assets://title1.jpg";
        imgs[1] = "assets://title2.jpg";
        imgs[2] = "assets://title3.jpg";
        imgs[3] = "assets://title4.jpg";
        imgs[4] = "assets://title5.jpg";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = this.getActivity();
        View rootView = inflater.inflate(R.layout.fragement_scoll_image, container, false);
        viewFlipper = (ViewFlipper) rootView.findViewById(R.id.roll_image_flipper);
        gestureDetector = new GestureDetector(this);    // 声明检测手势事件

        for (int i = 0; i < imgs.length; i++) {          // 添加图片源
            ImageView iv = new ImageView(this.mActivity);
            //iv.setImageResource(imgs[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            ImageLoader.getInstance().displayImage(imgs[i], iv, options);
            viewFlipper.addView(iv);
        }

        viewFlipper.setAutoStart(true);         // 设置自动播放功能（点击事件，前自动播放）
        viewFlipper.setFlipInterval(9000);      //自动播放时间
        if(viewFlipper.isAutoStart() && !viewFlipper.isFlipping()){
            viewFlipper.startFlipping();
        }

        myOnTouchListener = new MainActivity.MyOnTouchListener() {

            @Override
            public boolean onTouch(MotionEvent ev) {
                //viewFlipper.stopFlipping();
                boolean result = gestureDetector.onTouchEvent(ev);
                return result;
            }
        };
        ((MainActivity) getActivity())
                .registerMyOnTouchListener(myOnTouchListener);

        return rootView;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e2==null||e1==null){
            return true;
        }
        if (e2.getX() - e1.getX() > 120) {            // 从左向右滑动（左进右出）
            Animation rInAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_in);  // 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）
            Animation rOutAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            viewFlipper.setInAnimation(rInAnim);
            viewFlipper.setOutAnimation(rOutAnim);
            viewFlipper.showPrevious();
            return true;
        } else if (e2.getX() - e1.getX() < -120) {        // 从右向左滑动（右进左出）
            Animation lInAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_in);       // 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）
            Animation lOutAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_out);     // 向左滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            viewFlipper.setInAnimation(lInAnim);
            viewFlipper.setOutAnimation(lOutAnim);
            viewFlipper.showNext();
            return true;
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e){
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        int index = viewFlipper.getDisplayedChild();
        Intent i = new Intent(this.getActivity(), WebPageActivity.class);
        i.setAction("kexincom.cbpm.WebPageActivity");
        switch(index){
            case 0:
                break;
            case 1:
                i.putExtra(WebPageActivity.URL, "http://mp.weixin.qq.com/s?__biz=MzA5MDYwNzA2NQ==&mid=208639714&idx=1&sn=fff2f7b1c50af832fcca056488ce8a69&scene=18#wechat_redirect");
                startActivity(i);
                break;
            case 2:
                i.putExtra(WebPageActivity.URL, "http://m.cbpm.cn/home/cn/phone/index/");
                startActivity(i);
                break;
            case 3:
            case 4:
                break;

        }
        //Toast.makeText(this.getActivity(),"open "+index, Toast.LENGTH_SHORT).show();
        return false;
    }


}
