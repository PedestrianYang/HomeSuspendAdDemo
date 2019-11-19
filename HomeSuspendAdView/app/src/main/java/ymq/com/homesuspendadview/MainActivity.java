package ymq.com.homesuspendadview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ymq.com.homesuspendadview.widget.ADSuspendRelativeLayout;

public class MainActivity extends AppCompatActivity {

    private enum AdFloatViewState{
        HIDE,
        SHOW,
        DISMISS
    }

    private ListView listView;


    private ADSuspendRelativeLayout suspendAdView;
    private ImageView suspendAdImg;
    private ImageView suspendAdmgClose;
    private AdFloatViewState floatAdViewState = AdFloatViewState.SHOW;
    private AnimationHandler animationHandler;
    private AnimatorSet adSuspendHideAnimSet;
    private AnimatorSet adSuspendShowAnimSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getView();
    }

    private void getView() {
        listView = findViewById(R.id.list_view);
        final ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 100; i ++){
            datas.add("数据" + String.valueOf(i));
        }
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public Object getItem(int position) {
                return datas.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                String data = datas.get(position);
                TextView textView = new TextView(MainActivity.this);
                textView.setTextSize(20);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setText(data);

                return textView;
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (animationHandler == null){
                    animationHandler = new AnimationHandler();
                }
                switch (scrollState){
                    case SCROLL_STATE_IDLE:
                        if (!animationHandler.hasMessages(1) && floatAdViewState == AdFloatViewState.HIDE){
                            animationHandler.sendEmptyMessageDelayed(1, 1500);
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                    case SCROLL_STATE_FLING:
                        if (animationHandler.hasMessages(1)){
                            animationHandler.removeMessages(1);
                        }
                        if (!animationHandler.hasMessages(0) && floatAdViewState == AdFloatViewState.SHOW){

                            animationHandler.sendEmptyMessageDelayed(0, 1500);
                            floatAdViewState = AdFloatViewState.HIDE;
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        suspendAdView = findViewById(R.id.ad_view);
        suspendAdImg = findViewById(R.id.ad_img);
        suspendAdmgClose = findViewById(R.id.ad_img_close);
    }

    class AnimationHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int show = msg.what;
            startShowHideFloatADViewAnimation(show == 1);
        }
    }

    public void startShowHideFloatADViewAnimation(boolean shouldShow){

        float offsetWidth = (float) (suspendAdView.getMeasuredWidth() * 0.5);
        if (shouldShow){
            if (adSuspendShowAnimSet == null){
                adSuspendShowAnimSet = new AnimatorSet();
            }

            if (adSuspendHideAnimSet.isStarted()){
                adSuspendShowAnimSet.setStartDelay(510);
            }
            ObjectAnimator moveIn = ObjectAnimator.ofFloat(suspendAdView, "translationX", offsetWidth, 0f);
            ObjectAnimator rotate = ObjectAnimator.ofFloat(suspendAdView, "rotation", -45f, 0f);
            ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(suspendAdView, "alpha", 0.5f, 1f);
            ObjectAnimator closeBtnfadeInOut = ObjectAnimator.ofFloat(suspendAdmgClose, "alpha", 0f, 1f);
            adSuspendShowAnimSet.play(moveIn).with(fadeInOut).before(rotate).with(closeBtnfadeInOut);
            adSuspendShowAnimSet.setDuration(500);
            adSuspendShowAnimSet.start();
            floatAdViewState = AdFloatViewState.SHOW;
        }else {
            if (adSuspendHideAnimSet == null){
                adSuspendHideAnimSet = new AnimatorSet();
            }
            ObjectAnimator moveIn = ObjectAnimator.ofFloat(suspendAdView, "translationX", 0f, offsetWidth);
            ObjectAnimator rotate = ObjectAnimator.ofFloat(suspendAdView, "rotation", 0f, -45f);
            ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(suspendAdView, "alpha", 1f, 0.5f);
            ObjectAnimator closeBtnfadeInOut = ObjectAnimator.ofFloat(suspendAdmgClose, "alpha", 1f, 0f);
            adSuspendHideAnimSet.play(moveIn).with(fadeInOut).after(rotate).with(closeBtnfadeInOut);
            adSuspendHideAnimSet.setDuration(500);
            adSuspendHideAnimSet.start();
        }


    }
}
