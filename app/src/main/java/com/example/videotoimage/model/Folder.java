package com.example.videotoimage.model;

public class Folder {
    String folderName ;
    String folderPath ;
    int count = 0 ;

    public Folder(String folderName, String folderPath, int count) {
        this.folderName = folderName;
        this.folderPath = folderPath;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
}
