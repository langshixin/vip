package com.lang520.vip;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VipApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void contextLoads2() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d=null;
        try {
            d=f.parse("2019-12-19 18:09:00");
        } catch (ParseException e) {

            e.printStackTrace();
        }
        cal.setTime(d);
        int w=cal.get(Calendar.DAY_OF_WEEK)-1;
        if(w==0) w=7;
        System.out.println("星期"+w);
    }

  /*  @Test
    void test() throws Exception {
        File file = new File("C:\\demo\\08.mtn");//创建新文件
        if(file!=null && !file.exists()){
            file.createNewFile();
        }
        OutputStream oputstream = new FileOutputStream(file);
        URL url = new URL("https://unpkg.com/live2d-widget-model-hijiki@1.0.5/assets/mtn/08.mtn");
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
        uc.connect();
        InputStream iputstream = uc.getInputStream();
        System.out.println("file size is:"+uc.getContentLength());//打印文件长度
        byte[] buffer = new byte[4*1024];
        int byteRead = -1;
        while((byteRead=(iputstream.read(buffer)))!= -1){
            oputstream.write(buffer, 0, byteRead);
        }
        oputstream.flush();
        iputstream.close();
        oputstream.close();

    }*/

}
