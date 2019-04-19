/*package com.taotao.controller;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import sun.net.ftp.FtpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class FTPTest {

    @Test
    public void testFtpClient() throws IOException {
        //创建一个ftpClient对象
        FTPClient ftpClient = new FTPClient();
        //创建一个ftp连接
        ftpClient.connect("192.168.43.130",21);
        //登录ftp服务器，使用用户名和密码
        ftpClient.login("nice","131459421");
        //上传文件
        //读取本地文件 FileInputStream
        File file = new File("C:\\Users\\nice\\Desktop\\temp\\0023.png");
        System.out.println(file.exists());
        FileInputStream fileInputStream = new FileInputStream(file);
        System.out.println("====="+fileInputStream.read());
       //设置上传路径
//        ftpClient.changeWorkingDirectory("/etc/vsftpd/chroot_list");
        ftpClient.changeWorkingDirectory("/usr/share/nginx/html/images");
        //第一个参数，服务端文档名
        //第二个参数，上传文档的inputStream
        ftpClient.storeFile("0023.png", fileInputStream);
        //设置为被动模式
//        ftpClient.enterLocalPassiveMode();
        //关闭连接
        fileInputStream.close();
        ftpClient.logout();
    }
}*/
