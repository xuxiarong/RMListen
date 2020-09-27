package com.rm.business_lib.db;

import com.rm.business_lib.bean.ChapterList;
import com.rm.business_lib.db.converter.ChapterListConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * @des:
 * @data: 9/27/20 12:22 PM
 * @Version: 1.0.0
 */
@Entity
public class HistoryPlayBook {


    @Id
    private Long audio_id;
    private int audio_type;
    private String audio_name;
    private String original_name;
    private String author;
    private String author_intro;
    private String anchor_id;
    private String short_intro;
    private String audio_intro;
    private String audio_cover;
    private String audio_label;
    private String quality;
    private int progress;
    private String play_count;
    private String subscription_count;
    private String last_sequence;
    private int status;
    private String created_at;
    private String chapter_updated_at;
    private String audio_cover_url;


    @Convert(columnType = String.class, converter = ChapterListConverter.class)
    private List<ChapterList> listBean; //实体类中list数据


    @Generated(hash = 2084409069)
    public HistoryPlayBook(Long audio_id, int audio_type, String audio_name,
            String original_name, String author, String author_intro,
            String anchor_id, String short_intro, String audio_intro,
            String audio_cover, String audio_label, String quality, int progress,
            String play_count, String subscription_count, String last_sequence,
            int status, String created_at, String chapter_updated_at,
            String audio_cover_url, List<ChapterList> listBean) {
        this.audio_id = audio_id;
        this.audio_type = audio_type;
        this.audio_name = audio_name;
        this.original_name = original_name;
        this.author = author;
        this.author_intro = author_intro;
        this.anchor_id = anchor_id;
        this.short_intro = short_intro;
        this.audio_intro = audio_intro;
        this.audio_cover = audio_cover;
        this.audio_label = audio_label;
        this.quality = quality;
        this.progress = progress;
        this.play_count = play_count;
        this.subscription_count = subscription_count;
        this.last_sequence = last_sequence;
        this.status = status;
        this.created_at = created_at;
        this.chapter_updated_at = chapter_updated_at;
        this.audio_cover_url = audio_cover_url;
        this.listBean = listBean;
    }

    @Generated(hash = 1164712371)
    public HistoryPlayBook() {
    }

    
    public Long getAudio_id() {
        return this.audio_id;
    }

    public void setAudio_id(Long audio_id) {
        this.audio_id = audio_id;
    }

    public int getAudio_type() {
        return this.audio_type;
    }

    public void setAudio_type(int audio_type) {
        this.audio_type = audio_type;
    }

    public String getAudio_name() {
        return this.audio_name;
    }

    public void setAudio_name(String audio_name) {
        this.audio_name = audio_name;
    }

    public String getOriginal_name() {
        return this.original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor_intro() {
        return this.author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public String getAnchor_id() {
        return this.anchor_id;
    }

    public void setAnchor_id(String anchor_id) {
        this.anchor_id = anchor_id;
    }

    public String getShort_intro() {
        return this.short_intro;
    }

    public void setShort_intro(String short_intro) {
        this.short_intro = short_intro;
    }

    public String getAudio_intro() {
        return this.audio_intro;
    }

    public void setAudio_intro(String audio_intro) {
        this.audio_intro = audio_intro;
    }

    public String getAudio_cover() {
        return this.audio_cover;
    }

    public void setAudio_cover(String audio_cover) {
        this.audio_cover = audio_cover;
    }

    public String getAudio_label() {
        return this.audio_label;
    }

    public void setAudio_label(String audio_label) {
        this.audio_label = audio_label;
    }

    public String getQuality() {
        return this.quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getPlay_count() {
        return this.play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public String getSubscription_count() {
        return this.subscription_count;
    }

    public void setSubscription_count(String subscription_count) {
        this.subscription_count = subscription_count;
    }

    public String getLast_sequence() {
        return this.last_sequence;
    }

    public void setLast_sequence(String last_sequence) {
        this.last_sequence = last_sequence;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getChapter_updated_at() {
        return this.chapter_updated_at;
    }

    public void setChapter_updated_at(String chapter_updated_at) {
        this.chapter_updated_at = chapter_updated_at;
    }

    public String getAudio_cover_url() {
        return this.audio_cover_url;
    }

    public void setAudio_cover_url(String audio_cover_url) {
        this.audio_cover_url = audio_cover_url;
    }

    public List<ChapterList> getListBean() {
        return this.listBean;
    }

    public void setListBean(List<ChapterList> beanList) {
        this.listBean = beanList;
    }

    public void addChaterBean(ChapterList listBean) {
        this.listBean.add(listBean);
    }

}
