package com.lang520.vip.controller;

import com.alibaba.fastjson.JSONObject;
import com.lang520.vip.config.PayProducer;
import com.lang520.vip.config.ProductWebSocket;
import com.lang520.vip.dao.UserReposiory;
import com.lang520.vip.entity.UserEntity;
import com.lang520.vip.utils.FileUtil;
import com.lang520.vip.utils.ThreadReptile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * @author LSX
 * @version 1.0
 * @date 2019/11/22 11:25
 */

@Api(description = "闹着玩接口")
@Controller
public class UserInfoController {
    private static Logger log = Logger.getLogger(UserInfoController.class);

    private static String UPLOAD_PATH = "fileData/upload";
    /**
     * 单文件上传
     * @param files
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "小文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile files, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        response.setCharacterEncoding("utf-8");
        String msg = "添加成功";
        log.info("-------------------开始调用上传文件upload接口-------------------");
        try{
            String name = files.getOriginalFilename();
            Path directory = Paths.get(UPLOAD_PATH);

            if(!Files.exists(directory)){
                Files.createDirectories(directory);
            }
            files.transferTo(directory.resolve(name));
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
    @ApiOperation(value = "获取分片上传页面")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    private String test(){
        return "test";
    }

    /**
     * 用来测试websocket
     * @return
     */
    @ApiOperation(value = "用来测试websocket 001用户连接")
    @RequestMapping(value = "/webs", method = RequestMethod.GET)
    private String webs(){
        return "webs";
    }

    /**
     * 用来测试websocket
     * @return
     */
    @ApiOperation(value = "用来测试websocket 002用户连接")
    @RequestMapping(value = "/webs2", method = RequestMethod.GET)
    private String webs2(){
        return "webs2";
    }

    /**
     * 玩玩多线程
     * @return
     */
    @ApiOperation(value = "玩玩多线程")
    @ResponseBody
    @RequestMapping(value = "/threadTrue", method = RequestMethod.GET)
    private String threadTrue(){

                int cpuNum = Runtime.getRuntime().availableProcessors();// 获取处理器数量
                int threadNum = cpuNum * 2 + 1;// 根据cpu数量,计算出合理的线程并发数
                int corePoolSize = cpuNum * 2;
                int maximumPoolSize = corePoolSize + 1;
                long keepAliveTime = 60l;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(300), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        threadPoolExecutor.execute(new ThreadReptile());

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new ThreadReptile());
        return "随便玩玩";
    }


    /**
     * 用来测试websocket通过服务端给前端推送消息
     * @return
     */
    @ApiOperation(value = "用来测试websocket通过服务端给指定前端推送消息")
    @RequestMapping(value = "/sendToUser", method = RequestMethod.GET)
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
    @ApiOperation(value = "用来测试websocket通过服务端给所有在线前端推送消息")
    @RequestMapping(value = "/sendAll", method = RequestMethod.GET)
    @ResponseBody
    private String sendAll(String message){
        try {
            ProductWebSocket.sendInfo(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }


    @Autowired
    private PayProducer payProducer;

    /**
     * topic,消息依赖于topic
     */
    private static final String topic = "pay_test_topic";

    @ResponseBody
    @RequestMapping(value = "/rq",method = RequestMethod.GET)
    public Object callback(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        // 创建消息  主题   二级分类   消息内容好的字节数组
        Message message = new Message(topic, "taga",("hello rocketMQ " + text).getBytes());

        SendResult send = payProducer.getProducer().send(message);

        System.out.println(send);

        return new HashMap<>();
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
    @ApiOperation(value = "大文件上传 分片")
    @ResponseBody
    @RequestMapping(value = "/BigFileUp", method = RequestMethod.POST)
    public String fileUpload(HttpServletRequest request, String guid, String md5value, String chunks, String chunk, String id, String name,
                             String type, String lastModifiedDate, int size, MultipartFile file) {
        String fileName;
        JSONObject result=new JSONObject();
        try {
            int index;
//            String uploadFolderPath =  new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();

            String mergePath = Paths.get(UPLOAD_PATH) + File.separator  + id + File.separator ;
            String ext = name.substring(name.lastIndexOf("."));

            // 判断文件是否分块
            if (chunks != null && chunk != null) {
                index = Integer.parseInt(chunk);
                fileName = String.valueOf(index) + ext;
                // 将文件分块保存到临时文件夹里，便于之后的合并文件
                FileUtil.saveFile(mergePath, fileName, file, request);
                // 验证所有分块是否上传成功，成功的话进行合并
                String uploaded = FileUtil.Uploaded(md5value, guid, chunk, chunks, mergePath, fileName, ext, request);
                if(!StringUtils.isEmpty(uploaded)){
                    result.put("url", uploaded);
                }
            } else {
                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                SimpleDateFormat m = new SimpleDateFormat("MM");
                SimpleDateFormat d = new SimpleDateFormat("dd");
                Date date = new Date();
                String destPath = Paths.get(UPLOAD_PATH)  +File.separator  + "video" + File.separator  + year.format(date) + File.separator 
                        + m.format(date) + File.separator  + d.format(date) + File.separator ;// 文件路径
                String newName = System.currentTimeMillis() + ext;// 文件新名称
                // fileName = guid + ext;
                // 上传文件没有分块的话就直接保存目标目录
                FileUtil.saveFile(destPath, newName, file, request);
                result.put("url", destPath+newName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result.put("code", 0);
            result.put("msg", "上传失败");
            return result.toString();
        }
        result.put("code", 1);
        result.put("msg", "上传成功");
        return result.toString();
    }



  /*  @Autowired
    private UserReposiory userReposiory;

    @RequestMapping(value = "/addUser",method = RequestMethod.GET)
    @ResponseBody
    public UserEntity addUser(Integer id) {
        UserEntity user1 = new UserEntity();
        user1.setAge(19);
        user1.setId(id);
        user1.setSex(1);
        user1.setUsername("你1大爷"+id);

        userReposiory.save(user1);
       *//* UserEntity user2 = new UserEntity();
        user2.setAge(19);
        user2.setId(3);
        user2.setSex(1);
        user2.setUsername("你2大爷"+id);
        UserEntity user3 = new UserEntity();
        user3.setAge(19);
        user3.setId(2);
        user3.setSex(1);
        user3.setUsername("你3大爷"+id);

        List<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(user1);
        userEntityList.add(user3);
        userEntityList.add(user2);
        userReposiory.saveAll(userEntityList);*//*
        return user1;
    }

    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
    @ResponseBody
    public  Iterable<UserEntity> findUser(Integer id) {
        return userReposiory.findAll();
    }


    @RequestMapping(value = "/findUserName", method = RequestMethod.GET)
    @ResponseBody
    public  Iterable<UserEntity> findUserName(String username) {
        return userReposiory.findByUsername(username);
    }
*/
}
