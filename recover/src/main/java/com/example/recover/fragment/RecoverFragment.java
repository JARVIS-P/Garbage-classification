package com.example.recover.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.example.recover.MyMarker;
import com.example.recover.MyScroller;
import com.example.recover.ProgressDialogUtil;
import com.example.recover.R;
import com.example.recover.activity.AddActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import mapapi.overlayutil.WalkingRouteOverlay;

@Route(path = "/recover/fragment")
public class RecoverFragment extends Fragment {

    private LocationClient client;

    private TextureMapView mapView;

    private BaiduMap baiduMap;

    private Boolean isFirstLocate=true;

    private RoutePlanSearch mSearch;

    private LatLng myLatLng;

    private LatLng startLatLng;

    private LatLng aimsLatLng;

    private TextView promptText;

    private ImageView upImage;

    private ImageView aimsImage;

    private Boolean isRoute=false;

    private MyScroller myScroller;

    private boolean isGPS=false;

    private boolean isShow=false;

    private int number=0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recover_fragment_recover, container,false);
        client=new LocationClient(Objects.requireNonNull(getActivity()).getApplicationContext());
        client.registerLocationListener(new MyListener());

        ImageView addImage=view.findViewById(R.id.add_image);
        ImageView routeImage=view.findViewById(R.id.route_image);
        ImageView refreshImage=view.findViewById(R.id.refresh_image);
        ImageView ashcanImage=view.findViewById(R.id.ashcan_image);
        ImageView openImage=view.findViewById(R.id.open_image);

        myScroller=view.findViewById(R.id.my_scroller);
        mapView=view.findViewById(R.id.map_view);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        mSearch = RoutePlanSearch.newInstance();

        promptText=view.findViewById(R.id.prompt_text);
        aimsImage=view.findViewById(R.id.aims_image);
        upImage=view.findViewById(R.id.up_image);

        baiduMap.setMyLocationEnabled(true);

        MyLocationConfiguration myLocationConfiguration=new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,null);

        baiduMap.setMyLocationConfiguration(myLocationConfiguration);

        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }

        if(!permissionList.isEmpty()){
            String[] permission=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(),permission,1);
        }

        requestLocation();

        openImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myScroller.open();
            }
        });//点击打开折叠栏按钮

        OnGetRoutePlanResultListener listener=new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                WalkingRouteOverlay overlay=new WalkingRouteOverlay(baiduMap);
                if(walkingRouteResult.getRouteLines().size()>0){
                    overlay.setData(walkingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                }
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };//初始化路径规划功能

        mSearch.setOnGetRoutePlanResultListener(listener);

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                routePlan(myLatLng,marker.getPosition());
                return true;
            }
        });//Marker点击事件

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AddActivity.class);
                intent.putExtra("mLatLng",myLatLng);
                startActivity(intent);
            }
        });//addImage点击事件

        ashcanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMarker();
                Toast.makeText(getContext(),"已在地图中添加附近回收点，点击标记点即可开始路线规划",Toast.LENGTH_SHORT).show();
            }
        });//ashcanImage点击事件

        routeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRoute){
                    baiduMap.clear();
                    isRoute=false;
                    upImage.setVisibility(View.INVISIBLE);
                    promptText.setVisibility(View.VISIBLE);
                    aimsImage.setVisibility(View.INVISIBLE);
                    initMarker();
                    Toast.makeText(getContext(),"已取消当前路径规划",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"请先选择目标回收点",Toast.LENGTH_SHORT).show();
                }
            }
        });//routeImage点击事件

        refreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRoute){
                    routePlan(myLatLng,aimsLatLng);
                }else {
                    Toast.makeText(getContext(),"请点击您的目标回收点",Toast.LENGTH_SHORT).show();
                }
            }
        });//refreshImage点击事件

        return view;
    }

    private void routePlan(LatLng mLatLng,LatLng aLatLng){
        PlanNode stNode=PlanNode.withLocation(mLatLng);
        PlanNode enNode=PlanNode.withLocation(aLatLng);

        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode));

        isRoute=true;
        aimsLatLng=aLatLng;

        baiduMap.clear();

        MyMarker myMarker=new MyMarker(aLatLng);
        myMarker.addMaker(baiduMap);

        upImage.setVisibility(View.VISIBLE);
        promptText.setVisibility(View.INVISIBLE);
        aimsImage.setVisibility(View.VISIBLE);
        aimsImage.setImageResource(R.drawable.aims);
    }//路径规划

    private void requestLocation(){
        initLocation();
        client.start();
    }//开始定位

    private class MyListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            myLatLng=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation){
                isGPS=true;
            }else {
                isGPS=false;
            }
            if(bdLocation.getLocType()==BDLocation.TypeNetWorkLocation||bdLocation.getLocType()==BDLocation.TypeGpsLocation){
                navigateTo(bdLocation);
            }

            /*MyLocationData locData=new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(bdLocation.getDirection())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();

            baiduMap.setMyLocationData(locData);*/

            if(!isGPS&&number==1){
                Toast.makeText(getContext(),"当前GPS信号较差,可能会影响到定位及规划结果",Toast.LENGTH_SHORT).show();
                number++;
            }

            ProgressDialogUtil.dismiss();
        }
    }//自定义BDLocationListener

    private void navigateTo(BDLocation location){
        LatLng temp=new LatLng(location.getLatitude(),location.getLongitude());
        myLatLng=temp;

        if(isFirstLocate){
            LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
            if(ll==null){
                Toast.makeText(getContext(),"请打开GPS",Toast.LENGTH_SHORT).show();
            }
            MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update=MapStatusUpdateFactory.zoomTo(19f);
            baiduMap.animateMapStatus(update);
            isFirstLocate=false;
            startLatLng=myLatLng;
        }

        MyLocationData locData=new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();

        baiduMap.setMyLocationData(locData);
    }//地图显示的初始化设置

    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setNeedDeviceDirect(true);
        client.setLocOption(option);
    }//定位时间间隔

    private void initMarker(){
        LatLng ll=new LatLng(startLatLng.latitude+0.05, startLatLng.longitude+0.02);
        new MyMarker(ll).addMaker(baiduMap);
        ll=new LatLng(startLatLng.latitude-0.05, startLatLng.longitude+0.02);
        MyMarker myMarker=new MyMarker(ll);
        myMarker.addMaker(baiduMap);

    }//初始化所有Marker

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(),"未获得某些权限，有些功能可能无法正常使用!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } else {
                Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
            }
        }
    }//权限结果处理

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            isShow=true;
        }else {
            isShow=false;
        }
        if(!hidden&&number==0){
            number++;
            ProgressDialogUtil.showProgressDialog(getContext());
        }
    }//当该碎片显示到台前时调用的方法

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSearch.destroy();
        mapView.onDestroy();
    }
}