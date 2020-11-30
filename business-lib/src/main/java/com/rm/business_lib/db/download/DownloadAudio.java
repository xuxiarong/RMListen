package com.rm.business_lib.db.download;


import com.rm.business_lib.bean.Anchor;
import com.rm.business_lib.bean.DetailTags;
import com.rm.business_lib.db.DaoSession;
import com.rm.business_lib.db.DownloadAudioDao;
import com.rm.business_lib.db.DownloadChapterDao;
import com.rm.business_lib.db.converter.AnchorConvert;
import com.rm.business_lib.db.converter.DetailTagsConvert;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;

/**
 * desc   :
 * date   : 2020/10/17
 * version: 1.0
 */
@Entity
public class DownloadAudio implements Serializable {
    private static final long serialVersionUID = 1723090457747853170L;
    @Id
    private Long audio_id;
    private String audio_type;
    private String audio_name;
    private String original_name;
    private String status;
    private String author_intro;
    private String anchor_id;
    private String short_intro;
    private String audio_intro;
    private String audio_cover;
    private String cover_url;
    private String audio_label;
    private String quality;
    private int progress;
    private String play_count;
    private String created_at;
    private String chapter_updated_at;
    private String author;
    private String member_id;
    private String nickname;
    private String subscription_count;
    private String last_sequence;
    private int count_sequence;
    private String audio_cover_url;
    private String anchor_name;
    private long updateMillis;
    private String sortType;
    private String listenChapterId;
    @Convert(columnType = String.class, converter = AnchorConvert.class)
    private Anchor anchor;
    @Convert(columnType = String.class, converter = DetailTagsConvert.class)
    private List<DetailTags> tags;
    private boolean is_subscribe = false;
    private boolean is_fav = false;
    private int download_num;
    private long down_size;
    private boolean edit_select;
    private boolean listen_finish;

    @ToMany(referencedJoinProperty = "audio_id")
    private List<DownloadChapter> chapterList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 265156160)
    private transient DownloadAudioDao myDao;

    @Generated(hash = 697042537)
    public DownloadAudio(Long audio_id, String audio_type, String audio_name,
            String original_name, String status, String author_intro,
            String anchor_id, String short_intro, String audio_intro,
            String audio_cover, String cover_url, String audio_label,
            String quality, int progress, String play_count, String created_at,
            String chapter_updated_at, String author, String member_id,
            String nickname, String subscription_count, String last_sequence,
            int count_sequence, String audio_cover_url, String anchor_name,
            long updateMillis, String sortType, String listenChapterId,
            Anchor anchor, List<DetailTags> tags, boolean is_subscribe,
            boolean is_fav, int download_num, long down_size, boolean edit_select,
            boolean listen_finish) {
        this.audio_id = audio_id;
        this.audio_type = audio_type;
        this.audio_name = audio_name;
        this.original_name = original_name;
        this.status = status;
        this.author_intro = author_intro;
        this.anchor_id = anchor_id;
        this.short_intro = short_intro;
        this.audio_intro = audio_intro;
        this.audio_cover = audio_cover;
        this.cover_url = cover_url;
        this.audio_label = audio_label;
        this.quality = quality;
        this.progress = progress;
        this.play_count = play_count;
        this.created_at = created_at;
        this.chapter_updated_at = chapter_updated_at;
        this.author = author;
        this.member_id = member_id;
        this.nickname = nickname;
        this.subscription_count = subscription_count;
        this.last_sequence = last_sequence;
        this.count_sequence = count_sequence;
        this.audio_cover_url = audio_cover_url;
        this.anchor_name = anchor_name;
        this.updateMillis = updateMillis;
        this.sortType = sortType;
        this.listenChapterId = listenChapterId;
        this.anchor = anchor;
        this.tags = tags;
        this.is_subscribe = is_subscribe;
        this.is_fav = is_fav;
        this.download_num = download_num;
        this.down_size = down_size;
        this.edit_select = edit_select;
        this.listen_finish = listen_finish;
    }

    @Generated(hash = 1552335181)
    public DownloadAudio() {
    }

    public Long getAudio_id() {
        return this.audio_id;
    }

    public void setAudio_id(Long audio_id) {
        this.audio_id = audio_id;
    }

    public String getAudio_type() {
        return this.audio_type;
    }

    public void setAudio_type(String audio_type) {
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCover_url() {
        return this.cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
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

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMember_id() {
        return this.member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public int getCount_sequence() {
        return this.count_sequence;
    }

    public void setCount_sequence(int count_sequence) {
        this.count_sequence = count_sequence;
    }

    public String getAudio_cover_url() {
        return this.audio_cover_url;
    }

    public void setAudio_cover_url(String audio_cover_url) {
        this.audio_cover_url = audio_cover_url;
    }

    public String getAnchor_name() {
        return this.anchor_name;
    }

    public void setAnchor_name(String anchor_name) {
        this.anchor_name = anchor_name;
    }

    public long getUpdateMillis() {
        return this.updateMillis;
    }

    public void setUpdateMillis(long updateMillis) {
        this.updateMillis = updateMillis;
    }

    public String getSortType() {
        return this.sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getListenChapterId() {
        return this.listenChapterId;
    }

    public void setListenChapterId(String listenChapterId) {
        this.listenChapterId = listenChapterId;
    }

    public Anchor getAnchor() {
        return this.anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public List<DetailTags> getTags() {
        return this.tags;
    }

    public void setTags(List<DetailTags> tags) {
        this.tags = tags;
    }

    public boolean getIs_subscribe() {
        return this.is_subscribe;
    }

    public void setIs_subscribe(boolean is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public boolean getIs_fav() {
        return this.is_fav;
    }

    public void setIs_fav(boolean is_fav) {
        this.is_fav = is_fav;
    }

    public int getDownload_num() {
        return this.download_num;
    }

    public void setDownload_num(int download_num) {
        this.download_num = download_num;
    }

    public long getDown_size() {
        return this.down_size;
    }

    public void setDown_size(long down_size) {
        this.down_size = down_size;
    }

    public boolean getEdit_select() {
        return this.edit_select;
    }

    public void setEdit_select(boolean edit_select) {
        this.edit_select = edit_select;
    }

    public boolean getListen_finish() {
        return this.listen_finish;
    }

    public void setListen_finish(boolean listen_finish) {
        this.listen_finish = listen_finish;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 267201260)
    public List<DownloadChapter> getChapterList() {
        if (chapterList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DownloadChapterDao targetDao = daoSession.getDownloadChapterDao();
            List<DownloadChapter> chapterListNew = targetDao
                    ._queryDownloadAudio_ChapterList(audio_id);
            synchronized (this) {
                if (chapterList == null) {
                    chapterList = chapterListNew;
                }
            }
        }
        return chapterList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1743307878)
    public synchronized void resetChapterList() {
        chapterList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 584871444)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDownloadAudioDao() : null;
    }

}
