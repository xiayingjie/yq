package com.common.manage.view.head;

import com.common.manage.entity.yq.User;
import com.common.manage.tool.RandomUtil;
import com.common.manage.tool.RedisKey;
import com.common.manage.tool.SMSUtil;
import com.common.manage.view.BaseController;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.xyj.common.tool.BeanInfoUtil;
import com.xyj.common.tool.HttpClientUtil;
import com.xyj.common.tool.upload.spring.FileMeta;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/27
 */




@Controller
@RequestMapping(value = "/common")
public class CommonController extends BaseController {

        //注册
        @RequestMapping(value = "sendCaptcha")
        public void sendCaptcha(HttpServletRequest request, HttpServletResponse response, String phone, int type) {
            //获取key,放入redis中 缓存24小时
            String key=String.format(RedisKey.USER_PHONE_CODE, phone, type);
            Object obj=redisUtil.get(key);

            if(null==obj){
                int num=RandomUtil.random(4);
                redisUtil.set(key,String.valueOf(num));
                redisUtil.getRedisTemplate().expire(key,24, TimeUnit.HOURS);
                obj=num;
            }
            String content="您好，当前验证码为"+obj.toString()+"，请勿泄露给他人。【17寻找】";
            //发送短信
            boolean flag=SMSUtil.sendSMS(phone, content);
            Map map=null;
            if(flag==true){
                map=this.getSuccess();
            }else{
                map=this.getFaild(0);
            }
            this.out(response,map);



        }
        @RequestMapping(value = "error")
        public void error(HttpServletRequest request, HttpServletResponse response) {
            Map map =this.getFaild(0);
            this.out(response,map);

        }

        //验证验证码
        @RequestMapping(value = "verifyCaptcha")
        public void verifyCaptcha(HttpServletRequest request, HttpServletResponse response, String phone, int type,String captcha ) {
            //type 0 表示注册，
            //获取key,放入redis中 缓存24小时
            String key=String.format(RedisKey.USER_PHONE_CODE,phone,type);
            Object obj=redisUtil.get(key);
            Map map=null;
            if(null==obj || !obj.equals(captcha)){
                map=this.getFaild(0);
            }else{
                map=this.getSuccess();
            }
            this.out(response,map);
        }

        //上传文件
        @RequestMapping(value = "uploadImage", method = RequestMethod.POST)
        public void upload(MultipartHttpServletRequest request, HttpServletResponse response) {
            Map map=null;
            // 获取系统分隔符
            String fs = System.getProperties().getProperty(
                    "file.separator");

            List<String> files = new LinkedList<String>();
                //1.创建Iterator对象
            Iterator<String> itr = request.getFileNames();
            MultipartFile mpf = null;

            boolean flag=false;
            //2.遍历获取文件
            while (itr.hasNext()) {
                   StringBuffer path = new StringBuffer(request.getSession().getServletContext().getRealPath("/"));

                    //2.1 get next MultipartFile
                    mpf = request.getFile(itr.next());
                    // System.out.println(mpf.getOriginalFilename() + " uploaded! " + files.size());
                    String fileName=mpf.getOriginalFilename();
                    // 获取上传文件的类型
                    String fileExt = fileName.substring(
                            fileName.lastIndexOf(".") + 1, fileName.length());


                    try {
                        File temp = null;

                        Calendar calendar = Calendar.getInstance();
                        int YY = calendar.get(Calendar.YEAR);
                        int MM = calendar.get(Calendar.MONTH) + 1;
                        int DD = calendar.get(Calendar.DATE);
                        int HH = calendar.get(Calendar.HOUR);
                        int NN = calendar.get(Calendar.MINUTE);
                        int SS = calendar.get(Calendar.SECOND);

                        String dir=fs+ "yq" + fs+YY + fs + MM + fs + DD + fs;
                        path.append(dir);
                        //如果上传文件目录不存在则创建
                        File uploadDir = new File(path.toString());

                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        // 这里定义一个随机数，便于上传文件不同名
                        Random r = new Random();
                        String date = new SimpleDateFormat(
                                "yyyyMMddhhmmssSSS").format(calendar
                                .getTime());

                        String fname=date + r.nextInt(100) + "." + fileExt;
                        String newFileName = path.append(fname).toString();

                        files.add(dir+fname);
                        //复制文件到本地硬盘
                        FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(newFileName));
                        flag=true;

                    } catch (IOException e) {

                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                // 返回结果如下：
                // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]

                if(flag){
                    map=this.getSuccess();
                    String[]fss=new String[files.size()];
                    int i=0;
                    for(String file:files){
                        fss[i]="http://43.254.55.184"+file;
                        i++;
                    }
                    map.put("data",fss);

                }else{
                    map=this.getFaild(0);
                }
                this.out(response,map);

            }



    public static void main(String[] args) {
        String url="http://localhost:8080/common/uploadImage";
        Map map=new HashMap();
        map.put("file","/Users/xiayingjie/a.txt");
        String s=HttpClientUtil.postFile(url,map,map);
        System.out.println(s);
    }



}

