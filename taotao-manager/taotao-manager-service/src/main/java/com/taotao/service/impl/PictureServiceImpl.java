package com.taotao.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.taotao.common.utils.AliyunOSSClientUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.OSSClientConstants;
import com.taotao.service.PictureService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.taotao.common.utils.OSSClientConstants.*;

@Service
public class PictureServiceImpl implements PictureService {
    //log日志
    private static Logger logger = Logger.getLogger(AliyunOSSClientUtil.class);

//    private  PutObjectResult result;

    @Override
    public Map uploadPicture(MultipartFile uploadFile,String requestUri,String hostname) throws IOException {
        //生成一个新的文件名
        //取原始文件名
        String oldName = uploadFile.getOriginalFilename();
        //生成新文件名
        //UUID.randomUUID();
        String newName = IDUtils.genImageName();
        newName = newName + oldName.substring(oldName.lastIndexOf("."));
        Map map = new HashMap();

        InputStream inputStream = null;
//        PutObjectRequest putObjectRequest = null;
//        PutObjectResult putObjectResult = null;
        OSSClient ossClient = null;
        byte[] buffer = new byte[1024];
        //图片上传
        try {
            inputStream = uploadFile.getInputStream();


            //初始化OSSClient
            ossClient = AliyunOSSClientUtil.getOSSClient();

            PutObjectRequest putObjectRequest= new PutObjectRequest(BACKET_NAME,FOLDER+newName,inputStream);

            //上传回调
            Callback callback = new Callback();
            callback.setCallbackUrl(requestUri);
            callback.setCallbackHost("/");
/*            callback.setCallbackBody("{\\\"bucket\\\":${bucket},\\\"object\\\":${object},"
                    + "\\\"mimeType\\\":${mimeType},\\\"size\\\":${size},"
                    + "\\\"my_var1\\\":${x:var1},\\\"my_var2\\\":${x:var2}}");*/
            callback.setCallbackBody("{\\\"mimeType\\\":${\\\"text/html\\\"},\\\"size\\\":${10}");
            map.put("status","200");
            map.put("message","success");
            callback.setCallbackVar(map);
            // callback.setCallbackBody(map.toString());
            callback.setCalbackBodyType(Callback.CalbackBodyType.JSON);
/*            callback.addCallbackVar("statusCode", "200");
            callback.addCallbackVar("message", "success");*/
            putObjectRequest.setCallback(callback);
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
            putObjectResult.getResponse().getContent().read(buffer);
            putObjectResult.getResponse().getContent().close();

            //  ossClient.putObject(BACKET_NAME,newName,inputStream);
//            System.out.println(ossClient.putObject(BACKET_NAME,FOLDER+newName,inputStream));
        //    ossClient.shutdown();
           // AliyunOSSClientUtil.uploadObject2055(ossClient,FOLDER+newName,BACKET_NAME,FOLDER);
            //上传文件
            /*String files = "C:\\Users\\nice\\Desktop\\temp\\role.png";
            String[] file = files.split(",");
            for (String fileName : file) {
                File filess = new File(fileName);
                String md5key = AliyunOSSClientUtil.uploadObject2055(ossClient,filess,BACKET_NAME,FOLDER);
                logger.info("上传后的文件MD5数字唯一签名:" + md5key);
                //上传后的文件MD5数字唯一签名
            }*/
//           if (!result.getResponse().isSuccessful()){
/*               map.put("id",result.getResponse().getRequestId());
               map.put("uri",result.getResponse().getUri());
               map.put("error",result.getResponse().getStatusCode());
               map.put("message",result.getResponse().getErrorResponseAsString());
               return map;*/
//           }else {
//               map.put("id",result.getResponse().getRequestId());
//               map.put("uri",result.getResponse().getUri());
//               map.put("error",result.getResponse().getStatusCode());
//               map.put("message",result.getResponse().getErrorResponseAsString());
/*               return map;
           }*/
   // 刚刚注释的
/*  map.put("id",result.getResponse().getRequestId());
            map.put("uri",result.getResponse().getUri());
            map.put("error",result.getResponse().getStatusCode());
            map.put("message",result.getResponse().getErrorResponseAsString());*/

            return map;
        } catch (IOException e) {
            e.printStackTrace();
           // e.printStackTrace();
            /*map.put("id",PictureServiceImpl.result.getResponse().getRequestId());
            map.put("uri",PictureServiceImpl.result.getResponse().getUri());
            map.put("error",PictureServiceImpl.result.getResponse().getStatusCode());
            map.put("message",PictureServiceImpl.result.getResponse().getErrorResponseAsString());*/
            map.put("error","500");
            map.put("message","failure");
            return map;
        }finally {
            inputStream.close();
            ossClient.shutdown();
        }
       // finally {
//            map.put("id",result.getResponse().getRequestId());
//            map.put("uri",result.getResponse().getUri());
//            map.put("error",result.getResponse().getStatusCode());
//            map.put("message",result.getResponse().getErrorResponseAsString());
//            return map;
     //   }
       // return null;
    }
}
