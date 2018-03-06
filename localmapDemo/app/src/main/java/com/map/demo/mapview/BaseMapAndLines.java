package com.map.demo.mapview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import com.map.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 地图 地图
 * Created by Administrator on 2017/11/30.
 * @author Administrator
 */
public class BaseMapAndLines extends AppCompatImageView {

    // 线 坐标
    public ArrayList<MapPointWithTitleView> mapLineCoords;

    public List<List<MapPointWithTitleView>> allMapLines;

    private Context mContext;
    private MapView mapView;

    private Paint mPaint;
    private int lineColor = Color.BLUE;
    private float lineWidth = 1;
    private float pointRadius = 10;
    private float firstScale;
    private Path pathLine;
    private ValueAnimator animator;
    private float mAnimatorValue;
    private float[] pos;
    private float[] tan;
    private Paint paintText;
    private int goTime = 30;

    public BaseMapAndLines(Context context) {
        this(context,null);
        this.mContext = context;
    }

    public BaseMapAndLines(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        //路径粗细
        lineWidth = context.getResources().getDimension(R.dimen.x2);
        pointRadius = context.getResources().getDimension(R.dimen.x3 );
        init();
    }

    private void init() {
        mapLineCoords = new ArrayList<>();
        allMapLines = new ArrayList<>();

        pathLine = new Path();
        mPaint = new Paint();
        mPaint.setColor(lineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(lineWidth);


        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeWidth(3);
        paintText.setTextSize(25);
        paintText.setColor(lineColor);

//        m_TextPaint.setStrokeWidth(3);  // 描边宽度
        paintText.setStyle(Paint.Style.FILL_AND_STROKE); //描边种类
        paintText.setFakeBoldText(true); // 外层text采用粗体

        animator = ValueAnimator.ofFloat(0, 1).setDuration(700);

    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public ArrayList<MapPointWithTitleView> getMapLineCoords() {
        return mapLineCoords;
    }

    /**
     * 移除某条线上的节点
     * @param mapLineCoord
     */
    public void removeMainLineCoord(MapPointWithTitleView mapLineCoord){
        if(mapLineCoords.contains(mapLineCoord)){
            mapLineCoords.remove(mapLineCoord);
            this.invalidate();
        }
    }


    public int getMapLineSize() {
        return mapLineCoords == null? 0:mapLineCoords.size();
    }

    public void clearMapLine() {
        mapLineCoords.clear();
        this.invalidate();
    }

    public MapPointWithTitleView getLine(int index) {
        return mapLineCoords.get(index);
    }

    public void addLine(MapPointWithTitleView mapLineCoord) {
        mapLineCoords.add(mapLineCoord);
        this.invalidate();
    }

    public List<List<MapPointWithTitleView>> getAllMapLines() {
        return allMapLines;
    }

    public void addLines(List<MapPointWithTitleView> mapLineCoords,int goTime) {
        this.goTime = goTime;
        if(allMapLines.size() >0){
            this.allMapLines.clear();
        }
        this.allMapLines.add(mapLineCoords);
        animator.start();
//        this.invalidate();
    }

    public int getAllMapLineSize(){
        return allMapLines.size();
    }

    public List<MapPointWithTitleView> getMapLine(int i){
        return allMapLines.get(i);
    }

    public void clearAllMapLines(){
        allMapLines.clear();
        this.invalidate();
    }

    public void addLines(List<MapPointWithTitleView> mapLineCoords, boolean isRepaint) {
        if (isRepaint) {
            addLines(mapLineCoords,goTime);
        }else {
            this.allMapLines.add(mapLineCoords);
        }

    }
        @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

            pathLine.reset();

            if(mapView !=  null){
                firstScale = mapView.getFirstScale();
                Log.i("BaseMapAndLines","--firstScale--->" + firstScale);
            }

//            for (int i = 0; i < mapLineCoords.size(); i++) {
//                if(i == 0){
//                    canvas.drawCircle(mapLineCoords.get(0).getX() + 40,
//                            mapLineCoords.get(0).getY() + 45,
//                            10, mPaint);
//                }
//
//                //不为1的时候再画结束的点
//                if (mapLineCoords.size() != 1) {
//                    canvas.drawCircle(mapLineCoords.get(mapLineCoords.size() - 1).getX() + 40,
//                            mapLineCoords.get(mapLineCoords.size() - 1).getY() + 45,
//                            10, mPaint);
//                }
//
//                if(i > 0) {
//                    //画线
//                    canvas.drawLine(mapLineCoords.get(i - 1).getX() + 40,
//                            mapLineCoords.get(i - 1).getY() + 45,
//                            mapLineCoords.get(i).getX() + 40,
//                            mapLineCoords.get(i).getY() + 45 , mPaint);
//
//                }
//            }

            //画原有的线
            for (int i = 0; i < allMapLines.size(); i++) {
                List<MapPointWithTitleView> mapLineCoords = allMapLines.get(i);
                if(mapLineCoords != null && mapLineCoords.size() > 0){

                    for (int k = 0; k < mapLineCoords.size(); k++) {
                        MapPointWithTitleView pointView = mapLineCoords.get(k);
                        int width = pointView.getMeasuredWidth();
                        int height = pointView.getMeasuredHeight();
//                        LogUtils.e("path"+ width +"-"+height);
                        if(k == 0){
//                            canvas.drawCircle(mapLineCoords.get(0).getX() + 40,
//                                    mapLineCoords.get(0).getY() + 45,
//                                    pointRadius, mPaint);

                            pathLine.moveTo(pointView.getX() + width /2, pointView.getY() + height /2);
                        }

                        //不为1的时候再画结束的点
//                        if (mapLineCoords.size() != 1) {
//                            canvas.drawCircle(mapLineCoords.get(mapLineCoords.size() - 1).getX() + 40,
//                                    mapLineCoords.get(mapLineCoords.size() - 1).getY() + 45,
//                                    pointRadius, mPaint);
//                        }

                        if (k > 0) {
                            pathLine.lineTo(pointView.getX() + width/2, pointView.getY() + height);
//                            //画线
//                            canvas.drawLine(mapLineCoords.get(k - 1).getX() + 40,
//                                    mapLineCoords.get(k - 1).getY() + 45,
//                                    mapLineCoords.get(k).getX() + 40,
//                                    mapLineCoords.get(k).getY() + 45 , mPaint);
//
                        }
                    }
                }
            }
            PathEffect effects = new DashPathEffect(new float[]{18,10,18,10},1);
            mPaint.setPathEffect(effects);
            mPaint.setStyle(Paint.Style.STROKE);


            Path dst3 = new Path();
            PathMeasure pathMeasure = new PathMeasure(pathLine,false);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mAnimatorValue = (float)animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            pathMeasure.getSegment(0,pathMeasure.getLength() * mAnimatorValue, dst3, true);

//            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(dst3,mPaint);
            if(mAnimatorValue == 1){
                //绘制 路程时间
                pos = new float[2];
                tan = new float[2];
                pathMeasure.getPosTan(pathMeasure.getLength()/2 , pos , tan);

                paintText.setColor(0xFFFFFFFF);
                paintText.setStyle(Paint.Style.FILL_AND_STROKE); //描边种类
                paintText.setFakeBoldText(true); // 外层text采用粗体
                paintText.setStrokeWidth(8);
                canvas.drawText("Walk "+goTime+" minutes",pos[0]-100,pos[1],paintText);

                paintText.setColor(lineColor);
                paintText.setStrokeWidth(3);
                paintText.setFakeBoldText(false);
                paintText.setStyle(Paint.Style.FILL);
                canvas.drawText("Walk "+goTime+" minutes",pos[0]-100,pos[1],paintText);
            }
    }

    public void release(){
        animator.removeAllUpdateListeners();
        animator = null;
    }

    /**
     * 地图 线 拐点 坐标
     */
    public class MapLineCoord {
        private float firstX;
        private float firstY;

        private float viewX;
        private float viewY;

        private int index;

        public MapLineCoord() {
        }

        public MapLineCoord(float firstX, float firstY, float viewX, float viewY) {
            this.firstX = firstX;
            this.firstY = firstY;
            this.viewX = viewX;
            this.viewY = viewY;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public float getFirstX() {
            return firstX;
        }

        public void setFirstX(float firstX) {
            this.firstX = firstX;
        }

        public float getFirstY() {
            return firstY;
        }

        public void setFirstY(float firstY) {
            this.firstY = firstY;
        }

        public float getViewX() {
            return viewX;
        }

        public void setViewX(float viewX) {
            this.viewX = viewX;
        }

        public float getViewY() {
            return viewY;
        }

        public void setViewY(float viewY) {
            this.viewY = viewY;
        }

        @Override
        public String toString() {
            return "MapLineCoord{" +
                    "firstX=" + firstX +
                    ", firstY=" + firstY +
                    ", viewX=" + viewX +
                    ", viewY=" + viewY +
                    '}';
        }
    }
}
