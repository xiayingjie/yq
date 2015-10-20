package com.common.manage.tool;

import com.xyj.common.tool.DateUtil;
import com.xyj.common.tool.HttpClientUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/28
 */

public class SMSUtil {

    /**
     * 发送短信验证
     * @return
     */
    public static boolean sendSMS(String phone,String content)  {
        boolean flag=false;
        URL url = null;
        String CorpID="TEST02308";
        String Pwd="123456";
        String send_content= null;
        String send_time= DateUtil.datetime();
        try {
            send_content = URLEncoder.encode(content.replaceAll("<br/>", " "), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            url = new URL("http://42.96.149.47:1086/sdk/BatchSend.aspx?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+phone+"&Content="+send_content+"&Cell=&SendTime=&encode=utf-8");
            System.out.println(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        long inputLine = -1;
        try {

            in = new BufferedReader(new InputStreamReader(url.openStream()));
            inputLine = new Long(in.readLine()).longValue();

        } catch (Exception e) {
            inputLine=-2;
        }
        if(inputLine>=0){
            flag=true;
        }

        return flag;
    }

    public static void main(String[] args) {
        sendSMS("18789687423", "您的验证码为133434，请不要告诉任何人，谢谢【科技】");
    }
}
