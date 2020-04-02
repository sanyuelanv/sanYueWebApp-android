package com.sanyuelanv.sanwebapp.bean;

import com.sanyuelanv.sanwebapp.utils.SanYueWebAppFileUtils;

import java.io.File;

/**
 * Create By songhang in 2020/3/3
 */
public class SanYueAppMessage {
    private String url;
    private String date;
    private String version;
    private String rootPath;
    private String size;

    public SanYueAppMessage(String url, String date, String version, String rootPath) {
        this.url = url;
        this.date = date;
        this.version = version;
        this.rootPath = rootPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getSize() {
        if (this.size != null) return this.size;
        File file = new File(this.rootPath);
        long size =  SanYueWebAppFileUtils.getDirSize(file);
        String sizeStr = SanYueWebAppFileUtils.formatFileSize(size);
        this.size = sizeStr;
        return this.size;
    }
}
