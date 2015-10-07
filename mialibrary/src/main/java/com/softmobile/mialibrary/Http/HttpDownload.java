package com.softmobile.mialibrary.Http;

import com.softmobile.mialibrary.utility.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

abstract public class HttpDownload implements Runnable{
    private StringBuilder mStringBuilder;

    public HttpDownload() {
        mStringBuilder = new StringBuilder();
    }


    public HttpDownload append(CharSequence csq) {
        mStringBuilder.append(csq);
        return this;
    }


    public HttpDownload append(CharSequence csq, int start, int end) {
        mStringBuilder.append(csq.subSequence(start, end));
        return this;
    }


    public HttpDownload append(char c) throws IOException {
        mStringBuilder.append(c);
        return this;
    }

    @Override
    public void run() {
        Utility.log("httpDownload run");

        BufferedReader bufferedReader = null;
        HttpURLConnection httpURLConnection = null;

        try {
            //下載JSON資料
            Utility.log("httpDownload url = " + mStringBuilder.toString());

            URL url = new URL(mStringBuilder.toString());
            httpURLConnection = (HttpURLConnection)url.openConnection();

            InputStreamReader inputStreamReader =
                    new InputStreamReader(new BufferedInputStream(httpURLConnection.getInputStream()));
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String string;
            while ((string = bufferedReader.readLine()) !=  null) {
                stringBuilder.append(string);
            }
            handleData(stringBuilder.toString());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    /*
    解析JSON資料
     */
    abstract public void handleData(String string) ;
}