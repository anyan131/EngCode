package com.zte.engineer.CommitReportUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CommitReportEntity extends BaseResult {
    private boolean success;
    private String data;

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public JsonObject getData() {
        return (JsonObject)new JsonParser().parse(this.data);
    }
    public void setData(String data) {
        this.data = data;
    }
}
