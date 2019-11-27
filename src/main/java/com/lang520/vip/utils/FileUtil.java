package com.lang520.vip.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author LSX
 * @version 1.0
 * @date 2019/11/26 17:03
 */
public class FileUtil {


    public DataFile upload(HttpServletRequest request, MultipartFile[] myfiles, String fileT, String modelCode)
            throws Exception {
        DataFile dataFile = new DataFile();

        if (myfiles.length > 0) {
            String realPath = request.getSession().getServletContext().getRealPath(File.separator);
            System.out.println("1--" + realPath);
            realPath = realPath.substring(0, realPath.length() - 1);
            System.out.println("2--" + realPath);
            int aString = realPath.lastIndexOf(File.separator);
            System.out.println("3--" + aString);

            for (int i = 0; i < myfiles.length; i++) {

                int type = myfiles[i].getOriginalFilename().lastIndexOf(".");
                String fileType = myfiles[i].getOriginalFilename().substring(type,
                        myfiles[i].getOriginalFilename().length());

                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                SimpleDateFormat m = new SimpleDateFormat("MM");
                SimpleDateFormat d = new SimpleDateFormat("dd");
                Date date = new Date();

                realPath = realPath.substring(0, aString) + File.separator + "fileDate" + File.separator +fileT + File.separator + year.format(date) + File.separator
                        + m.format(date) + File.separator + d.format(date) + File.separator;
                File folder = new File(realPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String newName = System.currentTimeMillis() + fileType;
                realPath = realPath + modelCode + newName;

                myfiles[i].transferTo(new File(realPath));

                dataFile.setOldName(myfiles[i].getOriginalFilename());
                dataFile.setFileUrl(File.separator+"fileDate" + File.separator + fileT + File.separator + year.format(date) + File.separator + m.format(date) + File.separator
                        + d.format(date));
                dataFile.setNewName(modelCode + newName);
            }
        }
        return dataFile;
    }



    public DataFile upload(HttpServletRequest request, MultipartFile myfile, String fileT, String modelCode)
            throws Exception {
        DataFile dataFile = new DataFile();
        String realPath = request.getSession().getServletContext().getRealPath(File.separator);
        System.out.println("1--" + realPath);
        realPath = realPath.substring(0, realPath.length() - 1);
        System.out.println("2--" + realPath);
        int aString = realPath.lastIndexOf(File.separator);
        System.out.println("3--" + aString);

        int type = myfile.getOriginalFilename().lastIndexOf(".");
        String fileType = myfile.getOriginalFilename().substring(type, myfile.getOriginalFilename().length());

        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat m = new SimpleDateFormat("MM");
        SimpleDateFormat d = new SimpleDateFormat("dd");
        Date date = new Date();

        realPath = realPath.substring(0, aString) +File.separator +"fileDate"+File.separator + fileT + File.separator + year.format(date) + File.separator
                + m.format(date) + File.separator + d.format(date) + File.separator;
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String newName = System.currentTimeMillis() + fileType;
        realPath = realPath + modelCode + newName;

        myfile.transferTo(new File(realPath));

        dataFile.setOldName(myfile.getOriginalFilename());
        dataFile.setFileUrl(
                File.separator+"fileDate" +File.separator + fileT + File.separator + year.format(date) + File.separator + m.format(date) + File.separator + d.format(date));
        dataFile.setNewName(modelCode + newName);
        return dataFile;
    }

    public static void deleteFile(HttpServletRequest request, List<String> filePath) {
        String realPath = request.getSession().getServletContext().getRealPath(File.separator);
        realPath = realPath.substring(0, realPath.length() - 1);
        int aString = realPath.lastIndexOf(File.separator);
        realPath = realPath.substring(0, aString);

        if (filePath != null && !filePath.isEmpty()) {
            for (String path : filePath) {
                File file = new File(realPath + path);
                System.out.println("path==" + realPath + path);
                if (file.exists()) {
                    file.delete();
                    System.out.println("成功删除文件");
                }
            }
        }

    }

    /**
     * @Description: 取得tomcat中的webapps目录 如： /home/gzxiaoi/apache-tomcat-8.0.45/webapps
     * @param request
     * @return
     */
    public static String getRealPath(HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath(File.separator);
        realPath = realPath.substring(0, realPath.length() - 1);
        int aString = realPath.lastIndexOf(File.separator);
        realPath = realPath.substring(0, aString);
        return realPath;
    }

    public static boolean saveFile(String savePath, String fileFullName, MultipartFile file, HttpServletRequest request)
            throws Exception {
        File uploadDirectory = new File(getRealPath(request));
        byte[] data = readInputStream(file.getInputStream());
        // new一个文件对象用来保存图片，默认保存当前工程根目录
        File uploadFile = new File(savePath + fileFullName);
        // 判断文件夹是否存在，不存在就创建一个
        File fileDirectory = new File(savePath);
        synchronized (uploadDirectory) {
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
               /* if (!uploadDirectory.mkdir()) {
                    throw new Exception("保存文件的父文件夹创建失败！路径为：" + savePath);
                }*/
            }
            if (!fileDirectory.exists()) {
                fileDirectory.mkdirs();
//                if (!fileDirectory.mkdir()) {
//                    throw new Exception("文件夹创建失败！路径为：" + savePath);
//                }
            }
        }

        // 创建输出流
        try (FileOutputStream outStream = new FileOutputStream(uploadFile)) {// 写入数据
            outStream.write(data);
            outStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return uploadFile.exists();
    }

    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    private final static List<UploadInfo> uploadInfoList = new ArrayList<>();

    public static void Uploaded(String md5, String guid, String chunk, String chunks, String uploadFolderPath,
                                String fileName, String ext, HttpServletRequest request) throws Exception {
        synchronized (uploadInfoList) {
            if ((md5!=null&&!md5.equals(""))&&(chunks!=null&&!chunks.equals(""))&&!isNotExist(md5,chunk)) {
                uploadInfoList.add(new UploadInfo(md5, chunks, chunk, uploadFolderPath, fileName, ext));
            }
        }
        boolean allUploaded = isAllUploaded(md5, chunks);
        int chunksNumber = Integer.parseInt(chunks);

        if (allUploaded) {
            mergeFile(chunksNumber, ext, guid, uploadFolderPath, request);
            // fileService.save(new
            // com.zhangzhihao.FileUpload.Java.Model.File(guid + ext, md5, new
            // Date()));
        }
    }

    //判断在uploadInfoList是否有存在MD5和chunk都相同的元素
    private static boolean isNotExist(final String md5, final String chunk) {
        boolean flag =false;
        for (UploadInfo uploadInfo : uploadInfoList) {
            if (uploadInfo.getChunk().equals(chunk)&&uploadInfo.getMd5().equals(md5)) {
                //若md5和chunk都相同，则认为两条记录相同，返回true
                flag=true;
            }
        }
        return flag;
    }

    private static boolean isAllUploaded(final String md5, String chunks) {
        int size = uploadInfoList.stream().filter(new Predicate<UploadInfo>() {
            @Override
            public boolean test(UploadInfo item) {
                return item.getMd5().equals(md5);
            }
        }).distinct().collect(Collectors.toList()).size();
        boolean bool = (size == Integer.parseInt(chunks));
        if (bool) {
            synchronized (uploadInfoList) {
                uploadInfoList.removeIf(new Predicate<UploadInfo>() {
                    @Override
                    public boolean test(UploadInfo item) {
                        return Objects.equals(item.getMd5(), md5);
                    }
                });
            }
        }
        return bool;
    }

    @SuppressWarnings("all")
    private static void mergeFile(int chunksNumber, String ext, String guid, String uploadFolderPath,
                                  HttpServletRequest request) {
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat m = new SimpleDateFormat("MM");
        SimpleDateFormat d = new SimpleDateFormat("dd");
        Date date = new Date();
        /* 合并输入流 */
        String mergePath = uploadFolderPath;

        String destPath = getRealPath(request) +File.separator +"fileDate"+File.separator + "video" + File.separator + year.format(date) + File.separator
                + m.format(date) + File.separator + d.format(date) + File.separator;// 文件路径
        String newName = System.currentTimeMillis() + ext;// 文件新名称

        SequenceInputStream s;
        InputStream s1;
        try {
            s1 = new FileInputStream(mergePath + 0 + ext);
            String tempFilePath;
            InputStream s2 = new FileInputStream(mergePath + 1 + ext);
            s = new SequenceInputStream(s1, s2);
            for (int i = 2; i < chunksNumber; i++) {
                tempFilePath = mergePath + i + ext;
                InputStream s3 = new FileInputStream(tempFilePath);
                s = new SequenceInputStream(s, s3);
            }
            // 通过输出流向文件写入数据(转移正式文件到目标目录)
            // uploadFolderPath + guid + ext
            saveStreamToFile(s, destPath, newName);
            // 删除保存分块文件的文件夹
            deleteFolder(mergePath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static boolean deleteFolder(String mergePath) {
        File dir = new File(mergePath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dir.delete();
    }

    private static void saveStreamToFile(SequenceInputStream inputStream, String filePath, String newName)
            throws Exception {
        File fileDirectory = new File(filePath);
        synchronized (fileDirectory) {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdirs()) {
                    throw new Exception("保存文件的父文件夹创建失败！路径为：" + fileDirectory);
                }
            }
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdirs()) {
                    throw new Exception("文件夹创建失败！路径为：" + fileDirectory);
                }
            }
        }

        /* 创建输出流，写入数据，合并分块 */
        OutputStream outputStream = new FileOutputStream(filePath + newName);
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }

    /**
     * @Description: 分片文件追加
     * @param request
     * @param sliceFile  分片文件
     * @param name   文件名称
     * @param dirType  文件夹类型 如video/audio
     * @param fileExt  文件扩展名 如.mp4/.avi  ./mp3
     * @return
     */
    public static String randomWrite(HttpServletRequest request, byte[] sliceFile, String name, String dirType,String fileExt) {
        try {

            /** 以读写的方式建立一个RandomAccessFile对象 **/
            //获取相对路径/home/gzxiaoi/apache-tomcat-8.0.45/webapps
            String realPath=getRealPath(request);
            //拼接文件保存路径 /fileDate/video/2017/08/09  如果没有该文件夹，则创建
            String savePath=getSavePath(realPath,dirType);
            // String realName = UUID.randomUUID().toString().replace("-", "");
            String realName = name;
            String saveFile =realPath+ savePath + realName+fileExt;
            RandomAccessFile raf = new RandomAccessFile(saveFile, "rw");
            // 将记录指针移动到文件最后
            raf.seek(raf.length());
            raf.write(sliceFile);
            return savePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @Description: 获取文件保存的路径，如果没有该目录，则创建
     * @param realPath 相对路径 ，如   /home/gzxiaoi/apache-tomcat-8.0.45/webapps
     * @param fileType  文件类型 如： images/video/audio用于拼接文件保存路径，区分音视频
     * @return
     */
    public static String getSavePath(String realPath, String fileType) {
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat m = new SimpleDateFormat("MM");
        SimpleDateFormat d = new SimpleDateFormat("dd");
        Date date = new Date();
        String sp= File.separator + "fileDate" + File.separator +fileType + File.separator + year.format(date) + File.separator
                + m.format(date) + File.separator + d.format(date) + File.separator;
        String savePath = realPath+ sp;
        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return sp;
    }


}
