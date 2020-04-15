package com.app.socialtruth.actions;

public class AddTruthAction extends NetworkAction {
    private String action = "AddTruth";
    private String key;
    private String url;
    private String isTrue;
    private String summary;
    private String title;
    public AddTruthAction(){}
    public AddTruthAction( String key, String url,String isTrue,String summary,String title){
        this.key = key;
        this.url = url;
        this.isTrue = isTrue;
        this.summary = summary;
        this.title = title;
    }
    @Override
    public String getAction() {
        return null;
    }

    @Override
    public void setAction(String action) {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(String isTrue) {
        this.isTrue = isTrue;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
