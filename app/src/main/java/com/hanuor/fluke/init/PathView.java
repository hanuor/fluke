package com.hanuor.fluke.init;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Shantanu Johri on 20-05-2016.
 */
public class PathView extends View {

    Path path1;
    Path path2;
    Paint paint;
    float length;
    float length2;

    public PathView(Context context)
    {
        super(context);
    }

    public PathView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void init()
    {
        paint = new Paint();
        paint.setColor(Color.parseColor("#66ffffff"));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        path1 = new Path();
        path2 = new Path();
        path2.moveTo(0,0);
        path2.lineTo(700, 700);

        path1.moveTo(0, 100);
        path1.lineTo(350, 350);
       /* path.lineTo(200, 500);
        path.lineTo(290, 300);
        path.lineTo(350, 300);
*/
        // Measure the path
        PathMeasure measure = new PathMeasure(path1, false);
        PathMeasure measure2 = new PathMeasure(path2, false);

        length = measure.getLength();

        length2 = measure2.getLength();
      //  float[] intervals = new float[]{length, length};

        ObjectAnimator animat = ObjectAnimator.ofFloat(PathView.this, "phase", 1.0f, 0.0f);
        animat.setDuration(1800);
        animat.start();

        ObjectAnimator animator = ObjectAnimator.ofFloat(PathView.this, "phase", 1.0f, 0.0f);
        animator.setDuration(3000);
        animator.start();
    }

    //is called by animtor object
    public void setPhase(float phase)
    {
        Log.d("pathview","setPhase called with:" + String.valueOf(phase));
        paint.setPathEffect(createPathEffect(length, phase, 0.0f));
        invalidate();//will calll onDraw
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset)
    {
        return new DashPathEffect(new float[] { pathLength, pathLength },
                Math.max(phase * pathLength, offset));
    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        c.drawPath(path1, paint);
        c.drawPath(path2,paint);
    }
}
