package com.map.demo.mapview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.map.demo.R;
import com.map.demo.activity.OlympicMapAcyT;
import com.map.demo.bean.LableBean;
import com.map.demo.config.App;

import java.lang.ref.WeakReference;


/**
 * 自定义 地图 坐标 点
 * Created by Administrator on 2016/6/30.
 *  @author li
 */
public class MapPointWithTitleView extends LinearLayout
        implements View.OnClickListener, View.OnLongClickListener/*, PointClickPopupWindow.OnTextClickListener*/ {

    private static String TAG = MapPointWithTitleView.class.getSimpleName();

    public static final int RED_POINT = 0;
    public static final int BLUE_POINT = 1;
    public static final int GRAY_POINT = 2;
    public static final int BLACK_POINT = 3;
    public static final int PEOPLE_POINT = 4;
    public static final int DEFAULT_POINT = 5;
    public static final int GREEN_POINT = 6;
    public static final int YELLOW_POINT = 7;

    /**
     * 点的底图
     */
    private ImageView textPointIcon;

    /**
     * 点的右上角tag
     */
    private TextView tv_labe_info;
    /**
     *  根布局
     */
    private RelativeLayout pointLayout;


    private AppCompatActivity mActivity;
    /**
     * 初始 位置
     */
    private double firstX;
    private double firstY;

    /**
     * 点的 边界
     */
    private double borderTop;
    private double borderLeft;
    /**
     * 点的中心点位置
     */
    private double pointCenterX;
    private double pointCenterY;
    /** 
     * 点 的 显示 状态
     */
    private int pointState;
    /**
     * 点 的 名称
     */
    private String pointName;
    /**
     * 是否 显示 点 名称
     */
    private boolean isNameShow;
    private View mView;
    /**
     * 顺序 在集合中的顺序
     */
    private int pointIndex;
    /**
     * 构造方法传递过来的数据参数
     */
    private PointPosition mPointPosition;
    public RelativeLayout map_point_linear;
    private OlympicMapAcyT olympicMapAcyT;
    private ImageView iv_myLocation_bg;
    public View rl_pop_layout;
    private LableBean lableBean;
    private ImageView iv_left_icon;
    private ProgressBar progressbar_park;
    private TextView tv_park_count;
    private ValueAnimator mAnimator;

    public LableBean getLableBean() {
        return lableBean;
    }

    public void setLableBean(LableBean lableBean) {
        this.lableBean = lableBean;
    }

    public MapPointWithTitleView(Context context) {
        super(context);
        init();
    }

    public MapPointWithTitleView(Context context, AppCompatActivity activity) {
        super(context);

        this.mActivity = activity;
        init();
    }

    public MapPointWithTitleView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

        init();
    }

    public MapPointWithTitleView(WeakReference contextWf, double pointX, double pointY,
                                 int pointState, boolean isNameShow, String pointName) {
        super((Context) contextWf.get());

        this.firstX = pointX;
        this.firstY = pointY;
        this.pointState = pointState;
        this.pointName = pointName;
        this.isNameShow = isNameShow;
        init();
    }

    /**
     * 传递对象参数进来
     *
     * @param context
     * @param activity
     * @param pointX
     * @param pointY
     * @param pointState
     * @param isNameShow
     * @param pointPosition
     */
    public MapPointWithTitleView(Context context, AppCompatActivity activity,
                                 double pointX, double pointY, int pointState,
                                 boolean isNameShow, PointPosition pointPosition){
        super(context);

        this.mActivity = activity;
        this.firstX = pointX;
        this.firstY = pointY;
        this.pointState = pointState;
        this.isNameShow = isNameShow;
        this.mPointPosition = pointPosition;
        init();
    }

    public void setFirstXShow(float x) {
        //改为中心点
        x -= pointCenterX;
        setX(x);
    }

    public void setFirstYShow(float y) {
        //改为中心点
        y -= pointCenterY;
        setY(y);
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_map_point, this);
        textPointIcon = (ImageView) view.findViewById(R.id.pointIcon);
        iv_myLocation_bg = view.findViewById(R.id.iv_myLocation_bg);

        iv_left_icon = view.findViewById(R.id.iv_left_icon);
        progressbar_park = findViewById(R.id.progressbar_park);
        tv_park_count = (TextView) view.findViewById(R.id.tv_park_count);

        rl_pop_layout = view.findViewById(R.id.rl_pop_layout);
        tv_labe_info = (TextView) view.findViewById(R.id.tv_labe_info);
        pointLayout = (RelativeLayout) view.findViewById(R.id.map_point_relative);
        map_point_linear = view.findViewById(R.id.map_point_linear);
        // 设置 显示 内容
        setPointIcon();

        if(!isNameShow){
            progressbar_park.setVisibility(View.VISIBLE);
            tv_park_count.setVisibility(View.VISIBLE);
        }

        //传递title过来
        if(!TextUtils.isEmpty(pointName)){

            //传递参数对象过来
        }else if(mPointPosition != null){
            this.pointName = mPointPosition.getTitle();
            //int型
            String tag = "p" + mPointPosition.getPosition();
            tv_labe_info.setText(tag);
        }

        // 测量 边界
        measureBorder();

        // 设置 监听
        this.setOnClickListener(this);
        this.setOnLongClickListener(this);
    }

    public void setLayoutVisible(boolean isVisible){
        if(!isVisible){
            pointLayout.setVisibility(GONE);
            mView.setClickable(false);
        }
    }

    /**
     * 返回指定的pos
     * @return pos
     */
    public String getPosition(){
        if (mPointPosition != null) {
            return mPointPosition.getPosition();
        }
        return "";
    }

    public int getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(int pointIndex) {
        this.pointIndex = pointIndex;
    }

    /**
     * 测量 地图点 的 边界
     */
    private void measureBorder() {
        if(!isNameShow){
            ViewGroup.LayoutParams layoutParams = progressbar_park.getLayoutParams();
            layoutParams.width = (int) App.getApplication().getBaseContext().getResources().getDimension(R.dimen.x70);
        }
        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        progressbar_park.measure(width,height);
        this.rl_pop_layout.measure(width, height);
//        this.textPointName.measure(width, height);
        this.measure(width, height);

        if (!isNameShow) {
//            textPointName.setVisibility(INVISIBLE);
        } else {
//            textPointName.setVisibility(VISIBLE);

            if(!TextUtils.isEmpty(pointName)){
//                textPointName.setText(pointName);
                //传递参数对象过来
            }else if(mPointPosition != null){
                this.pointName = mPointPosition.getTitle();
//                textPointName.setText(mPointPosition.getTitle());
                //int型
                String tag = "p" + mPointPosition.getPosition();
                tv_labe_info.setText(tag);
            }
        }

        //图形的左上角坐标点------相对于底图的位置
        borderLeft = (this.getMeasuredWidth() - this.textPointIcon.getMeasuredWidth()) / 2;
        borderTop = (this.getMeasuredHeight() - this.textPointIcon.getMeasuredHeight()
                            );
        //测量title的宽度，居中点

        this.pointCenterX = this.getMeasuredWidth()/2;
        this.pointCenterY = this.getMeasuredHeight()/2;
//        Log.e(TAG, this.pointCenterX + "==x==point==y===" + this.pointCenterY);
    }

    /**
     * 设置 显示 图标
     */
    private void setPointIcon() {
        // 默认 点显示 在 左上角的位置
        int pointPictureName;
        switch (this.pointState) {
            case RED_POINT:
                pointPictureName = R.mipmap.point_icon_red;
                break;
            case BLUE_POINT:
                pointPictureName = R.mipmap.point_icon_blue;
                break;
            case GRAY_POINT:
                pointPictureName = R.mipmap.point_icon_gray;
                break;
            case BLACK_POINT:
                pointPictureName = R.mipmap.point_icon_black;
                break;
            case PEOPLE_POINT:
                pointPictureName = R.mipmap.point_icon_people;
                break;
            case GREEN_POINT:
                pointPictureName = R.mipmap.point_icon_green;
                break;
            case YELLOW_POINT:
                pointPictureName = R.mipmap.point_icon_yellow;
                break;
            case DEFAULT_POINT:
            default:
                pointPictureName = R.mipmap.point_icon_blue;
                break;
        }
        this.textPointIcon.setImageResource(pointPictureName);
    }

    /**
     * 自己设置显示icon
     * @param pointIcon
     */
    public void setPointIcon(int pointIcon,int popLeftIcon){
        if(pointIcon != 0){
            this.textPointIcon.setImageResource(pointIcon);
        }
        iv_left_icon.setImageResource(popLeftIcon);

//        Drawable rightDrawable = getResources().getDrawable(popLeftIcon);
//        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
//        tv_labe_info.setCompoundDrawables(rightDrawable, null,null , null);

        this.invalidate();
    }

    /**
     * 自己位置 需要一个缩放动画
     */
    public void showPointIconBG() {
        iv_myLocation_bg.setVisibility(View.VISIBLE);
        textPointIcon.setVisibility(View.GONE);
        findViewById(R.id.iv_myLocationPoint).setVisibility(View.VISIBLE);
        myLocationAnim(iv_myLocation_bg);
    }

    /**
     * 自己位置 闪烁动画
     *
     * @param iv_myLocation
     */
    private void myLocationAnim(final View iv_myLocation) {
        mAnimator = ValueAnimator.ofFloat(0.7f, 1.0f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                float animatorValue = (float) animation.getAnimatedValue();
                iv_myLocation.setScaleX(animatorValue);
                iv_myLocation.setScaleY(animatorValue);
            }
        });
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.setDuration(700);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.start();
    }
    @Override
    public void onClick(View v) {

//        ToastUtil.showToast("onClick"+"---"+firstX+firstY);

        Log.i(TAG,"downX--->>" + firstX + "downY--->>" + firstY);
        if (mOnPointClickListener != null) {
            mOnPointClickListener.onPointClick(v, mPointPosition.getPosition(),
                    mPointPosition,firstX,firstY,pointState);
        }
    }

    @Override
    public boolean onLongClick(View v) {
//        String skipTag=PointClickPopupWindow.SKIP,markTag=PointClickPopupWindow.MARK;
//        Log.i(TAG,"长按时间");
//        ToastUtil.showToast("onLongClick"+"---");
//        new PointClickPopupWindow(context).showPop(v,mPointPosition,skipTag,markTag).setOnTextClickListener(this);


        return true;
    }

    /**
     * 跳过  标记--点击事件
     *
     * @param view
     * @param devicePosition
     * @param skipTag
     * @param skipPopupWindow
     */
//    @Override
//    public void onSkipClick(View view, PointPosition devicePosition, String skipTag, PointClickPopupWindow skipPopupWindow) {
//
//        Log.i(TAG,"------>是否跳检");
////        Toast.makeText(context, "是否设置标识", Toast.LENGTH_SHORT).show();
//
//        skipPopupWindow.dismiss();
//    }

    /**
     * 设置巡检点的标记 颜色
     *
     * @param devicePosition
     *
     *
     * @param deltag
     * @param color
     */
    private void setDeviceNodeDeltag(PointPosition devicePosition,int deltag,int color){
        Log.i(TAG,"设置标识");
//        Toast.makeText(context, "设置标识", Toast.LENGTH_SHORT).show();
    }

    public void dissmissTag(boolean b) {
        tv_labe_info.setVisibility(View.GONE);
    }

    /**
     * 显示地点详细信息
     * @param
     * @param
     * @param
     */
    public void showPopWin(WeakReference weakReference, LableBean lableBean) {
        this.lableBean = lableBean;
        map_point_linear.setVisibility(View.GONE);
        tv_labe_info.setText(lableBean.getName());
        rl_pop_layout.setVisibility(View.VISIBLE);
        // 根据i 获取到 详细信息。显示 pop 里面icon 和名字
//        1 展馆 2 shop 3 停车场 4 公交站 5 wc 6 餐厅

        OlympicMapAcyT mOlympicMapAcyT = (OlympicMapAcyT) weakReference.get();
        if(mOlympicMapAcyT != null){
            mOlympicMapAcyT.showTopAdvertImageAndPath(lableBean);
        }
    }

    public void setTv_labe_info(String name){
        tv_labe_info.setText(name);
    }

    /**
     * 设置停车场 park 停车情况
     * @param parkCount
     */
    public void setParkCount(String parkCount) {
        tv_park_count.setText(tv_park_count.getText().toString().trim()+parkCount);
    }


    public interface OnPointClickListener{
        /**
         * 点击回调
         *
         * @param view
         * @param position
         * @param devicePosition
         * @param x
         * @param y
         * @param state
         */
        void onPointClick(View view, String position, PointPosition devicePosition, double x, double y, int state);
    }

    private OnPointClickListener mOnPointClickListener;

    public void setOnPointClickListener(OnPointClickListener onPointClickListener){
        this.mOnPointClickListener = onPointClickListener;
    }

    public PointPosition getPointPosition() {
        return mPointPosition;
    }

    public void setPointPosition(PointPosition pointPosition) {
        mPointPosition = pointPosition;
    }

    public double getFirstY() {
        return firstY;
    }

    public void setFirstY(double firstY) {
        this.firstY = firstY;
    }

    public double getFirstX() {
        return firstX;
    }

    public void setFirstX(double firstX) {
        this.firstX = firstX;
    }

    public int getPointState() {
        return pointState;
    }

    public void setPointState(int pointState) {
        this.pointState = pointState;
        this.invalidate();
    }

    public double getPointCenterX() {
        return pointCenterX;
    }

    public void setPointCenterX(double pointCenterX) {
        this.pointCenterX = pointCenterX;
    }

    public double getPointCenterY() {
        return pointCenterY;
    }

    public void setPointCenterY(double pointCenterY) {
        this.pointCenterY = pointCenterY;
    }

    public void releaseContext(){

        if(olympicMapAcyT != null){
            olympicMapAcyT = null;
        }
        if(mAnimator != null){
            mAnimator.removeAllUpdateListeners();
        }
        iv_myLocation_bg.clearAnimation();

    }

}
