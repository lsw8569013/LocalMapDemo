package com.map.demo.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.map.demo.R;
import com.map.demo.activity.OlympicMapAcyT;
import com.map.demo.util.DensityUtil;
import com.map.demo.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 地图 控件
 * Created by user on 2016/3/3.
 * @author li
 */
public class MapView extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener {
    private static String TAG = MapView.class.getSimpleName();
    private Context context;
    private AppCompatActivity mActivity;
    /**
     * 显示 地图 底图和线 的 控件
     */
    private BaseMapAndLines myBaseMapAndLines;
    /**
     * 点击缩放显示地图
     */
    private ImageView mapBiggerView, mapSmallerView, mapOriginalView;

    /**
     * 地图 原点 左上角
     */
    private View originalPointView;

    /**
     * 地图上 标记 的 点
     */
    private List<MapPointWithTitleView> mapPoints;
    public List<List<MapPointWithTitleView>> allMyMapPoints;//所有线的点的集合
    /**
     * 首次 放大缩小的 倍数 放大缩小的 倍数0.1-20比较合理的缩放
     */
    private float firstScale;
    /**
     * 长按 标志
     */
    private boolean longPressTag = false;

    /**
     * 手指 第一次 按下的 事件 坐标
     */
    private float firstDownX;
    private float firstDownY;

    /**
     * 第一次 按下时间
     */
    private long downTime;
    private float borderSize = R.dimen.x5;

    /**
     * 自定义 地图 长按 监听
     */
    private MapViewOnLongClickListener mapOnLongClickListener;
    private MapViewOnClickListener mMapViewOnClickListener;
    private float mapViewWidth;
    private float mapViewHeight;
    private int screeWidth;
    private int screeHeight;
    public  float tranX = 0;
    public float tranY = 0;
    private Bitmap bitmap;

    //移动地图距离 叠加值X
    private float moveMapX;
    //移动地图距离 叠加值Y
    private float moveMapY;

    public MapView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MapView(Context context, AppCompatActivity activity) {
        super(context);
        this.context = context;
        this.mActivity = activity;
        init();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        LayoutInflater.from(context).inflate(R.layout.view_map, this);
        myBaseMapAndLines = (BaseMapAndLines) this.findViewById(R.id.imageView);
        myBaseMapAndLines.setMapView(this);
        mapBiggerView = (ImageView) this.findViewById(R.id.mapBigger);
        mapSmallerView = (ImageView) this.findViewById(R.id.mapSmaller);
        mapOriginalView = (ImageView) this.findViewById(R.id.mapOriginal);

        originalPointView = new MapPointWithTitleView(context);
        LogUtils.e(  "===originalPointView====" + originalPointView.getY()+"x"+originalPointView.getX());
        mapPoints = new ArrayList<>();
        allMyMapPoints = new ArrayList<>();
        // 设置 触摸 监听
        this.setOnTouchListener(new MyTouchListener());
        mapBiggerView.setOnClickListener(this);
        mapSmallerView.setOnClickListener(this);
        mapOriginalView.setOnClickListener(this);
    }


    public float getFirstScale() {
        return firstScale;
    }

    public void setFirstScale(float firstScale) {
        this.firstScale = firstScale;
    }

    /**
     * 加多个 点  单条线
     *
     * @param list
     */
    public void setMapPoints(List<MapPointWithTitleView> list) {
        clearMapPoints();
        this.mapPoints.addAll(list);
    }

    /**
     * 得到 地图 线 的 集合
     *
     * @return
     */
    public List<MapPointWithTitleView> getMapLines() {
        return myBaseMapAndLines.getMapLineCoords();
    }

    /**
     * 清空 点
     */
    public void clearMapPoints() {
        for (int i = 0; i < mapPoints.size(); i++) {
            this.removeView(mapPoints.get(i));
        }
        this.mapPoints.clear();

        for (int i = 0; i < allMyMapPoints.size(); i++) {
            List<MapPointWithTitleView> views = allMyMapPoints.get(i);
            for (int j = 0; j < views.size(); j++) {
                this.removeView(views.get(j));
            }
            views.clear();
        }

        allMyMapPoints.clear();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    /**
     * 根据 当前 缩放 比例
     * 移动 点 的 位置
     * 用于 放大缩小 按钮
     */
    public void moveMapPoints() {
        for (int i = 0; i < mapPoints.size(); i++) {
            MapPointWithTitleView point = mapPoints.get(i);
            // 设置 点 的 初始位置
            point.setFirstXShow((float) (point.getFirstX() * firstScale));
            point.setFirstYShow((float) (point.getFirstY() * firstScale));
        }

        for (int i = 0; i < allMyMapPoints.size(); i++) {
            List<MapPointWithTitleView> views = allMyMapPoints.get(i);
            for (int j = 0; j < views.size(); j++) {
                MapPointWithTitleView point = views.get(j);
                // 设置 点 的 初始位置
                point.setFirstXShow((float) (point.getFirstX() * firstScale));
                point.setFirstYShow((float) (point.getFirstY() * firstScale));
            }
        }

        myBaseMapAndLines.invalidate();
    }

    /**
     * 根据 当前 缩放 比例
     * 移动 线 的 位置
     * 用于 放大缩小 按钮
     */
    public void moveMapLines() {
        myBaseMapAndLines.invalidate();
    }

    /**
     * 加 多条 线   多条线
     *
     * @param mapLineCoords
     * @param goTime
     */
    public void addMapLines(List<MapPointWithTitleView> mapLineCoords, int goTime) {

        if(mapLineCoords != null &&mapLineCoords.size() > 0){
            for (int i = 0; i < mapLineCoords.size(); i++) {
                allMyMapPoints.clear();
                allMyMapPoints.add(mapLineCoords);
                MapPointWithTitleView mapPointWithTitleView = mapLineCoords.get(i);
                mapPointWithTitleView.setFirstXShow((float) (mapPointWithTitleView.getFirstX() * firstScale + originalPointView.getX()));
                mapPointWithTitleView.setFirstYShow((float) (mapPointWithTitleView.getFirstY() * firstScale + originalPointView.getY()));
//                this.addView(mapPointWithTitleView);
            }
            this.myBaseMapAndLines.addLines(mapLineCoords,goTime);
        }

//        this.invalidate();
    }

    /**
     * 得到 地图 点 的集合
     *
     * @return
     */
    public List<MapPointWithTitleView> getMapPoints() {
        return mapPoints;
    }

    /**
     * 清空 线
     */
    public void clearAllMapLines() {
        this.myBaseMapAndLines.clearAllMapLines();
    }

    /**
     * 添加一个点 并显示
     */
    public void addMapPoint(MapPointWithTitleView myMapPointWithTitleView) {
        Log.i("testss", originalPointView.getX() + "===originalPointView===addMapPoint=" + originalPointView.getY());
        mapPoints.add(myMapPointWithTitleView);
        myMapPointWithTitleView.setFirstXShow((float) (myMapPointWithTitleView.getFirstX() * firstScale + originalPointView.getX()));
        myMapPointWithTitleView.setFirstYShow((float) (myMapPointWithTitleView.getFirstY() * firstScale + originalPointView.getY()));

        this.addView(myMapPointWithTitleView);

        myMapPointWithTitleView.layout((int)(myMapPointWithTitleView.getFirstX() * firstScale + originalPointView.getX()),
                (int)(myMapPointWithTitleView.getFirstY() * firstScale + originalPointView.getY()),
                myMapPointWithTitleView.getWidth(),
                myMapPointWithTitleView.getHeight()
        );

//        myBaseMapAndLines.addLine(myMapPointWithTitleView);

        this.invalidate();
    }


    /**
     * 设置 地图 底图 并显示
     *
     * @param bitmap
     * @param bitmapWith
     * @param bitmapHeight
     */
    public void setMapState(Bitmap bitmap, int bitmapWith, int bitmapHeight,int titleHeight) {
        this.bitmap = bitmap;
        LogUtils.e("bitmapWith="+bitmapWith+",bitmapHeight="+bitmapHeight+",-titleHeight="+titleHeight);
        myBaseMapAndLines.setImageBitmap(bitmap);
        // 获取 屏幕 宽度
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        //应用区域
        Rect outRect1 = new Rect();
        ((OlympicMapAcyT)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        // 屏幕宽度（像素）
        screeWidth = metric.widthPixels;
        // 屏幕高度（像素）
        screeHeight = outRect1.height() - titleHeight ;
        LogUtils.e("状态栏高度"+ DensityUtil.getStatusHeight(context));
        // 显示 地图
        if(bitmapWith < screeWidth){
            //       如果宽度 小于 屏幕宽度按宽缩放
            showMap((float) screeWidth / (float) bitmapWith,bitmapHeight,bitmapWith);
        }else if(bitmapHeight < screeHeight){
//            按高缩放
            showMap((float) screeHeight / (float) bitmapHeight,bitmapHeight,bitmapWith);
        }else {
            //按原图大小显示
            showMapNoScale(bitmapHeight,bitmapWith);
        }
    }

    private void showMapNoScale(int bitmapHeight, int bitmapWith) {
        Matrix matrix = new Matrix();
        mapViewWidth = bitmapWith ;
        mapViewHeight = bitmapHeight;

        if(mapViewWidth > screeWidth){
            //宽度平移
            tranX = -(mapViewWidth - screeWidth) / 2;
        }
        if(mapViewHeight > screeHeight){
//           高度平移
            tranY = -(mapViewHeight - screeHeight) / 2;
        }
        //map 地图可见区域大小设置
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = (int) screeWidth;
        layoutParams.height = (int) screeHeight;
        setLayoutParams(layoutParams);
        myBaseMapAndLines.setLayoutParams(layoutParams);
        LogUtils.e("tranx="+tranX+",tranY="+tranY +",gao-"+(mapViewHeight-screeHeight)+",kuan"+ (mapViewWidth-screeWidth));
        myBaseMapAndLines.setImageMatrix(matrix);
    }




    /**
     * 根据 传入 的 缩放 比例 显示 地图
     *
     * @param scale 地图缩放 比例
     * @param bitmapHeight
     * @param bitmapWith
     */
    private void showMap(float scale, int bitmapHeight, int bitmapWith) {
        this.firstScale = scale;
        LogUtils.e("地图缩放比例--"+scale);
        Matrix matrix = new Matrix();
        // 放大缩小 适应屏幕宽度
        matrix.preScale(this.firstScale, this.firstScale);
        mapViewWidth = bitmapWith * scale ;
        originalPointView.setX((originalPointView.getX() * scale));
        mapViewHeight = bitmapHeight * scale;
        originalPointView.setY((originalPointView.getY() * scale));

        if(mapViewWidth > screeWidth){
            //宽度平移
            tranX = -(mapViewWidth - screeWidth) / 2;
//            matrix.postTranslate(tranX,0 );
        }else{
            tranX = (screeWidth - mapViewWidth) / 2;
        }
        if(mapViewHeight > screeHeight){
//           高度平移
            tranY = -(mapViewHeight - screeHeight) / 2;
//            matrix.postTranslate(0, tranY);
        }else{
            tranY = ( screeHeight - mapViewHeight ) /2 ;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = (int) mapViewWidth;
        layoutParams.height = (int) mapViewHeight;
        setLayoutParams(layoutParams);
        myBaseMapAndLines.setLayoutParams(layoutParams);

        LogUtils.e("tranx-"+tranX+"---tranY=="+tranY +"-gao=="+(mapViewHeight-screeHeight)+"--kuan"+ (mapViewWidth-screeWidth));
        myBaseMapAndLines.setImageMatrix(matrix);
//        moveMapView(tranX,tranY);
    }


    /**
     * 地图 按钮 点击 事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            放大图片
            case R.id.mapBigger:
//                showMap(this.firstScale + 0.5f);
//                moveMapPoints();
//                moveMapLines();
                break;
            case R.id.mapOriginal:
                //原图显示
                showMap(1, bitmap.getHeight(), bitmap.getWidth());
                moveMapPoints();
                moveMapLines();
                break;
            case R.id.mapSmaller:
                //缩小图片
                showMap(this.firstScale - 0.5f, bitmap.getHeight(), bitmap.getWidth());
                moveMapPoints();
                moveMapLines();
                break;
            default:
                break;
        }
    }

    /**
     * 设置 地图 自定义 长按 监听
     *
     * @param mapOnLongClickListener 自定义 地图 长按 监听器
     */
    public void setMapOnLongClickListener(MapViewOnLongClickListener mapOnLongClickListener) {
        this.mapOnLongClickListener = mapOnLongClickListener;
        this.setOnLongClickListener(this);
    }

    public MapViewOnLongClickListener getMapOnLongClickListener() {
        return mapOnLongClickListener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (longPressTag) {

            float firstDownX = getFirstDownX();
            float firstDownY = getFirstDownY();
            Log.i(TAG, "===onLongClick==firstDownevent==-x--" + firstDownX + "===y====" + firstDownY);
            float fingerX = firstDownX - getX();
            float fingerY = firstDownY - getY();
            Log.i(TAG, "===onLongClick==firstDownevent=finger=-x--" + fingerX + "===y====" + fingerY);

            float originalPointViewX = originalPointView.getX();
            float originalPointViewY = originalPointView.getY();

            Log.i(TAG, "===onLongClick==originalPointViewX==" + originalPointViewX + "====originalPointViewX====" + originalPointViewY);

            float x = (fingerX - originalPointViewX) / firstScale;
            float y = (fingerY - originalPointViewY) / firstScale;

            // 长按 事件 回调
            this.mapOnLongClickListener.mapViewOnLongClick(x, y);
            return true;
        }
        return false;
    }


    public float getFirstDownX() {
        return firstDownX;
    }

    public void setFirstDownX(float firstDownX) {
        this.firstDownX = firstDownX;
    }

    public float getFirstDownY() {
        return firstDownY;
    }

    public void setFirstDownY(float firstDownY) {
        this.firstDownY = firstDownY;
    }

    public float getTransX() {
        return moveMapX;
    }
    public float getTransY() {
        return moveMapY;
    }

    /**
     * 移动当前图标，到地图 居中位置
     *  。
     * @param x 横向坐标点
     * @param y
     */
    public void moveLableToCenter(int x, int y) {
        float willMoveX = x+moveMapX ;
//        LogUtils.e("原来移动 x 距离" + moveMapX);
        willMoveX = screeWidth/2 - willMoveX;
//        LogUtils.e("需要移动 x 距离"+ willMoveX +"屏幕中心x"+screeWidth /2);
        float willMoveY =screeHeight / 2 - (moveMapY +   y);
        moveMapView(willMoveX,willMoveY);
    }

    public void releaseView() {
        myBaseMapAndLines.release();
        context = null;
    }

    /**
     * 自定义 地图 长按 事件
     */
    public interface MapViewOnLongClickListener {

        /**
         * 手指 按下 相对 与 地图左上角 原点  原始的 x ，y 坐标
         *
         * @param downX x坐标
         * @param downY y 坐标
         */
        void mapViewOnLongClick(float downX, float downY);
    }

    /**
     * 自定义 地图 点击 事件
     */
    public interface  MapViewOnClickListener{
        /**
         * 手指 按下 相对 与 地图左上角 原点  原始的 x ，y 坐标
         * @param view 组件
         * @param downX x坐标
         * @param downY y 坐标
         */
        void mapViewOnClick(View view, float downX, float downY);
    }

    /**
     * 地图 移动 放大 监听
     */
    private class MyTouchListener implements OnTouchListener {
        /**
         * 记录是拖拉照片模式还是放大缩小照片模式
         */
        private int mode = 0;// 初始状态
        /**
         * 拖拉照片模式
         */
        private static final int MODE_DRAG = 1;
        /**
         * 放大缩小照片模式
         */
        private static final int MODE_ZOOM = 2;

        /**
         * 用于记录开始时候的坐标位置
         */
        private PointF startPoint = new PointF();
        /**
         * 用于记录拖拉图片移动的坐标位置
         */
        private Matrix matrix = new Matrix();
        /**
         * 用于记录图片要进行拖拉时候的坐标位置
         */
        private Matrix currentMatrix = new Matrix();

        /**
         * 两个手指的开始距离
         */
        private float startDis;
        /**
         * 两个手指的中间点
         */
        private PointF midPoint;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float dx;
            float dy;
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(myBaseMapAndLines.getImageMatrix());
                    startPoint.set(event.getX(), event.getY());

                    // 当 手指 按下时 初始化 长按标志
                    longPressTag = true;
                    downTime = System.currentTimeMillis();

                    setFirstDownX(event.getX());
                    setFirstDownY(event.getY());
                    break;
                // 手指在屏幕上移动，改事件会被不断触发
                case MotionEvent.ACTION_MOVE:
                    // 拖拉图片
                    if (mode == MODE_DRAG) {
                        dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                        dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                        // 在没有移动之前的位置上进行移动
                        matrix.set(currentMatrix);
                        matrix.postTranslate(dx, dy);
                        // 如果 手指移动 距离不超过 5 个像素点 的 视为 没有移动
                        float offset = (float) Math.sqrt(dx * dx + dy * dy);
                        longPressTag = offset < 5;
                    }

                    // 放大缩小图片
                    else if (mode == MODE_ZOOM) {
                        float endDis = distance(event);// 结束距离
                        if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                            float scale = endDis / startDis;// 得到缩放倍数
                            matrix.set(currentMatrix);
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        }
                    }
                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    //如果 按下 抬起 时间 大于 2s 则是 长按 事件
                    longPressTag = System.currentTimeMillis() - downTime > 2000;
                    if(mode == MODE_DRAG){
                        boolean moveTime = System.currentTimeMillis() - downTime < 300;
                        if(moveTime){
                            //
//                            LogUtils.e("快速滑动");
                        }
                    }


                    // 当触点离开屏幕，但是屏幕上还有触点(手指)
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    break;
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    longPressTag = false;
                    //注释此代码屏蔽缩放
//                    mode = MODE_ZOOM;
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
                        //记录当前ImageView的缩放倍数
                        currentMatrix.set(myBaseMapAndLines.getImageMatrix());
                    }
                    break;
                default:
                    break;
            }

            /**
             * 如果 此次 触摸事件  是  移动，放大事件
             * 则 改变 地图 和 坐标点的位置
             */
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_MOVE:

                    float[] matrixValues = new float[9];
                    matrix.getValues(matrixValues);
                    //如果判断，是否移动 过界 ,经过测试左和上 x  y 地址 = 0 是超出屏幕，显示灰色背景

//                  LogUtils.e( "移动后原点的x距离" + (0 * matrixValues[0]+  matrixValues[2]));
//                  LogUtils.e("移动后原点的y距离" + (0 * matrixValues[4] + matrixValues[5]));

                    float moveX = 0 * matrixValues[0] + matrixValues[2];
                    float moveY = 0 * matrixValues[4] + matrixValues[5];
//                    LogUtils.e("x==="+ matrixValues[2]);
//                  -------------------------
//                    if(moveX == 0 || Math.abs(moveX)  == (mapViewWidth-screeWidth) ){
//                        LogUtils.e("x 原值==="+ matrixValues[2]);
//                    }
//                    if( Math.abs(moveX)  == (mapViewWidth-screeWidth)){
//                        LogUtils.e("x边界点"+ matrixValues[2]+"--- 计算的值" +(mapViewWidth-screeWidth));
//                    }
                    if(moveY  == 0  || moveY == mapViewHeight - screeHeight ){
//                        LogUtils.e("y==="+ matrixValues[5]);
                    }
                    //----------边界检测
                    if(moveX > 0    ) {
                        moveX = 0;
//                        LogUtils.e("x 越界");
                        matrixValues[2] = 0;
                    }else if( Math.abs(moveX)  >= (mapViewWidth-screeWidth)){
                        matrixValues[2] = -Math.abs(mapViewWidth-screeWidth);
                    }

                    if(moveY  > 0 ) {
                        moveY = 0;
//                        LogUtils.e("y 越界");
                        matrixValues[5] = 0;
                    }else if(Math.abs(moveY)  >= (mapViewHeight-screeHeight)){
                        matrixValues[5] = -Math.abs(mapViewHeight-screeHeight);
                    }

                    moveMapX = matrixValues[2];
                    moveMapY = matrixValues[5];

                    matrix.setValues(matrixValues);

                    //----------------根据手势 移动地图的view
                    // 移动 地图
                    myBaseMapAndLines.setImageMatrix(matrix);

                    // 地图原点移动


                    originalPointView.setX((float) moveX);
                    originalPointView.setY((float) moveY);

//                        Log.e("testss", originalPointView.getX() + "===originalPointView====" + originalPointView.getY());

                    firstScale = matrixValues[0];

                    // 移动 点
                    for (int i = 0; i < mapPoints.size(); i++) {
                        double scaleX = mapPoints.get(i).getFirstX() * matrixValues[0];
                        double scaleY = mapPoints.get(i).getFirstY() * matrixValues[4];
                        mapPoints.get(i).setFirstXShow((float) (scaleX + matrixValues[2]));
                        mapPoints.get(i).setFirstYShow((float) (scaleY + matrixValues[5]));
                    }

                    for (int i = 0; i < allMyMapPoints.size(); i++) {

                        List<MapPointWithTitleView> views = allMyMapPoints.get(i);
                        for (int j = 0; j < views.size(); j++) {
                            double scaleX = views.get(j).getFirstX() * matrixValues[0];
                            double scaleY = views.get(j).getFirstY() * matrixValues[4];
                            views.get(j).setFirstXShow((float) (scaleX + matrixValues[2]));
                            views.get(j).setFirstYShow((float) (scaleY + matrixValues[5]));
                        }

                    }
//
                // 移动 线
                for (int i = 0; i < myBaseMapAndLines.getMapLineSize(); i++) {
                    float v1 = (float) (myBaseMapAndLines.getLine(i).getFirstX() * matrixValues[0] + matrixValues[2]);
                    float v2 = (float) (myBaseMapAndLines.getLine(i).getFirstY() * matrixValues[4] + matrixValues[5]);
                    myBaseMapAndLines.getLine(i).setFirstXShow(v1);
                    myBaseMapAndLines.getLine(i).setFirstYShow(v2);
                }

                //线的集合
                for (int i = 0; i < myBaseMapAndLines.getAllMapLineSize(); i++) {

                    List<MapPointWithTitleView> mapLine = myBaseMapAndLines.getMapLine(i);
                    for (int j = 0; j < mapLine.size(); j++) {
                        float v1 = (float) (mapLine.get(i).getFirstX() * matrixValues[0] + matrixValues[2]);
                        float v2 = (float) (mapLine.get(i).getFirstY() * matrixValues[4] + matrixValues[5]);
                        mapLine.get(i).setFirstXShow(v1);
                        mapLine.get(i).setFirstYShow(v2);
                    }
                }
                myBaseMapAndLines.invalidate();


                    /**
                     *如果 外层为ScrollView 此句代码是解决
                     * 地图的移动 和 ScrollView 的滚动冲突的
                     * 当触摸事件在地图范围内时，ScrollView 滚动事件无法响应
                     * 当触摸事件在 地图范围外时，ScrollView可以滚动
                     */
                    getParent().requestDisallowInterceptTouchEvent(true);

                    break;
                default:
                    break;
            }

            // 如果 设置 了 长按 监听 则 传递 事件
            // 否则 自己 消费 该 事件
            if (getMapOnLongClickListener() != null) {
                return false;
            }
            return true;
        }


        /**
         * 计算两个手指间的距离
         */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        /**
         * 计算两个手指间的中间点
         */
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }
    }

    public float getTranX() {
        return tranX;
    }

    public void setTranX(float tranX) {
        this.tranX = tranX;
    }

    public float getTranY() {
        return tranY;
    }

    public void setTranY(float tranY) {
        this.tranY = tranY;
    }

    /**
     * 主动移动地图
     */
    public void moveMapView(float x,float y){
        //down
        Matrix currentMatrix = new Matrix();
        Matrix matrix = new Matrix();
        currentMatrix.set(myBaseMapAndLines.getImageMatrix());
//            startPoint.set(event.getX(), event.getY());


        matrix.set(currentMatrix);
        matrix.postTranslate(x, y);



        float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);



        float moveX = 0 * matrixValues[0] + matrixValues[2];
        float moveY = 0 * matrixValues[4] + matrixValues[5];
        //----------边界检测
        if(moveX > 0    ) {
            moveX = 0;
//                        LogUtils.e("x 越界");
            matrixValues[2] = 0;
        }else if( Math.abs(moveX)  >= (mapViewWidth-screeWidth)){
            matrixValues[2] = -Math.abs(mapViewWidth-screeWidth);
        }

        if(moveY  > 0 ) {
            moveY = 0;
//                        LogUtils.e("y 越界");
            matrixValues[5] = 0;
        }else if(Math.abs(moveY)  >= (mapViewHeight-screeHeight)){
            matrixValues[5] = -Math.abs(mapViewHeight-screeHeight);
        }

        moveMapX = matrixValues[2];
        moveMapY = matrixValues[5];

        matrix.setValues(matrixValues);

        //----------------根据手势 移动地图的view
        // 移动 地图
        myBaseMapAndLines.setImageMatrix(matrix);

        // 地图原点移动
        originalPointView.setX((float) moveX);
        originalPointView.setY((float) moveY);


        firstScale = matrixValues[0];

        // 移动 点
        for (int i = 0; i < mapPoints.size(); i++) {
            double scaleX = mapPoints.get(i).getFirstX() * matrixValues[0];
            double scaleY = mapPoints.get(i).getFirstY() * matrixValues[4];
            mapPoints.get(i).setFirstXShow((float) (scaleX + matrixValues[2]));
            mapPoints.get(i).setFirstYShow((float) (scaleY + matrixValues[5]));
        }

        for (int i = 0; i < allMyMapPoints.size(); i++) {

            List<MapPointWithTitleView> views = allMyMapPoints.get(i);
            for (int j = 0; j < views.size(); j++) {
                double scaleX = views.get(j).getFirstX() * matrixValues[0];
                double scaleY = views.get(j).getFirstY() * matrixValues[4];
                views.get(j).setFirstXShow((float) (scaleX + matrixValues[2]));
                views.get(j).setFirstYShow((float) (scaleY + matrixValues[5]));
            }

        }
//
        // 移动 线
        for (int i = 0; i < myBaseMapAndLines.getMapLineSize(); i++) {
            float v1 = (float) (myBaseMapAndLines.getLine(i).getFirstX() * matrixValues[0] + matrixValues[2]);
            float v2 = (float) (myBaseMapAndLines.getLine(i).getFirstY() * matrixValues[4] + matrixValues[5]);
            myBaseMapAndLines.getLine(i).setFirstXShow(v1);
            myBaseMapAndLines.getLine(i).setFirstYShow(v2);
        }

        //线的集合
        for (int i = 0; i < myBaseMapAndLines.getAllMapLineSize(); i++) {
            List<MapPointWithTitleView> mapLine = myBaseMapAndLines.getMapLine(i);
            for (int j = 0; j < mapLine.size(); j++) {
                float v1 = (float) (mapLine.get(i).getFirstX() * matrixValues[0] + matrixValues[2]);
                float v2 = (float) (mapLine.get(i).getFirstY() * matrixValues[4] + matrixValues[5]);
                mapLine.get(i).setFirstXShow(v1);
                mapLine.get(i).setFirstYShow(v2);
            }
        }
        myBaseMapAndLines.invalidate();

    }

}
