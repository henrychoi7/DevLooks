package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

public class OnOffMixData {
    private OnOffMixErrorData error;
    private ArrayList<OnOffMixEventListData> eventList;

    public OnOffMixErrorData getError() {
        return error;
    }

    public ArrayList<OnOffMixEventListData> getEventList() {
        return eventList;
    }
}
