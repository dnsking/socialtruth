package com.app.socialtruth.data;

public class TruthResult {
    private boolean articleTrue;
    private String url;
    private String briefArticle;
    private String header;

    public TruthResult(){}

    public boolean isArticleTrue() {
        return articleTrue;
    }

    public void setArticleTrue(boolean articleTrue) {
        this.articleTrue = articleTrue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBriefArticle() {
        return briefArticle;
    }

    public void setBriefArticle(String briefArticle) {
        this.briefArticle = briefArticle;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
