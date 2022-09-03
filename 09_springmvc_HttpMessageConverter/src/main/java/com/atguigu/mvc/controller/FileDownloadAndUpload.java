package com.atguigu.mvc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
public class FileDownloadAndUpload {

    @RequestMapping("/testDownload")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException {
        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取服务器中文件的真实路径
        String realPath = servletContext.getRealPath("/static/img/1.jpg"); // servlet上下文getRealPath() - 获取服务器部署路径
        //创建输入流
        InputStream is = new FileInputStream(realPath);
        //创建字节数组
        byte[] bytes = new byte[is.available()];    // 输入流available() - 创建长度为文件对应的字节数大小
        //将流读到字节数组中
        is.read(bytes);
        //创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        //设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition", "attachment;filename=1.jpg");    // 响应报文header，可设置下载的默认文件名
        //设置响应状态码
        HttpStatus statusCode = HttpStatus.OK;
        //创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode); // ResponseEntity<byte[]> 响应报文类型
        //关闭输入流
        is.close();
        return responseEntity;
    }

    @RequestMapping("/testUpload")
    public String testUp(MultipartFile uploadFile, HttpSession session) throws IOException {
        String name = uploadFile.getName(); // 获取上传文件的key名：uploadFile
        //获取上传的文件的文件名
        String fileName = uploadFile.getOriginalFilename();
        //处理文件重名问题，若不处理，则文件会被覆盖写入
        String hzName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString() + hzName;   // 防止上传文件重名，使用UUID
        //获取服务器中photo目录的路径
        ServletContext servletContext = session.getServletContext();
        String uploadFilePath = servletContext.getRealPath(name);   // getRealPath部署路径下的上传文件夹名称，此处使用"uploadFile"
        File file = new File(uploadFilePath);
        if(!file.exists()){
            file.mkdir();   // 若"uploadFile"文件夹目录不存在，则创建一个
        }
        String finalPath = uploadFilePath + File.separator + fileName;
        //实现上传功能
        uploadFile.transferTo(new File(finalPath)); // 拷贝文件上传到服务器目录
        return "success";
    }
}
