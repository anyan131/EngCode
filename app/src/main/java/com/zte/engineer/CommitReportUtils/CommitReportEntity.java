package com.zte.engineer.CommitReportUtils;

public class CommitReportEntity extends BaseResult {
    private boolean success;
    private String data;

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }
}
