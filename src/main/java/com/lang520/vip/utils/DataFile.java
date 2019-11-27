package com.lang520.vip.utils;

/**
 * @author LSX
 * @version 1.0
 * @date 2019/11/26 17:15
 */
public class DataFile {

    private String oldName;
    private String fileUrl;
    private String newName;

    public DataFile() {
    }

    public DataFile(String oldName, String fileUrl, String newName) {
        this.oldName = oldName;
        this.fileUrl = fileUrl;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
