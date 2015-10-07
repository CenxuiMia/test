package HksData;

import java.util.ArrayList;

/**
 * Created by miahuang on 2015/9/25.
 */
public class Event implements Form {
    String m_strTitle             = null;
    String m_strStartDate         = null;
    String m_strEndDate           = null;
    String m_strNote              = null;
    String m_strDetail            = null;
    String m_strPhone             = null;
    String m_strWeb               = null;
    ArrayList<Coupon> m_alCoupons = null;

    Event() {
        m_alCoupons = new ArrayList<>();
    }

    @Override
    public String getName() {
        return m_strTitle;
    }

    public String getEndDate() {
        return m_strEndDate;
    }

    public String getStartDate() {
        return m_strStartDate;
    }

    public String getNote() {
        return m_strNote;
    }

    public String getDetail() {
        return m_strDetail;
    }

    public String getPhone() {
        return m_strPhone;
    }

    public String getWeb() {
        return m_strWeb;
    }

    public int getCouponLength() {
        return m_alCoupons.size();
    }

    public Coupon getCoupon(int index) {
        return m_alCoupons.get(index);
    }
}
