package com.example.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户镜像包记录表
 */
public class CustomerPackageRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Integer id;

    /** 时间年月日时分 */
    private LocalDateTime recordDate;

    /** 镜像包地址 */
    private String packageUrl;

    /** 修复内容描述 */
    private String fixContent;

    /** 镜像ID */
    private String imageId;

    /** 开发工程师 */
    private String developer;

    /** Commit id */
    private String commitId;

    /** 测试工程师 */
    private String tester;

    /** 测试结果 */
    private String testResult;

    /** 备注 */
    private String remarks;

    /** Android版本 (例如: a10, a13) */
    private String androidVersion;

    /** 分类 (例如: vmos, qt) */
    private String category;

    /** 记录创建时间 */
    private LocalDateTime createTime;

    /** 记录更新时间 */
    private LocalDateTime updateTime;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public void setPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
    }

    public String getFixContent() {
        return fixContent;
    }

    public void setFixContent(String fixContent) {
        this.fixContent = fixContent;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
} 