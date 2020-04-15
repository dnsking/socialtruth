package com.app.socialtruth.actions;

public class SearchTruthAction extends NetworkAction {
    private String action = "SearchTruth";
    private String Key;
    public SearchTruthAction(){}
    public SearchTruthAction(String Key){
        this.Key = Key;
    }
    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {

    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
