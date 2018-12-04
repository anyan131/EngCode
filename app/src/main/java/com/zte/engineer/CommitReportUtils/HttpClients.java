package com.zte.engineer.CommitReportUtils;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClients {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF8");
    public static final String DEFAULT_PROTOCOL = "http";
    private static final String TAG = HttpClients.class.getSimpleName();
    public HttpClients(){};

    public HttpResponseResult sendRequestGetResponse(String path, String request) {
        Log.d(TAG, format("path -> %s, request -> %s", path, request));
        
        HttpURLConnection connection = null;
        try {
            connection = initConnection(path);
            
            send(request, connection);

            return receive(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponseResult.RESPONSE_EXCEPTION_RESULT;
        } finally {
        	IOUtil.disconnectQuietly(connection);
        }
    }

    private HttpResponseResult receive(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        System.out.println("---lzg responseCode="+responseCode);
        Log.d(TAG, format("code -> %s", responseCode));
        System.out.println("### respmsg ="+connection.getResponseMessage());
        
        if (responseCode == HTTP_OK) {
            return getHttpResponseResult(connection.getInputStream(), HttpResponseResult.ResponseResultType.SUCCEEDED);
        }

        if (responseCode == HTTP_INTERNAL_ERROR) {
            return getHttpResponseResult(connection.getErrorStream(), HttpResponseResult.ResponseResultType.FAILED);
        }
    
        throw new IllegalStateException();
    }

    private HttpResponseResult getHttpResponseResult(InputStream inputStream, HttpResponseResult.ResponseResultType succeeded) throws IOException {
    	Scanner inScn = null;
        StringBuilder sb = new StringBuilder();
        
		try {
			inScn = new Scanner(inputStream);
			while (inScn.hasNextLine()) {
				sb.append(inScn.nextLine());
			}
			
			/*System.out.println("sb.toString()==" + sb);
			String response = CharStreams.toString(new InputStreamReader(
					inputStream, DEFAULT_CHARSET));
			*/
			Log.d(TAG, format("response -> %s", sb));
		}catch (Exception e) {
			 throw new IllegalStateException();
		}finally {
			if (null != inScn) {
				inScn.close();
			}
		}
		return new HttpResponseResult(succeeded, sb.toString());
    }

    private void send(String request, HttpURLConnection connection) throws IOException {
        byte[] bytes = request.getBytes(DEFAULT_CHARSET);
        
        connection.setFixedLengthStreamingMode(bytes.length);
        
        
        DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    private HttpURLConnection initConnection(String path) throws IOException {
//        URL url = new URL(DEFAULT_PROTOCOL, host, port, path);
    	  URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(6*1000);
        connection.setReadTimeout(6*1000);
//        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        //        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, conTimeout);  
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        return connection;
    }
}