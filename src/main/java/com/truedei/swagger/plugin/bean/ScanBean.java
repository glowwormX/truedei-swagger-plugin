package com.truedei.swagger.plugin.bean;


import io.swagger.annotations.ApiModelProperty;

public class ScanBean {

    /**
     * 扫描的路径
     */
    @ApiModelProperty("扫描的路径")
    private String path;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
