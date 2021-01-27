package com.example.recover;

import com.example.baselibs.*;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

public class MyMarker {

    private LatLng ll;

    public MyMarker(LatLng ll) {
        super();
        this.ll = ll;
    }

    public void addMaker(BaiduMap baiduMap){
        BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.marker);
        OverlayOptions options=new MarkerOptions()
                .perspective(true)
                .position(ll)
                .icon(bitmap);
        baiduMap.addOverlay(options);
    }

    public LatLng getLl() {
        return ll;
    }
}
