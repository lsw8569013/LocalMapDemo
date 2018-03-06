package com.map.demo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.map.demo.R;
import com.map.demo.base.BaseAppCompatActivity;
import com.map.demo.bean.LableBean;
import com.map.demo.bean.LoginUserInfo;
import com.map.demo.bean.MapBean;
import com.map.demo.config.App;
import com.map.demo.mapview.MapPointWithTitleView;
import com.map.demo.mapview.MapView;
import com.map.demo.util.ClearLeakUtils;
import com.map.demo.util.DumpingInterpolator;
import com.map.demo.util.ImageViewTouchLisenter;
import com.map.demo.util.LocalJsonResolutionUtils;
import com.map.demo.util.LogUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wb-lsw350290 on 2017/12/6.
 * <p>
 * RP 口袋奥运 acy
 */
public class OlympicMapAcyT extends BaseAppCompatActivity implements MapView.MapViewOnLongClickListener {

    @BindView(R.id.ll_bottom_layout)
    LinearLayout llBottomLayout;
    @BindView(R.id.iv_recommend)
    ImageView ivRecommend;
    @BindView(R.id.tv_recommed_title)
    TextView tv_recommed_title;

    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    //    @BindView(R.id.rl_back)
//    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;


    @BindView(R.id.iv_shop_bg)
    RelativeLayout ivShopBg;
    @BindView(R.id.iv_canting_bg)
    RelativeLayout ivCantingBg;
    @BindView(R.id.iv_zhanguang_bg)
    RelativeLayout ivZhanguangBg;
    @BindView(R.id.iv_park_bg)
    RelativeLayout ivParkBg;
    @BindView(R.id.iv_bus_bg)
    RelativeLayout ivBusBg;
    @BindView(R.id.iv_cesuo_bg)
    RelativeLayout ivCesuoBg;
    //地图上面 推荐图片显示
    @BindView(R.id.rl_recommend)
    RelativeLayout rlRecommend;


    @BindView(R.id.tv_shop)
    TextView tvShop;
    @BindView(R.id.tv_canting)
    TextView tvCanting;
    @BindView(R.id.tv_zhanguang_icon)
    TextView tvZhanguangIcon;
    @BindView(R.id.tv_pack)
    TextView tvPack;
    @BindView(R.id.tv_bus)
    TextView tvBus;
    @BindView(R.id.tv_wc)
    TextView tvWc;




    private MapView mapView;
    private Handler mHandler = new MyHandler(this);

    private static final int ZHAN_GUANG = 3;
    private static final int WC = 1;
    private static final int CAN_TING = 2;
    private static final int BIAN_LI_DIAN = 4;
    private static final int BUS = 5;
    private static final int PARKING = 6;

    private int select = -1;


    private MapPointWithTitleView fishLable;
    private MapPointWithTitleView qiuLable;
    private boolean firstClick = true;
    private MapPointWithTitleView myLocation;
    private View toolBar;
    private Bitmap bitmap;
    private ArrayList<LableBean> busLableData;
    private ArrayList<LableBean> parkLableData;
    private ArrayList<LableBean> restanruntLableData;
    private ArrayList<LableBean> shopLableData;
    private ArrayList<LableBean> washroomLableData;
    private ArrayList<LableBean> venueLableData1;

    private ArrayList<MapPointWithTitleView> venueMapPointViews;
    private LableBean myLocationLable;
    private ArrayList<MapPointWithTitleView> busMapPointViews;
    private ArrayList<MapPointWithTitleView> parkMapPointViews;
    private ArrayList<MapPointWithTitleView> ctMapPointViews;
    private ArrayList<MapPointWithTitleView> shopMapPointViews;
    private ArrayList<MapPointWithTitleView> wcMapPointViews;
    private MapPointWithTitleView start;
    private MapPointWithTitleView endLocation;
    private ArrayList<MapPointWithTitleView> pathMapPointView = new ArrayList<MapPointWithTitleView>();
    private WeakReference contextWeakRef;
    private LoginUserInfo loginUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        getToolbarRight().setText(R.string.map_title_right);
//        getToolbarRight().setText(R.string.map_title_right);
        mapView = (MapView) findViewById(R.id.mapView);

        ivRecommend.setOnTouchListener(new ImageViewTouchLisenter(tv_recommed_title));

        loginUser = App.getApplication().loginUser;

        InputStream open = null;
        try {
            open = getResources().getAssets().open("olympic_bg.png");
            bitmap = BitmapFactory.decodeStream(open);
            toolBar = findViewById(R.id.toolbar_layout);

//            toolBar.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
        } catch (IOException e) {
            e.printStackTrace();
        }
        contextWeakRef = new WeakReference(OlympicMapAcyT.this);

        Drawable drawableShop = getResources().getDrawable(R.mipmap.shop_icon);
        drawableShop.setBounds(0,0, (int) getResources().getDimension(R.dimen.x25),(int) getResources().getDimension(R.dimen.x25));
        tvShop.setCompoundDrawables(null,drawableShop,null,null);

        Drawable drawableCT = getResources().getDrawable(R.mipmap.canting_icon);
        drawableCT.setBounds(0,0, (int) getResources().getDimension(R.dimen.x25),(int) getResources().getDimension(R.dimen.x25));
        tvCanting.setCompoundDrawables(null,drawableCT,null,null);

        Drawable drawableVenue = getResources().getDrawable(R.mipmap.zg_icon);
        drawableVenue.setBounds(0,0, (int) getResources().getDimension(R.dimen.x25),(int) getResources().getDimension(R.dimen.x25));
        tvZhanguangIcon.setCompoundDrawables(null,drawableVenue,null,null);

        Drawable drawablePark = getResources().getDrawable(R.mipmap.park_icon);
        drawablePark.setBounds(0,0, (int) getResources().getDimension(R.dimen.x25),(int) getResources().getDimension(R.dimen.x25));
        tvPack.setCompoundDrawables(null,drawablePark,null,null);

        Drawable drawableBus = getResources().getDrawable(R.mipmap.bus_icon);
        drawableBus.setBounds(0,0, (int) getResources().getDimension(R.dimen.x25),(int) getResources().getDimension(R.dimen.x25));
        tvBus.setCompoundDrawables(null,drawableBus,null,null);

        Drawable drawableWC = getResources().getDrawable(R.mipmap.wc_icon);
        drawableWC.setBounds(0,0, (int) getResources().getDimension(R.dimen.x25),(int) getResources().getDimension(R.dimen.x25));
        tvWc.setCompoundDrawables(null,drawableWC,null,null);


        initMapData();
        //进来app 就显示 展馆数据
        ivZhanguangBg.performClick();
//        initJsonData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mapView.setMapState(bitmap, bitmap.getWidth(), bitmap.getHeight(), toolBar.getMeasuredHeight() + llBottomLayout.getMeasuredHeight());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.moveMapView(mapView.tranX, mapView.tranY);
                LogUtils.e("titleHeight-" + toolBar.getHeight() + "--" + toolBar.getMeasuredHeight());
            }
        }, 30);
    }


    /**
     * TODO 根据数据生成 图标view 添加到mapView 中
     */
    private void initMapData() {
        String mapdataStr = LocalJsonResolutionUtils.getJson(this, "mapdata");
        MapBean mapdata = new Gson().fromJson(mapdataStr, MapBean.class);
//        LogUtils.e("------"+mapdata.toString());

        myLocationLable = mapdata.getMyLocation();

        // 自己的位置
        this.myLocation = new MapPointWithTitleView(contextWeakRef, myLocationLable.getPos_x(), myLocationLable.getPos_y(), MapPointWithTitleView.RED_POINT, true, "");
        this.myLocation.setPointIcon(0, R.mipmap.zg_icon);
        this.myLocation.showPointIconBG();
        mapView.addMapPoint(this.myLocation);

        venueLableData1 = mapdata.getVenueLableData();
        busLableData = mapdata.getBusLableData();
        parkLableData = mapdata.getParkLableData();
        restanruntLableData = mapdata.getRestanruntLableData();
        shopLableData = mapdata.getShopLableData();
        washroomLableData = mapdata.getWashroomLableData();

        //1 添加 展馆的 标注
        venueMapPointViews = new ArrayList<>();
        for (int i = 0; i < venueLableData1.size(); i++) {
            final LableBean lableBean = venueLableData1.get(i);


            final MapPointWithTitleView lableMapView = new MapPointWithTitleView(contextWeakRef, lableBean.getPos_x(), lableBean.getPos_y(), MapPointWithTitleView.RED_POINT, true, "");
            lableMapView.setLableBean(lableBean);
            lableMapView.setPointIcon(R.mipmap.zg_lable, R.mipmap.zg_icon);

            lableMapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lableMapView.showPopWin(contextWeakRef, lableBean);
                    mapView.moveLableToCenter(lableBean.getPos_x(), lableBean.getPos_y());
                }
            });

            venueMapPointViews.add(lableMapView);

            mapView.addMapPoint(lableMapView);
            lableMapView.setVisibility(View.GONE);
        }

        //2 添加 bus 的 标注
        busMapPointViews = new ArrayList<>();
        for (int i = 0; i < busLableData.size(); i++) {
            final LableBean lableBean = busLableData.get(i);
            final MapPointWithTitleView lableMapView = new MapPointWithTitleView(contextWeakRef, lableBean.getPos_x(), lableBean.getPos_y(), MapPointWithTitleView.RED_POINT, true, "");
            lableMapView.setPointIcon(R.mipmap.bus_lable, R.mipmap.bus_icon);

            lableMapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lableMapView.showPopWin(contextWeakRef, lableBean);
                    mapView.moveLableToCenter(lableBean.getPos_x(), lableBean.getPos_y());
                }
            });


            busMapPointViews.add(lableMapView);

            mapView.addMapPoint(lableMapView);
            lableMapView.setVisibility(View.GONE);
        }
        //--------------

        //3 添加 park 的 标注
        parkMapPointViews = new ArrayList<>();
        for (int i = 0; i < parkLableData.size(); i++) {
            final LableBean lableBean = parkLableData.get(i);
            final MapPointWithTitleView lableMapView = new MapPointWithTitleView(contextWeakRef, lableBean.getPos_x(), lableBean.getPos_y(), MapPointWithTitleView.RED_POINT, false, "");
            lableMapView.setPointIcon(R.mipmap.park_lable, R.mipmap.park_icon);
            Random random = new Random();
            lableMapView.setParkCount(random.nextInt(200) + "/200");
            lableMapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lableMapView.showPopWin(contextWeakRef, lableBean);
                    mapView.moveLableToCenter(lableBean.getPos_x(), lableBean.getPos_y());
                }
            });

            parkMapPointViews.add(lableMapView);

            mapView.addMapPoint(lableMapView);
            lableMapView.setVisibility(View.GONE);
        }
        //--------------

        //4 添加 餐厅 的 标注
        ctMapPointViews = new ArrayList<MapPointWithTitleView>();
        for (int i = 0; i < restanruntLableData.size(); i++) {
            final LableBean lableBean = restanruntLableData.get(i);
            final MapPointWithTitleView lableMapView = new MapPointWithTitleView(contextWeakRef, lableBean.getPos_x(), lableBean.getPos_y(), MapPointWithTitleView.RED_POINT, true, "");
            lableMapView.setPointIcon(R.mipmap.canting_lable, R.mipmap.canting_icon);

            lableMapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lableMapView.showPopWin(contextWeakRef, lableBean);
                    mapView.moveLableToCenter(lableBean.getPos_x(), lableBean.getPos_y());
                }
            });

            ctMapPointViews.add(lableMapView);

            mapView.addMapPoint(lableMapView);
            lableMapView.setVisibility(View.GONE);
        }
        //--------------

        //5 添加 shop 的 标注
        shopMapPointViews = new ArrayList<MapPointWithTitleView>();
        for (int i = 0; i < shopLableData.size(); i++) {
            final LableBean lableBean = shopLableData.get(i);
            final MapPointWithTitleView lableMapView = new MapPointWithTitleView(contextWeakRef, lableBean.getPos_x(), lableBean.getPos_y(), MapPointWithTitleView.RED_POINT, true, "");
            lableMapView.setPointIcon(R.mipmap.shop_lable, R.mipmap.shop_icon);

            lableMapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lableMapView.showPopWin(contextWeakRef, lableBean);
                    mapView.moveLableToCenter(lableBean.getPos_x(), lableBean.getPos_y());
                }
            });


            shopMapPointViews.add(lableMapView);

            mapView.addMapPoint(lableMapView);
            lableMapView.setVisibility(View.GONE);
        }
        //-------------- 


        //6 添加 WC 的 标注
        wcMapPointViews = new ArrayList<MapPointWithTitleView>();
        for (int i = 0; i < washroomLableData.size(); i++) {
            final LableBean lableBean = washroomLableData.get(i);
            final MapPointWithTitleView lableMapView = new MapPointWithTitleView(contextWeakRef, lableBean.getPos_x(), lableBean.getPos_y(), MapPointWithTitleView.RED_POINT, true, "");
            lableMapView.setPointIcon(R.mipmap.wc_icon, R.mipmap.wc_icon);

            lableMapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lableMapView.showPopWin(contextWeakRef, lableBean);
                    mapView.moveLableToCenter(lableBean.getPos_x(), lableBean.getPos_y());
                }
            });


            wcMapPointViews.add(lableMapView);

            mapView.addMapPoint(lableMapView);
            lableMapView.setVisibility(View.GONE);
        }
        //--------------

//        fishLable = new MapPointWithTitleView(this, 150, 950, MapPointWithTitleView.RED_POINT, true, "");
//        fishLable.setPointIcon(R.mipmap.zg_lable);
//
//        fishLable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fishLable.showPopWin(OlympicMapAcyT.this, 1,"滑冰展馆");
//                mapView.moveLableToCenter(150, 950);
//            }
//        });
//        mapView.addMapPoint(fishLable);
//        fishLable.setVisibility(View.GONE);
//
//        qiuLable = new MapPointWithTitleView(this, 550, 730, MapPointWithTitleView.RED_POINT, true, "");
//        qiuLable.setPointIcon(R.mipmap.bus_lable);
//        qiuLable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //衣服店
//                //rl_recommend.setBackgroundResource(R.mipmap.bing_hu_bs);
//                mapView.moveLableToCenter(550, 330);
//            }
//        });
//        mapView.addMapPoint(qiuLable);
//        qiuLable.setVisibility(View.GONE);

        //TODO 模拟数据 for循环 生成view
//        zg_lables.add(fishLable);
//        zg_lables.add(qiuLable);
    }

    int i = 0;

    @Override
    public void mapViewOnLongClick(float downX, float downY) {
        MapPointWithTitleView myMapPointWithTitleView =
                new MapPointWithTitleView(contextWeakRef, downX, downY, MapPointWithTitleView.RED_POINT, true, "" + (i++));
        mapView.addMapPoint(myMapPointWithTitleView);
    }

    float x = 400;
    float y = 400;

    public void addLine(View view) {
//        x += 50;
//        y -= 50;
//        mapView.addMapPoint(new MapPointWithTitleView(this,x+mapView.getTranX(),y+mapView.getTranY(),MapPointWithTitleView.RED_POINT,true,"" + (i++)));
    }

    public void addOtherLine(View view) {
//        addLinePath(lableType, 0);
    }

    /**
     * 第几个标注点 显示这个点的路径Path
     *
     * @param lableType
     * @param
     */
    private void addLinePath(int lableType, LableBean lableBean) {
        //TODO 标注点的坐标 显示 不同的top and Text
        ivRecommend.setVisibility(View.VISIBLE);
        tv_recommed_title.setVisibility(View.VISIBLE);
        tv_recommed_title.setText(lableBean.getDescription());

        if (pathMapPointView.size() > 0) {
            releasePath(pathMapPointView);
        }

        //线的路径
        pathMapPointView = new ArrayList<>();

        //起始点我的位置
        start = new MapPointWithTitleView(contextWeakRef, myLocationLable.getPos_x(), myLocationLable.getPos_y(), MapPointWithTitleView.BLUE_POINT, true, "");
        pathMapPointView.add(start);

        ArrayList<Point> path = lableBean.getPath();
        for (Point point : path) {
            pathMapPointView.add(new MapPointWithTitleView(contextWeakRef, point.x, point.y, MapPointWithTitleView.BLACK_POINT, true, "" + (i++)));
        }
        //终点
        endLocation = new MapPointWithTitleView(contextWeakRef, lableBean.getPos_x(), lableBean.getPos_y(), MapPointWithTitleView.RED_POINT, true, "");

        pathMapPointView.add(endLocation);
        mapView.addMapLines(pathMapPointView, lableBean.getGoTime());
    }

    String lableInfo = null;
    @OnClick({R.id.iv_cesuo_bg, R.id.iv_canting_bg, R.id.iv_zhanguang_bg, R.id.iv_shop_bg, R.id.iv_bus_bg, R.id.iv_recommend, R.id.iv_park_bg})
    public void onViewClicked(View view) {
        ArrayList<View> views;

        switch (view.getId()) {
            case R.id.iv_cesuo_bg:
                //wc
//                ivRecommend.setImageResource(R.mipmap.canting);
                if (select != WC) {
                    ivCesuoBg.setBackgroundResource(R.color.bottom_icon_click);
                    showZGIconAnim(wcMapPointViews);
                    // 显示 默认显示的标注的信息
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            wcMapPointViews.get(0).performClick();
                        }
                    }, 100);
                    wcMapPointViews.get(0).performClick();
                    dismissBottom(select);
                    select = WC;
                    dismissOtherIcon(select);
                }
                break;
            case R.id.iv_canting_bg:
                ivRecommend.setImageResource(R.mipmap.canting);
                if (select != CAN_TING) {
                    ivCantingBg.setBackgroundResource(R.color.bottom_icon_click);
                    showZGIconAnim(ctMapPointViews);
                    // 显示 默认显示的标注的信息
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //
                            MapPointWithTitleView mapPointWithTitleView = null;
                            if(loginUser != null) {
                                switch (loginUser.getNationality()){
                                    case "5":
                                        mapPointWithTitleView = ctMapPointViews.get(0);
                                        lableInfo = "中国餐厅";
                                    break;
                                    default:
                                        mapPointWithTitleView = ctMapPointViews.get(0);
                                        lableInfo = "France Restanrunt";
                                        break;
                                }
                            }else{
                                mapPointWithTitleView = ctMapPointViews.get(0);
                                lableInfo = "英国餐厅";
                            }
                            mapPointWithTitleView.performClick();
                            mapPointWithTitleView.setTv_labe_info(lableInfo);

                        }
                    }, 100);
                    dismissBottom(select);
                    select = CAN_TING;
                    dismissOtherIcon(select);
                }
                break;
            case R.id.iv_zhanguang_bg:
                // 展馆点击
                ivRecommend.setImageResource(R.mipmap.vunue);
                if (select != ZHAN_GUANG) {
                    ivZhanguangBg.setBackgroundResource(R.color.bottom_icon_click);
                    showZGIconAnim(venueMapPointViews);
                    // 显示 默认显示的标注的信息
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MapPointWithTitleView mapPointWithTitleView = null;
                            if(loginUser != null) {
                                switch (loginUser.getFavoriteSports()) {
                                    case "01":
                                        mapPointWithTitleView = venueMapPointViews.get(1);
                                        lableInfo = "ice Hockey";
                                        break;
                                    case "02":
                                        mapPointWithTitleView = venueMapPointViews.get(0);
                                        lableInfo = "Curling";
                                        break;
                                    case "03":
                                        mapPointWithTitleView = venueMapPointViews.get(1);
                                        lableInfo = "Skiing";
                                        break;
                                    case "04":
                                        mapPointWithTitleView = venueMapPointViews.get(0);
                                        lableInfo = "Biathlon";
                                        break;
                                    case "05":
                                        mapPointWithTitleView = venueMapPointViews.get(1);
                                        lableInfo = "Skating";
                                        break;
                                    case "06":
                                        mapPointWithTitleView = venueMapPointViews.get(1);
                                        lableInfo = "Sled";
                                        break;
                                    case "07":
                                        mapPointWithTitleView = venueMapPointViews.get(0);
                                        lableInfo = "Bobsleigh";
                                        break;
                                }
                            }else{
                                mapPointWithTitleView = venueMapPointViews.get(0);
                                lableInfo = "Bobsleigh";
                            }
                            mapPointWithTitleView.performClick();
                            mapPointWithTitleView.setTv_labe_info(lableInfo);
                        }
                    }, 150);
                    dismissBottom(select);
                    select = ZHAN_GUANG;
                    dismissOtherIcon(select);
                }
                break;
            case R.id.iv_shop_bg:
                //商店
                ivRecommend.setImageResource(R.mipmap.bianli_d_);
                if (select != BIAN_LI_DIAN) {
                    ivShopBg.setBackgroundResource(R.color.bottom_icon_click);
                    showZGIconAnim(shopMapPointViews);
                    // 显示 默认显示的标注的信息
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            shopMapPointViews.get(0).performClick();
                        }
                    }, 100);
                    dismissBottom(select);
                    select = BIAN_LI_DIAN;
                    dismissOtherIcon(select);

                }
                break;
            case R.id.iv_bus_bg:
                //公交车站
                ivRecommend.setImageResource(R.mipmap.bus_top);
                if (select != BUS) {
                    ivBusBg.setBackgroundResource(R.color.bottom_icon_click);
                    showZGIconAnim(busMapPointViews);
                    // 显示 默认显示的标注的信息
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            busMapPointViews.get(0).performClick();
                        }
                    }, 100);
                    dismissBottom(select);
                    select = BUS;
                    dismissOtherIcon(select);

                }
                break;
            case R.id.iv_park_bg:
                ivRecommend.setImageResource(R.mipmap.park);

                if (select != PARKING) {
                    ivParkBg.setBackgroundResource(R.color.bottom_icon_click);
                    showZGIconAnim(parkMapPointViews);
                    // 显示 默认显示的标注的信息
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            parkMapPointViews.get(0).performClick();
                        }
                    }, 100);
                    dismissBottom(select);
                    select = PARKING;
                    dismissOtherIcon(select);
                }
                break;
            case R.id.iv_fish:
                break;
            case R.id.iv_clothes:
                break;
            case R.id.iv_recommend:
//                if (ivRecommend.isShown()) {
//                    ivRecommend.setVisibility(View.GONE);
//                    tv_recommed_title.setVisibility(View.GONE);
//                } else {
//                    tv_recommed_title.setVisibility(View.VISIBLE);
//                    ivRecommend.setVisibility(View.VISIBLE);
//                }
                break;
        }
    }

    /**
     * 隐藏其他图标
     *
     * @param select
     */
    private void dismissOtherIcon(int select) {
        switch (select) {
            case ZHAN_GUANG:
                mapView.clearAllMapLines();
                dismissBusIcon();
                dismissCTIcon();
                dismissParkIcon();
                dismissShopIcon();
                dismissWcIcon();
//                ivZhanguangBg.setBackgroundResource(R.color.transparent);
                break;
            case BUS:
                mapView.clearAllMapLines();
                dismissCTIcon();
                dismissParkIcon();
                dismissShopIcon();
                dismissWcIcon();
                dismissZGIcon();
//                ivBusBg.setBackgroundResource(R.color.transparent);
                break;
            case BIAN_LI_DIAN:
                //shop
                mapView.clearAllMapLines();
                dismissBusIcon();
                dismissCTIcon();
                dismissParkIcon();

                dismissWcIcon();
                dismissZGIcon();
//                ivShopBg.setBackgroundResource(R.color.transparent);
                break;

            case PARKING:
                //停车场
                mapView.clearAllMapLines();
                dismissBusIcon();
                dismissCTIcon();
                dismissShopIcon();
                dismissWcIcon();
                dismissZGIcon();
//                ivParkBg.setBackgroundResource(R.color.transparent);
                break;
            case WC:
                //厕所
                mapView.clearAllMapLines();
                dismissBusIcon();
                dismissCTIcon();
                dismissParkIcon();
                dismissShopIcon();
                dismissZGIcon();
//                ivCesuoBg.setBackgroundResource(R.color.transparent);
                break;
            case CAN_TING:
                //餐厅
                mapView.clearAllMapLines();
                dismissBusIcon();
                dismissParkIcon();
                dismissShopIcon();
                dismissWcIcon();
                dismissZGIcon();
//                ivCantingBg.setBackgroundResource(R.color.transparent);
                break;
        }
    }

    /**
     * bottom 点击图标
     *
     * @param select
     */
    private void dismissBottom(int select) {
        switch (select) {
            case ZHAN_GUANG:
                ivZhanguangBg.setBackgroundResource(R.color.transparent);
                break;
            case BUS:
                ivBusBg.setBackgroundResource(R.color.transparent);
                break;
            case BIAN_LI_DIAN:
                //shop
                ivShopBg.setBackgroundResource(R.color.transparent);
                break;

            case PARKING:
                //停车场
                ivParkBg.setBackgroundResource(R.color.transparent);
                break;
            case WC:
                //厕所
                ivCesuoBg.setBackgroundResource(R.color.transparent);
                break;
            case CAN_TING:
                //餐厅
                ivCantingBg.setBackgroundResource(R.color.transparent);
                break;
        }
    }

    /**
     * 展馆 图标群 一起跳出弹性动画
     */
    private void showZGIconAnim(final ArrayList<MapPointWithTitleView> views) {

        ValueAnimator mAnimator = ValueAnimator.ofFloat(0.2f, 1f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                float animatorValue = (float) animation.getAnimatedValue();
                for (View view : views) {
                    view.setScaleX(animatorValue);
                    view.setScaleY(animatorValue);
                }
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mapView.moveMapView(0.1f, 0);
                for (View view : views) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //view 第一次点击不显示弹出的动画
                if (firstClick) {
                    firstClick = false;
                    showZGIconAnim(views);
                }
            }
        });

        //4.设置动画的持续时间、是否重复及重复次数等属性
        if (firstClick) {
            mAnimator.setDuration(50);
        }
        mAnimator.setInterpolator(new DumpingInterpolator());
        mAnimator.start();
    }

    private void dismissZGIcon() {
        for (View zg_lable : venueMapPointViews) {
            zg_lable.setVisibility(View.GONE);
        }
    }

    private void dismissWcIcon() {
        for (View zg_lable : wcMapPointViews) {
            zg_lable.setVisibility(View.GONE);
        }
    }

    private void dismissShopIcon() {
        for (View zg_lable : shopMapPointViews) {
            zg_lable.setVisibility(View.GONE);
        }
    }

    private void dismissBusIcon() {
        for (View zg_lable : busMapPointViews) {
            zg_lable.setVisibility(View.GONE);
        }
    }

    private void dismissCTIcon() {
        for (View zg_lable : ctMapPointViews) {
            zg_lable.setVisibility(View.GONE);
        }
    }

    private void dismissParkIcon() {
        for (View zg_lable : parkMapPointViews) {
            zg_lable.setVisibility(View.GONE);
        }
    }

    /**
     * 生成虚拟数据
     */
    private void initJsonData() {

        MapBean mapBeanData = new MapBean();
        mapBeanData.setDescription("展馆地图");
        mapBeanData.setMapType(1);
        mapBeanData.setImage_url("");
        mapBeanData.setName("zg");

        //---------1展馆
        ArrayList<LableBean> lableBeans = new ArrayList<>();

        //----------------------
        LableBean lableBean = new LableBean();

        lableBean.setId(1);
        lableBean.setName("国家体育馆");
        lableBean.setDescription("即将开始男子组半决赛有少量余票免费赠送，可前往服务台领取");
        lableBean.setIcon("冰球");
        lableBean.setImage_url("hot_rock");
        lableBean.setLableType(1);
        lableBean.setPos_x(150);
        lableBean.setPos_y(950);
        lableBean.setGoTime(11);

        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(355, 1185));
        points.add(new Point(301, 1000));

//        points.add(new Point(350, 850));
//        points.add(new Point(150, 550));

        lableBean.setPath(points);
        lableBeans.add(lableBean);

//        、、---------
        LableBean lableBean2 = new LableBean();

        lableBean2.setId(2);
        lableBean2.setName("水立方");
        lableBean2.setDescription("即将开始女子组半决赛有少量余票免费赠送，可前往服务台领取");
        lableBean2.setIcon("fish");
        lableBean2.setImage_url("hot_rock");
        lableBean2.setLableType(1);
        lableBean2.setPos_x(807);
        lableBean2.setPos_y(876);
        lableBean2.setGoTime(11);

        ArrayList<Point> points2 = new ArrayList<>();

        points2.add(new Point(705, 1161));
//        points2.add(new Point(303, 1056));

//        points.add(new Point(350, 850));
//        points.add(new Point(150, 550));

        lableBean2.setPath(points2);

        lableBeans.add(lableBean2);

        mapBeanData.setVenueLableData(lableBeans);

        //-------------end-----------

        //---------2shop
        ArrayList<LableBean> shopLable = new ArrayList<>();

        LableBean lableBeanshop = new LableBean();

        lableBeanshop.setId(2);
        lableBeanshop.setName("奥运商店");
        lableBeanshop.setDescription("奥运商店");
        lableBeanshop.setIcon("shop");
        lableBeanshop.setImage_url("hot_rock");
        lableBeanshop.setLableType(2);
        lableBeanshop.setPos_x(220);
        lableBeanshop.setPos_y(1473);
        lableBeanshop.setGoTime(20);

        ArrayList<Point> pointsShop = new ArrayList<>();

        pointsShop.add(new Point(350, 1310));
        pointsShop.add(new Point(224, 1303));
//        pointsShop.add(new Point(150, 550));

        lableBeanshop.setPath(pointsShop);
        shopLable.add(lableBeanshop);

        mapBeanData.setShopLableData(shopLable);

        //----------end--------------

        //---------3CT
        ArrayList<LableBean> CTLable = new ArrayList<>();

        LableBean lableBeanCT = new LableBean();

        lableBeanCT.setId(3);
        lableBeanCT.setName("法国餐厅");
        lableBeanCT.setDescription("鹅肝蜗牛/龙虾饭/可丽饼/铁板扇贝");
        lableBeanCT.setIcon("shop");
        lableBeanCT.setImage_url("hot_rock");
        lableBeanCT.setLableType(6);
        lableBeanCT.setPos_x(829);
        lableBeanCT.setPos_y(1126);
        lableBeanCT.setGoTime(32);

        ArrayList<Point> pointsCT = new ArrayList<>();


        pointsCT.add(new Point(550, 1235));
//        pointsCT.add(new Point(350, 850));
//        pointsCT.add(new Point(150, 550));

        lableBeanCT.setPath(pointsCT);
        CTLable.add(lableBeanCT);

        mapBeanData.setRestanruntLableData(CTLable);

        //----------end--------------

        //---------4Bus
        ArrayList<LableBean> BusLable = new ArrayList<>();

        LableBean lableBeanBus = new LableBean();

        lableBeanBus.setId(4);
        lableBeanBus.setName("公共BUS");
        lableBeanBus.setDescription("公共BUS");
        lableBeanBus.setIcon("Bus");
        lableBeanBus.setImage_url("bus_top");
        lableBeanBus.setLableType(4);
        lableBeanBus.setPos_x(591);
        lableBeanBus.setPos_y(1575);
        ;
        lableBeanBus.setGoTime(23);

        ArrayList<Point> pointsBus = new ArrayList<>();

        pointsCT.add(new Point(550, 1235));
//        pointsCT.add(new Point(350, 850));
//        pointsCT.add(new Point(150, 550));


        lableBeanBus.setPath(pointsBus);
        BusLable.add(lableBeanBus);

        mapBeanData.setBusLableData(BusLable);

        //----------end--------------

        //---------5Park
        ArrayList<LableBean> ParkLable = new ArrayList<>();

        LableBean lableBeanPark = new LableBean();

        lableBeanPark.setId(4);
        lableBeanPark.setName("Oly Park");
        lableBeanPark.setDescription("Oly Park");
        lableBeanPark.setIcon("Park");
        lableBeanPark.setImage_url("park");
        lableBeanPark.setLableType(3);
        lableBeanPark.setPos_x(900);
        lableBeanPark.setPos_y(1460);
        lableBeanPark.setGoTime(25);
        ArrayList<Point> pointsPark = new ArrayList<>();

        pointsPark.add(new Point(778, 1276));
//        pointsPark.add(new Point(150, 750));
//        pointsPark.add(new Point(150, 550));

        lableBeanPark.setPath(pointsPark);
        ParkLable.add(lableBeanPark);

        mapBeanData.setParkLableData(ParkLable);

        //----------end--------------

        //---------6 wc
        ArrayList<LableBean> WcLable = new ArrayList<>();

        LableBean lableBeanWC = new LableBean();

        lableBeanWC.setId(4);
        lableBeanWC.setName("Washroom");
        lableBeanWC.setDescription("Washroom");
        lableBeanWC.setIcon("WC");
        lableBeanWC.setImage_url("null");
        lableBeanWC.setLableType(5);
        lableBeanWC.setPos_x(400);
        lableBeanWC.setPos_y(1452);
        lableBeanWC.setGoTime(27);
        ArrayList<Point> pointsWc = new ArrayList<>();

        pointsWc.add(new Point(357, 1239));
        pointsWc.add(new Point(357, 1315));
//        pointsWc.add(new Point(350, 850));


        lableBeanWC.setPath(pointsWc);
        WcLable.add(lableBeanWC);

        mapBeanData.setWashroomLableData(WcLable);

        //----------end--------------


        //-----------我的位置
        LableBean lb = new LableBean();

        lb.setId(2);
        lb.setDescription("myLocation");
        lb.setIcon("my_location_icon");
        lb.setImage_url(null);
        lb.setLableType(888);
        lb.setPos_x(476);
        lb.setPos_y(1222);
        //--------------------

        mapBeanData.setMyLocation(lb);

        String s = new Gson().toJson(mapBeanData);
        LogUtils.e(s);
    }

    public void showTopAdvertImageAndPath(LableBean lableBean) {
        addLinePath(lableBean.getLableType(), lableBean);

        switch (lableBean.getLableType()) {
            case 1:
                //展馆
                for (MapPointWithTitleView venueMapPointView : venueMapPointViews) {
                    if (lableBean.getId() != venueMapPointView.getLableBean().getId()) {
                        venueMapPointView.rl_pop_layout.setVisibility(View.INVISIBLE);
                        venueMapPointView.map_point_linear.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        //
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        ClearLeakUtils.fixInputMethodManagerLeak(this);
        for (MapPointWithTitleView venueMapPointView : venueMapPointViews) {
            venueMapPointView.releaseContext();
        }
        for (MapPointWithTitleView wcMapPointView : wcMapPointViews) {
            wcMapPointView.releaseContext();
        }
        for (MapPointWithTitleView parkMapPointView : parkMapPointViews) {
            parkMapPointView.releaseContext();
        }
        for (MapPointWithTitleView busMapPointView : busMapPointViews) {
            busMapPointView.releaseContext();
        }

        for (MapPointWithTitleView shopMapPointView : shopMapPointViews) {
            shopMapPointView.releaseContext();
        }

        for (MapPointWithTitleView ctMapPointView : ctMapPointViews) {
            ctMapPointView.releaseContext();
        }
        mHandler.removeCallbacksAndMessages(null);

        start.releaseContext();
        endLocation.releaseContext();

        releasePath(pathMapPointView);

        mapView.releaseView();
        ClearLeakUtils.fixInputMethodManagerLeak(this);

//        Toast mToast = new Toast(App.getApplication().getBaseContext());
//        try {
//            Field field = mToast.getClass().getDeclaredField("mTN");
//            field.setAccessible(true);//
//            Method cancel = field.getClass().getDeclaredMethod("cancel", null);
//            cancel.invoke(field.getClass(),null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        System.gc();
        super.onDestroy();
    }

    private void releasePath(ArrayList<MapPointWithTitleView> pathMapPointViews) {
        for (MapPointWithTitleView mapPointView : pathMapPointViews) {
            mapPointView.releaseContext();
        }
        pathMapPointViews.clear();
    }


}

