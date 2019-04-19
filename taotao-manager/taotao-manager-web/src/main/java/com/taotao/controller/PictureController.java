package com.taotao.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.taotao.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;


@Controller
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @RequestMapping(value = "/pic/upload",method = RequestMethod.POST)
    @ResponseBody
    public String pictureUpload(MultipartFile uploadFile, HttpServletRequest request) throws UnknownHostException {
        System.out.println(request.getRequestURI());
        System.out.println(InetAddress.getLocalHost().getHostName());
        Map result = null;
        try {
            result = pictureService.uploadPicture(uploadFile,request.getRequestURI(),InetAddress.getLocalHost().getHostName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return JSONUtils.toJSONString(result.toString());
    }
}
