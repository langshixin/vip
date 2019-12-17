package com.lang520.vip.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author LSX
 * @version 1.0
 * @date 2019/11/29 17:17
 */
@Document(indexName = "myesuser",type = "user")
public class UserEntity {
    @Id
    private int id;
    private String username;
    private int sex;
    private int age;

    public UserEntity() {
    }

    public UserEntity(int id, String username, int sex, int age) {
        this.id = id;
        this.username = username;
        this.sex = sex;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
