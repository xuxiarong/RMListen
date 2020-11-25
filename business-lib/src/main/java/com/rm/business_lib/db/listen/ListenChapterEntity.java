package com.rm.business_lib.db.listen;

import com.rm.business_lib.download.DownloadConstant;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * desc   :
 * date   : 2020/11/10
 * version: 1.0
 */

@Entity
public class ListenChapterEntity {
    @Id
    private Long chapter_id;
    private Long audio_id;
    private String audio_name;
    private int sequence;
    private String chapter_name;
    private long size; //音频大小
    private String down_speed;
    private long current_offset;
    private long duration;
    private long realDuration;
    private int need_pay;
    private double amount;
    private String play_count;
    private String created_at;
    private String file_path;
    private String path_url;
    //下载的状态
    private int down_status;
    //收听的进度
    private long listen_duration;
    private long updateMillis;
    //章节是否被选中下载
    private boolean chapter_edit_select;
    // 处于下载队列是否被选中
    private boolean down_edit_select;

    @Generated(hash = 1158211552)
    public ListenChapterEntity(Long chapter_id, Long audio_id, String audio_name, int sequence,
            String chapter_name, long size, String down_speed, long current_offset, long duration,
            long realDuration, int need_pay, double amount, String play_count, String created_at,
            String file_path, String path_url, int down_status, long listen_duration, long updateMillis,
            boolean chapter_edit_select, boolean down_edit_select) {
        this.chapter_id = chapter_id;
        this.audio_id = audio_id;
        this.audio_name = audio_name;
        this.sequence = sequence;
        this.chapter_name = chapter_name;
        this.size = size;
        this.down_speed = down_speed;
        this.current_offset = current_offset;
        this.duration = duration;
        this.realDuration = realDuration;
        this.need_pay = need_pay;
        this.amount = amount;
        this.play_count = play_count;
        this.created_at = created_at;
        this.file_path = file_path;
        this.path_url = path_url;
        this.down_status = down_status;
        this.listen_duration = listen_duration;
        this.updateMillis = updateMillis;
        this.chapter_edit_select = chapter_edit_select;
        this.down_edit_select = down_edit_select;
    }

    @Generated(hash = 1253451852)
    public ListenChapterEntity() {
    }

    public Long getChapter_id() {
        return this.chapter_id;
    }

    public void setChapter_id(Long chapter_id) {
        this.chapter_id = chapter_id;
    }

    public Long getAudio_id() {
        return this.audio_id;
    }

    public void setAudio_id(Long audio_id) {
        this.audio_id = audio_id;
    }

    public String getAudio_name() {
        return this.audio_name == null ? "" : this.audio_name;
    }

    public void setAudio_name(String audio_name) {
        this.audio_name = audio_name;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getChapter_name() {
        return this.chapter_name == null ? "" : this.chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDown_speed() {
        return this.down_speed == null ? "" : this.down_speed;
    }

    public void setDown_speed(String down_speed) {
        this.down_speed = down_speed;
    }

    public long getCurrent_offset() {
        return this.current_offset;
    }

    public void setCurrent_offset(long current_offset) {
        this.current_offset = current_offset;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getNeed_pay() {
        return this.need_pay;
    }

    public void setNeed_pay(int need_pay) {
        this.need_pay = need_pay;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPlay_count() {
        return this.play_count == null ? "0" : this.play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public String getCreated_at() {
        return this.created_at == null ? "0" : this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getFile_path() {
        return this.file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getPath_url() {
        return this.path_url == null ? "" : this.path_url;
    }

    public void setPath_url(String path_url) {
        this.path_url = path_url;
    }

    public int getDown_status() {
        return this.down_status;
    }

    public void setDown_status(int down_status) {
        this.down_status = down_status;
    }

    public long getListen_duration() {
        return this.listen_duration;
    }

    public void setListen_duration(long listen_duration) {
        this.listen_duration = listen_duration;
    }

    public boolean getChapter_edit_select() {
        return this.chapter_edit_select;
    }

    public void setChapter_edit_select(boolean chapter_edit_select) {
        this.chapter_edit_select = chapter_edit_select;
    }

    public boolean getDown_edit_select() {
        return this.down_edit_select;
    }

    public void setDown_edit_select(boolean down_edit_select) {
        this.down_edit_select = down_edit_select;
    }

    public boolean isDownloading() {
        return down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOADING;
    }

    public boolean isDownloadFinish() {
        return down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH;
    }

    public boolean isDownWait() {
        return down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT;
    }

    public long getUpdateMillis() {
        return this.updateMillis;
    }

    public void setUpdateMillis(long updateMillis) {
        this.updateMillis = updateMillis;
    }

    public long getRealDuration() {
        return this.realDuration > 0?this.realDuration : this.duration * 1000;
    }

    public void setRealDuration(long realDuration) {
        this.realDuration = realDuration;
    }
}
