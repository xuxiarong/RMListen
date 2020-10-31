package com.rm.business_lib.db.download;

import com.rm.business_lib.db.DaoSession;
import com.rm.business_lib.db.DownloadAudioDao;
import com.rm.business_lib.db.DownloadChapterDao;

import org.greenrobot.greendao.DaoException;
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
    private String audio_name;
    private String author;
    private String audio_cover_url;
    private int status;
    private int last_sequence;
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

    @Generated(hash = 990684679)
    public DownloadAudio(Long audio_id, String audio_name, String author, String audio_cover_url,
            int status, int last_sequence, int download_num, long down_size, boolean edit_select,
            boolean listen_finish) {
        this.audio_id = audio_id;
        this.audio_name = audio_name;
        this.author = author;
        this.audio_cover_url = audio_cover_url;
        this.status = status;
        this.last_sequence = last_sequence;
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

    public String getAudio_name() {
        return this.audio_name == null ?"":this.audio_name;
    }

    public void setAudio_name(String audio_name) {
        this.audio_name = audio_name;
    }

    public String getAuthor() {
        return this.author == null ? "" : this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAudio_cover_url() {
        return this.audio_cover_url == null ?"" :  this.audio_cover_url;
    }

    public void setAudio_cover_url(String audio_cover_url) {
        this.audio_cover_url = audio_cover_url;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLast_sequence() {
        return this.last_sequence;
    }

    public void setLast_sequence(int last_sequence) {
        this.last_sequence = last_sequence;
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

    public boolean getListen_finish() {
        return this.listen_finish;
    }

    public void setListen_finish(boolean listen_finish) {
        this.listen_finish = listen_finish;
    }




}
