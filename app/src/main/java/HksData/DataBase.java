package HksData;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by miahuang on 2015/9/25.
 */
public class DataBase {

    private static final String JSON_SHOP_NAME      = "name";
    private static final String JSON_SHOP_DISTANCE  = "distance";
    private static final String JSON_SHOP_LAT       = "lat";
    private static final String JSON_SHOP_LON       = "lon";
    private static final String JSON_SHOP_ADDRESS   = "addr";
    private static final String JSON_EVENT          = "event";
    private static final String JSON_EVENT_NAME     = "title";
    private static final String JSON_EVENT_DETAIL   = "detail";
    private static final String JSON_EVENT_PHONE    = "phone";
    private static final String JSON_EVENT_WEBSITE  = "website";
    private static final String JSON_EVENT_NOTE     = "note";
    private static final String JSON_EVENT_STA_DATE = "startDate";
    private static final String JSON_EVENT_END_DATE = "endDate";
    private static final String JSON_COUPON         = "coupon";
    private static final String JSON_COUPON_NAME    = "name";
    private static final String JSON_COUPON_URL     = "url";

    private final ArrayList<Shop> m_alShops;
    private static DataBase s_Instance = new DataBase();
    private int m_iCurrentShop   = 0;
    private int m_iCurrentEvent  = 0;
    private int m_iCurrentCoupon = 0;
    private Location m_Location  = null;

    private DataBase() {
        m_alShops = new ArrayList<>();
    }

    public static DataBase getInstance() {
        return s_Instance;
    }

    public boolean isEmpty() {
        return m_alShops.size() == 0;
    }

    public void clear() {
        m_alShops.clear();
        m_iCurrentShop  = 0;
        m_iCurrentEvent = 0;
    }

    public void setShop(JSONArray shopArray) {
        Shop shop;

        synchronized (this) {//同步化
            for(int iShop = 0; iShop<shopArray.length(); iShop++) {
                shop = new Shop();
                setShopData(shop, shopArray, iShop);
                m_alShops.add(shop);
            }
        }
    }

    public void setLocation(Location location) {
        m_Location = location;
    }

    public Location getLocation() {
        return m_Location;
    }

    public void setCurrentShop(int index) {
        m_iCurrentShop   = index;
        m_iCurrentEvent  = 0;
        m_iCurrentCoupon = 0;
    }

    public void setCurrentEvent(int index) {
        m_iCurrentEvent  = index;
        m_iCurrentCoupon = 0;
    }

    public void setCurrentCoupon(int index) {
        m_iCurrentCoupon = index;
    }

    public int getCurrentShopPosition() {
        return m_iCurrentShop;
    }

    public int getCurrentEventPosition() {
        return m_iCurrentEvent;
    }

    public int getCurrentCouponPosition() {
        return m_iCurrentCoupon;
    }

    public Shop getCurrentShop() {
        return getShop(m_iCurrentShop);
    }//已經知道,可以直接得到位置

    public Event getCurrentEvent() {
        return getShop(m_iCurrentShop).getEvent(m_iCurrentEvent);
    }

    public void goNext() {

        if (m_iCurrentEvent == getCurrentShop().getEventLength()-1) {//確定event溢位

            if (m_iCurrentShop == m_alShops.size()-1) {//確定shop溢位
                setCurrentShop(0);
            }else {
                setCurrentShop(m_iCurrentShop+1);
            }

        }else {
            setCurrentEvent(m_iCurrentEvent+1);
        }
    }

    public void goLast() {

        if (m_iCurrentEvent == 0) {//確定event溢位

            if (m_iCurrentShop == 0) {//確定shop溢位
                setCurrentShop(m_alShops.size()-1);
            }else {
                setCurrentShop(m_iCurrentShop-1);
            }

            setCurrentEvent(getCurrentShop().getEventLength()-1);
        }else {
            setCurrentEvent(m_iCurrentEvent-1);
        }
    }

    public Coupon getCurrentCoupon() {
        return getShop(m_iCurrentShop).getEvent(m_iCurrentEvent).getCoupon(m_iCurrentCoupon);
    }

    public Shop getShop(int index) {
        return m_alShops.get(index);
    }//解析商店位置的值

    public int getShopLength() {
        return m_alShops.size();
    }

    private void setShopData(Shop shop,JSONArray jsonArray,int index) {
        JSONObject jsonObject = jsonArray.optJSONObject(index);
        shop.m_strName     = jsonObject.optString(JSON_SHOP_NAME);
        shop.m_strAddress  = jsonObject.optString(JSON_SHOP_ADDRESS);
        shop.m_strDistance = jsonObject.optString(JSON_SHOP_DISTANCE);
        shop.m_strLat      = jsonObject.optString(JSON_SHOP_LAT);
        shop.m_strLon      = jsonObject.optString(JSON_SHOP_LON);

        Event event;

        for(int iEvent = 0; iEvent<jsonObject.optJSONArray(JSON_EVENT).length(); iEvent++) {
            event = new Event();
            setEventData(event,jsonObject.optJSONArray(JSON_EVENT),iEvent);
            shop.m_alEvents.add(event);
        }
    }

    private void setEventData(Event event,JSONArray jsonArray,int index){
        JSONObject jsonObject = jsonArray.optJSONObject(index);
        event.m_strTitle      = jsonObject.optString(JSON_EVENT_NAME);
        event.m_strStartDate  = jsonObject.optString(JSON_EVENT_STA_DATE);
        event.m_strEndDate    = jsonObject.optString(JSON_EVENT_END_DATE);
        event.m_strDetail     = jsonObject.optString(JSON_EVENT_DETAIL);
        event.m_strNote       = jsonObject.optString(JSON_EVENT_NOTE);
        event.m_strPhone      = jsonObject.optString(JSON_EVENT_PHONE);
        event.m_strWeb        = jsonObject.optString(JSON_EVENT_WEBSITE);

        Coupon coupon;

        for(int iCoupon = 0;iCoupon<jsonObject.optJSONArray(JSON_COUPON).length();iCoupon++) {
            coupon = new Coupon();
            setCouponData(coupon,jsonObject.optJSONArray(JSON_COUPON),iCoupon);
            event.m_alCoupons.add(coupon);
        }
    }

    private void setCouponData(Coupon coupon,JSONArray jsonArray,int index) {
        JSONObject jsonObject = jsonArray.optJSONObject(index);
        coupon.m_strName = jsonObject.optString(JSON_COUPON_NAME);
        coupon.m_strURL  = jsonObject.optString(JSON_COUPON_URL);
    }
}
