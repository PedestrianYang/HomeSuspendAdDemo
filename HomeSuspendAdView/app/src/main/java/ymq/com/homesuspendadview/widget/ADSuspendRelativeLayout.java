package ymq.com.homesuspendadview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by iyunshu on 2019/11/19.
 */

public class ADSuspendRelativeLayout extends RelativeLayout {

    public int lastYa;

    private ADFloatViewTouchLinstener mlinstener;

    public ADSuspendRelativeLayout(Context context) {
        super(context);
    }

    public ADSuspendRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setADFloatViewTouchLinstener(ADFloatViewTouchLinstener linstener){
        this.mlinstener = linstener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastYa = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastYa = (int) ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                float dy = ev.getRawY() - lastYa;

                //每次滑动重置手指开始位置
                lastYa = (int) ev.getRawY();
                if (mlinstener != null){
                    mlinstener.moveView(0, dy);
                }


                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public interface ADFloatViewTouchLinstener{
        void moveView(float offsetX, float offsetY);
    }
}
