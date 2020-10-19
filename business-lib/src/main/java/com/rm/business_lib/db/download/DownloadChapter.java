package com.rm.business_lib.db.download;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * desc   :
 * date   : 2020/10/17
 * version: 1.0
 */
@Entity
public class DownloadChapter {
    @Id
    private Long chapter_id;
    private Long audio_id ;
    private int  sequence;
    private String chapter_name;
    private Long size ; //音频大小
    private int duration;
    private int need_pay;
    private int amount;
    private String play_count;
    private String created_at;
    private String file_path;
    private String path_url;
    @Generated(hash = 1286659517)
    public DownloadChapter(Long chapter_id, Long audio_id, int sequence,
            String chapter_name, Long size, int duration, int need_pay, int amount,
            String play_count, String created_at, String file_path,
            String path_url) {
        this.chapter_id = chapter_id;
        this.audio_id = audio_id;
        this.sequence = sequence;
        this.chapter_name = chapter_name;
        this.size = size;
        this.duration = duration;
        this.need_pay = need_pay;
        this.amount = amount;
        this.play_count = play_count;
        this.created_at = created_at;
        this.file_path = file_path;
        this.path_url = path_url;
    }
    @Generated(hash = 1634822403)
    public DownloadChapter() {
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
    public int getSequence() {
        return this.sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    public String getChapter_name() {
        return this.chapter_name;
    }
    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }
    public Long getSize() {
        return this.size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getNeed_pay() {
        return this.need_pay;
    }
    public void setNeed_pay(int need_pay) {
        this.need_pay = need_pay;
    }
    public int getAmount() {
        return this.amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getPlay_count() {
        return this.play_count;
    }
    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }
    public String getCreated_at() {
        return this.created_at;
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
        return this.path_url;
    }
    public void setPath_url(String path_url) {
        this.path_url = path_url;
    }

}
