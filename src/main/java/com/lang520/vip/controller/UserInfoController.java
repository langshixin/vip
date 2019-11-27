package com.lang520.vip.controller;

import com.alibaba.fastjson.JSONObject;
import com.lang520.vip.config.ProductWebSocket;
import com.lang520.vip.utils.FileUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LSX
 * @version 1.0
 * @date 2019/11/22 11:25
 */

@Controller
public class UserInfoController {
    private static Logger log = Logger.getLogger(UserInfoController.class);


    /**
     * 单文件上传
     * @param files
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile files, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        response.setCharacterEncoding("utf-8");
        String msg = "添加成功";
        log.info("-------------------开始调用上传文件upload接口-------------------");
        try{
            String name = files.getOriginalFilename();
            String path1 = this.getClass().getResource("/").getPath();
            //获取类加载的根路径
            System.out.println(path1);
//            String path2 = this.getClass().getResource("").getPath();
//            //获取当前类的所在工程路径
//            System.out.println(path2);
            String path = this.getClass().getResource("/").getPath();
//            int index = path.indexOf("Shopping");
//            path = path.substring(0, index + "Shopping".length()) + "/WebContent/resources/upload/";
            path = path + File.separator + name;
            File uploadFile = new File(path);
            files.transferTo(uploadFile);
        }catch(Exception e){
            msg="添加失败";
        }
        log.info("-------------------结束调用上传文件upload接口-------------------");
        json.put("msg", msg);
        return json.toString();
    }

    /**
     * 用来测试的一些东西
     * @return
     */
    @RequestMapping("/test")
    private String test(){

        return "test";
    }


    /**
     * 用来测试websocket
     * @return
     */
    @RequestMapping("/webs")
    private String webs(){

        return "webs";
    }
    /**
     * 用来测试websocket
     * @return
     */
    @RequestMapping("/webs2")
    private String webs2(){

        return "webs2";
    }
    /**
     * 用来测试websocket通过服务端给前端推送消息
     * @return
     */
    @RequestMapping("/sendToUser")
    @ResponseBody
    private String sendToUser(String message,String sid){
        try {
            ProductWebSocket.sendtoUser(message,sid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }
    /**
     * 用来测试websocket通过服务端给前端推送消息
     * @return
     */
    @RequestMapping("/sendAll")
    @ResponseBody
    private String sendAll(String message){
        try {
            ProductWebSocket.sendInfo(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }


    /**
     *
     * @Description:
     *          接受文件分片，合并分片
     * @param guid
     *          可省略；每个文件有自己唯一的guid，后续测试中发现，每个分片也有自己的guid，所以不能使用guid来确定分片属于哪个文件。
     * @param md5value
     *          文件的MD5值
     * @param chunks
     *          当前所传文件的分片总数
     * @param chunk
     *          当前所传文件的当前分片数
     * @param id
     *          文件ID，如WU_FILE_1，后面数字代表当前传的是第几个文件,后续使用此ID来创建临时目录，将属于该文件ID的所有分片全部放在同一个文件夹中
     * @param name
     *          文件名称，如07-中文分词器和业务域的配置.avi
     * @param type
     *          文件类型，可选，在这里没有用到
     * @param lastModifiedDate 文件修改日期，可选，在这里没有用到
     * @param size  当前所传分片大小，可选，没有用到
     * @param file  当前所传分片
     * @return
     * @author: xiangdong.she
     * @date: Aug 20, 2017 12:37:56 PM
     */
    @ResponseBody
    @RequestMapping(value = "/BigFileUp")
    public String fileUpload(HttpServletRequest request, String guid, String md5value, String chunks, String chunk, String id, String name,
                             String type, String lastModifiedDate, int size, MultipartFile file) {
        String fileName;
        JSONObject result=new JSONObject();
        try {
            int index;
            String uploadFolderPath = this.getClass().getResource("/").getPath();

            String mergePath = uploadFolderPath + "\\fileDate\\" + id + "\\";
            String ext = name.substring(name.lastIndexOf("."));

            // 判断文件是否分块
            if (chunks != null && chunk != null) {
                index = Integer.parseInt(chunk);
                fileName = String.valueOf(index) + ext;
                // 将文件分块保存到临时文件夹里，便于之后的合并文件
                FileUtil.saveFile(mergePath, fileName, file, request);
                // 验证所有分块是否上传成功，成功的话进行合并
                FileUtil.Uploaded(md5value, guid, chunk, chunks, mergePath, fileName, ext, request);
            } else {
                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                SimpleDateFormat m = new SimpleDateFormat("MM");
                SimpleDateFormat d = new SimpleDateFormat("dd");
                Date date = new Date();
                String destPath = uploadFolderPath + "\\fileDate\\" + "video" + "\\" + year.format(date) + "\\"
                        + m.format(date) + "\\" + d.format(date) + "\\";// 文件路径
                String newName = System.currentTimeMillis() + ext;// 文件新名称
                // fileName = guid + ext;
                // 上传文件没有分块的话就直接保存目标目录
                FileUtil.saveFile(destPath, newName, file, request);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            result.put("code", 0);
            result.put("msg", "上传失败");
            result.put("data", null);
            return result.toString();
        }
        result.put("code", 1);
        result.put("msg", "上传成功");
        return result.toString();
    }





}
