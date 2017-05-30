package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-26.
 */

public class OnOffMixData {
    private OnOffMixErrorData error;
    private ArrayList<OnOffMixEventListData> eventList;

    public OnOffMixErrorData getError() {
        return error;
    }

    public void setError(OnOffMixErrorData error) {
        this.error = error;
    }

    public ArrayList<OnOffMixEventListData> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<OnOffMixEventListData> eventList) {
        this.eventList = eventList;
    }
}
