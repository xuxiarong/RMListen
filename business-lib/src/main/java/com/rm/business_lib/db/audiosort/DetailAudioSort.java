package com.rm.business_lib.db.audiosort;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author yuanfang
 * @date 12/14/20
 * @description
 */
@Entity
public class DetailAudioSort implements Serializable {
    private static final long serialVersionUID = 2026204024407173357L;
    @Id
    private Long audio_id;
    private String sort;
    @Generated(hash = 210298341)
    public DetailAudioSort(Long audio_id, String sort) {
        this.audio_id = audio_id;
        this.sort = sort;
    }
    @Generated(hash = 1551858593)
    public DetailAudioSort() {
    }
    public Long getAudio_id() {
        return this.audio_id;
    }
    public void setAudio_id(Long audio_id) {
        this.audio_id = audio_id;
    }
    public String getSort() {
        return this.sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
}
