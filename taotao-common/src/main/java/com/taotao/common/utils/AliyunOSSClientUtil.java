package com.taotao.common.utils;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.apache.log4j.Logger;

import java.io.*;

public class AliyunOSSClientUtil {
    //log日志
    private static Logger logger = Logger.getLogger(AliyunOSSClientUtil.class);
    //阿里云API内或外域网名
    private static String ENDPOINT;
    //阿里云API的密钥Access key ID
    private static  String ACCESS_KEY_ID;
    //阿里云API的密钥Access Key Secret
    private static  String ACCESS_KEY_SECRET;
    //阿里云API的bucket名称
    private static  String BACKET_NAME;
    //阿里云API的文件夹名称
    private static  String FOLDER;
    //初始化属性
    static {
        ENDPOINT = OSSClientConstants.ENDPOINT;
        ACCESS_KEY_ID = OSSClientConstants.ACCESS_KEY_ID;
        ACCESS_KEY_SECRET = OSSClientConstants.ACCESS_KEY_SECRET;
        BACKET_NAME = OSSClientConstants.BACKET_NAME;
        FOLDER = OSSClientConstants.FOLDER;
    }

    public static OSSClient getOSSClient(){
        return new OSSClient(ENDPOINT,ACCESS_KEY_ID,ACCESS_KEY_SECRET);
    }
    public static String createBucketName(OSSClient ossClient,String bucketName){
        //存储空间
        final String bucketNames = bucketName;
        if (!ossClient.doesBucketExist(bucketName)){
            Bucket bucket = ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketNames;
    }

    public static void deleteBucket(OSSClient ossClient,String bucketName){
        ossClient.deleteBucket(bucketName);
        logger.info("删除" + bucketName + "Bucket成功");
    }

    public static String createFolder(OSSClient ossClient,String bucketName,String folder){
        //文件夹名
        final String keySuffixWithSlash = folder;
        //判断文件夹是否存在，不存在则创建
        if(!ossClient.doesObjectExist(bucketName,keySuffixWithSlash)){
            //创建文件夹
            ossClient.putObject(bucketName,keySuffixWithSlash,new ByteArrayInputStream(new byte[0]));
            logger.info("得到文件夹成功");
            OSSObject object = ossClient.getObject(bucketName,keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;

        }
        return keySuffixWithSlash;
    }

    /**
     * 上传图片至OSS
     * @param ossClient oss连接
     * @param bucketName ossClient oss连接
     * @param folder
     * @param key
     */
    public static void deleteFile(OSSClient ossClient, String bucketName,String folder,String key){
        ossClient.deleteObject(bucketName,folder+ key);
        logger.info("删除" + bucketName + "下的文件" + folder +key + "成功");
    }

    public static  String uploadObject2055(OSSClient ossClient, File file,String bucketName,String folder){
        String resultStr = null;
        try {
            //以输入流的形式上传文件
            InputStream is = new FileInputStream(file);
            //文件名
            String fileName = file.getName();
            //文件大小
            Long fileSize = file.length();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new SelectObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME,定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据key或文件名的扩展名生成。
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称(指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称)
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte");
            //上传文件(上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, is, metadata);
            //解析结果
            resultStr = putResult.getETag();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }


    public static String getContentType(String fileName){
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)){
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)){
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)){
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)){
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)){
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)){
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)){
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)){
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)){
            return "text/xml";
        }
        //返回数据类型
        return "image/jpeg";
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        //初始化OSSClient
        OSSClient ossClient = AliyunOSSClientUtil.getOSSClient();
        //上传文件
        String files = "C:\\Users\\nice\\Desktop\\temp\\role.png";
        String[] file = files.split(",");
        for (String fileName : file) {
            File filess = new File(fileName);
            String md5key = AliyunOSSClientUtil.uploadObject2055(ossClient,filess,BACKET_NAME,FOLDER);
            logger.info("上传后的文件MD5数字唯一签名:" + md5key);
            //上传后的文件MD5数字唯一签名
        }
    }



}
