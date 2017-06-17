package gachon.mobile.programming.android.finalproject.models;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-26.
 */

public class OnOffMixEventListData {
    private String title;
    private int totalCanAttend;
    private String bannerUrl;
    private String eventUrl;
    private String categoryIdx;
    private String regTime;
    private String usePayment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public int getTotalCanAttend() {
        return totalCanAttend;
    }

    public void setTotalCanAttend(int totalCanAttend) {
        this.totalCanAttend = totalCanAttend;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getCategoryIdx() {
        return categoryIdx;
    }

    public void setCategoryIdx(String categoryIdx) {
        this.categoryIdx = categoryIdx;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getUsePayment() {
        return usePayment;
    }

    public void setUsePayment(String usePayment) {
        this.usePayment = usePayment;
    }
}
