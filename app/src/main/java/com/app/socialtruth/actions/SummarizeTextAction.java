package com.app.socialtruth.actions;

public class SummarizeTextAction extends NetworkAction {
    private String action = "SummarizeText";
    private String Key;
    public SummarizeTextAction(){}
    public SummarizeTextAction(String Key){
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
