package HksData;

import java.util.ArrayList;

/**
 * Created by miahuang on 2015/9/25.
 */
public class Shop implements Form {
    String m_strName            = null;
    String m_strLat             = null;
    String m_strLon             = null;
    String m_strAddress         = null;
    String m_strDistance        = null;
    ArrayList<Event> m_alEvents = null;

    Shop() {
        m_alEvents = new ArrayList<>();
    }

    @Override
    public String getName() {
        return m_strName;
    }

    public String getLat() {
        return m_strLat;
    }

    public String getLon() {
        return m_strLon;
    }

    public String getAddress() {
        return m_strAddress;
    }

    public String getDistance() {
        return m_strDistance;
    }

    public int getEventLength() {
        return m_alEvents.size();
    }

    public Event getEvent(int index) {
        return m_alEvents.get(index);
    }
}
