package com.xiaobao.good.sign.location;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.xiaobao.good.common.DateUtil;
import com.xiaobao.good.common.eventbus.LocationInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

/*****
 *
 * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
 *
 */
public class XBdLocationListener extends BDAbstractLocationListener {
    public static final String TAG = "X_BdLocationListener";
    private LocationClientOption mOption = null;


    @Override
    public void onReceiveLocation(BDLocation location) {
        if (null != location && location.getLocType() != BDLocation.TypeServerError) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            /**
             * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
             * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
             */
            sb.append(location.getTime());
            sb.append("\nlocType : ");// 定位类型
            sb.append(location.getLocType());
            sb.append("\nlocType description : ");// *****对应的定位类型说明*****
            sb.append(location.getLocTypeDescription());
            sb.append("\nlatitude : ");// 纬度
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");// 经度
            sb.append(location.getLongitude());
            sb.append("\naddr : ");// 地址信息
            sb.append(location.getAddrStr());

            if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                for (int i = 0; i < location.getPoiList().size(); i++) {
                    Poi poi = (Poi) location.getPoiList().get(i);
                    sb.append(poi.getName() + ";");
                }
            }
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 速度 单位：km/h
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());// 卫星数目
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 海拔高度 单位：米
                sb.append("\ngps status : ");
                sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                // 运营商信息
                if (location.hasAltitude()) {// *****如果有海拔高度*****
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                }
                sb.append("\noperationers : ");// 运营商信息
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }


            LocationInfo mLocationInfo = new LocationInfo();
            mLocationInfo.setAddr(location.getAddrStr());
            mLocationInfo.setTime(DateUtil.DateToStringYMDHM(new Date()));
            mLocationInfo.setLocType(location.getLocType() + "");
            mLocationInfo.setLocTypedescription(location.getLocTypeDescription());
            mLocationInfo.setLatitude(location.getLatitude() + "");
            mLocationInfo.setLontitude(location.getLongitude() + "");
            mLocationInfo.setLocationTime(location.getTime());


            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                mLocationInfo.setDescribe("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                mLocationInfo.setDescribe("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果

                mLocationInfo.setDescribe("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                mLocationInfo.setDescribe("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                mLocationInfo.setDescribe("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                mLocationInfo.setDescribe("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            if (mOption != null) {
                mLocationInfo.setCoorType(mOption.coorType);
                mLocationInfo.setScanSpan(mOption.getScanSpan() + "");
            }
            mLocationInfo.setSource("baidu");
            mLocationInfo.setSourceSyn(false);

            String status = "";
            EventBus.getDefault().post(mLocationInfo);
            Log.i(TAG, sb.toString());
        }
    }

    public LocationClientOption getOption() {
        return mOption;
    }

    public void setOption(LocationClientOption mOption) {
        this.mOption = mOption;
    }
}
