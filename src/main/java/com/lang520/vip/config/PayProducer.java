package com.lang520.vip.config;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

/**
 * @author LSX
 * @version 1.0
 * @date 2019/12/16 15:16
 */
@Component
public class PayProducer {

    /**
     * 生产组,生产者必须在生产组内
     */
    private String producerGroup = "pay_group";

    /**
     * 端口
     */
    private String nameServer = "127.0.0.1:9876";


    private DefaultMQProducer producer;

    public PayProducer() {
        producer = new DefaultMQProducer(producerGroup);
//        producer.setRetryTimesWhenSendFailed(2); 重试机制
        // 指定nameServer地址,多个地址之间以 ; 隔开
        producer.setNamesrvAddr(nameServer);
        start();
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    /**
     * 对象在使用之前必须调用一次,并且只能初始化一次
     */
    public void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一般在应用上下文,使用上下文监听器,进行关闭
     */
    public void shutdown() {
        producer.shutdown();
    }

}