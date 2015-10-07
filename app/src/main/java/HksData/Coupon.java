package HksData;

/**
 * Created by miahuang on 2015/9/25.
 */
public class Coupon implements Form {
    String m_strName = null;
    String m_strURL  = null;

    Coupon() {

    }

    @Override
    public String getName() {
        return m_strName;
    }

    public String getURL() {
        return m_strURL;
    }
}
