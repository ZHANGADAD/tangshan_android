package com.tangshan.gui.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 章鱼彩票
 * Created by Tony on 21/04/2017.
 */

public class AppReleaseInfo implements Parcelable {

    private String appVersion;      // 版本号
    private int versionCode;        // 版本Code;数字代号
    private String downloadUrl;     // 下载地址;安卓等提供
    private String releaseNote;     // 发布说明
    private String releaseImageUrl; // 发布配图地址
    private String packageName;     // 安装包名称;在安卓设备上下载时显示的文件名
    private String packageSize;     // 安装包尺寸
    private String releaseTime;     // 发布时间
    private String activeRemind;    // 是否主动提醒;客户端根据此属性决定是否弹窗提示用户更新
    private String forceUpdate;     // 是否强制更新;如果是，客户端应阻止用户在升级前做任何操作（例如弹出模态窗口）

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public String getReleaseImageUrl() {
        return releaseImageUrl;
    }

    public void setReleaseImageUrl(String releaseImageUrl) {
        this.releaseImageUrl = releaseImageUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getActiveRemind() {
        return activeRemind;
    }

    public void setActiveRemind(String activeRemind) {
        this.activeRemind = activeRemind;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appVersion);
        dest.writeInt(this.versionCode);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.releaseNote);
        dest.writeString(this.releaseImageUrl);
        dest.writeString(this.packageName);
        dest.writeString(this.packageSize);
        dest.writeString(this.releaseTime);
        dest.writeString(this.activeRemind);
        dest.writeString(this.forceUpdate);
    }

    public AppReleaseInfo() {
    }

    protected AppReleaseInfo(Parcel in) {
        this.appVersion = in.readString();
        this.versionCode = in.readInt();
        this.downloadUrl = in.readString();
        this.releaseNote = in.readString();
        this.releaseImageUrl = in.readString();
        this.packageName = in.readString();
        this.packageSize = in.readString();
        this.releaseTime = in.readString();
        this.activeRemind = in.readString();
        this.forceUpdate = in.readString();
    }

    public static final Parcelable.Creator<AppReleaseInfo> CREATOR = new Parcelable.Creator<AppReleaseInfo>() {
        @Override
        public AppReleaseInfo createFromParcel(Parcel source) {
            return new AppReleaseInfo(source);
        }

        @Override
        public AppReleaseInfo[] newArray(int size) {
            return new AppReleaseInfo[size];
        }
    };
}
