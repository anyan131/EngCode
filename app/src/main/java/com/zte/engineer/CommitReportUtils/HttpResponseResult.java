package com.zte.engineer.CommitReportUtils;

public class HttpResponseResult {
    private String responseResult;
    private Exception responseException;
    private ResponseResultType resultType;

    public HttpResponseResult(ResponseResultType resultType, String responseResult, Exception responseException) {
        this.resultType = resultType;
        this.responseException = responseException;
        this.responseResult = responseResult;
    }

    public HttpResponseResult(ResponseResultType resultType, String responseResult) {
        this(resultType, responseResult, null);
    }

    public static enum ResponseResultType {
        SUCCEEDED,
        FAILED
    }

    public final static HttpResponseResult RESPONSE_EXCEPTION_RESULT = new HttpResponseResult(ResponseResultType.FAILED, "", null);

    public ResponseResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResponseResultType resultType) {
        this.resultType = resultType;
    }

    public Exception getResponseException() {
        return responseException;
    }

    public void setResponseException(Exception responseException) {
        this.responseException = responseException;
    }

    public String getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }
}
