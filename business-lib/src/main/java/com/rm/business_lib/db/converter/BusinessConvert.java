package com.rm.business_lib.db.converter;

import com.rm.business_lib.db.download.DownloadAudio;
import com.rm.business_lib.db.download.DownloadChapter;
import com.rm.business_lib.db.listen.ListenAudioEntity;
import com.rm.business_lib.db.listen.ListenChapterEntity;

/**
 * desc   :
 * date   : 2020/11/11
 * version: 1.0
 */
public class BusinessConvert {

    public static DownloadAudio convertToDownloadAudio(ListenAudioEntity listenAudio) {
        return new DownloadAudio(
                listenAudio.getAudio_id(),
                listenAudio.getAudio_type(),
                listenAudio.getAudio_name(),
                listenAudio.getOriginal_name(),
                listenAudio.getStatus(),
                listenAudio.getAuthor_intro(),
                listenAudio.getAnchor_id(),
                listenAudio.getShort_intro(),
                listenAudio.getAudio_intro(),
                listenAudio.getAudio_cover(),
                listenAudio.getCover_url(),
                listenAudio.getAudio_label(),
                listenAudio.getQuality(),
                listenAudio.getProgress(),
                listenAudio.getPlay_count(),
                listenAudio.getCreated_at(),
                listenAudio.getChapter_updated_at(),
                listenAudio.getAuthor(),
                listenAudio.getMember_id(),
                listenAudio.getNickname(),
                listenAudio.getSubscription_count(),
                listenAudio.getLast_sequence(),
                listenAudio.getAudio_cover_url(),
                listenAudio.getAnchor(),
                listenAudio.getTags(),
                listenAudio.getIs_subscribe(),
                listenAudio.getIs_fav(),
                listenAudio.getDownload_num(),
                listenAudio.getDown_size(),
                listenAudio.getEdit_select(),
                listenAudio.getListen_finish()
        );
    }

    public static ListenAudioEntity convertToListenAudio(DownloadAudio downloadAudio) {
        return new ListenAudioEntity(
                downloadAudio.getAudio_id(),
                downloadAudio.getAudio_type(),
                downloadAudio.getAudio_name(),
                downloadAudio.getOriginal_name(),
                downloadAudio.getStatus(),
                downloadAudio.getAuthor_intro(),
                downloadAudio.getAnchor_id(),
                downloadAudio.getShort_intro(),
                downloadAudio.getAudio_intro(),
                downloadAudio.getAudio_cover(),
                downloadAudio.getCover_url(),
                downloadAudio.getAudio_label(),
                downloadAudio.getQuality(),
                downloadAudio.getProgress(),
                downloadAudio.getPlay_count(),
                downloadAudio.getCreated_at(),
                downloadAudio.getChapter_updated_at(),
                downloadAudio.getAuthor(),
                downloadAudio.getMember_id(),
                downloadAudio.getNickname(),
                downloadAudio.getSubscription_count(),
                downloadAudio.getLast_sequence(),
                downloadAudio.getAudio_cover_url(),
                downloadAudio.getAnchor(),
                downloadAudio.getTags(),
                downloadAudio.getIs_subscribe(),
                downloadAudio.getIs_fav(),
                downloadAudio.getDownload_num(),
                downloadAudio.getDown_size(),
                downloadAudio.getEdit_select(),
                downloadAudio.getListen_finish()
        );
    }

    public static DownloadChapter convertToDownloadChapter(ListenChapterEntity listenChapter) {
        return new DownloadChapter(
                listenChapter.getChapter_id(),
                listenChapter.getAudio_id(),
                listenChapter.getAudio_name(),
                listenChapter.getSequence(),
                listenChapter.getChapter_name(),
                listenChapter.getSize(),
                listenChapter.getDown_speed(),
                listenChapter.getCurrent_offset(),
                listenChapter.getDuration(),
                listenChapter.getNeed_pay(),
                listenChapter.getAmount(),
                listenChapter.getPlay_count(),
                listenChapter.getCreated_at(),
                listenChapter.getFile_path(),
                listenChapter.getPath_url(),
                listenChapter.getDown_status(),
                listenChapter.getListen_duration(),
                listenChapter.getChapter_edit_select(),
                listenChapter.getDown_edit_select()
        );
    }

    public static ListenChapterEntity convertToListenChapter(DownloadChapter downloadChapter) {
        return new ListenChapterEntity(
                downloadChapter.getChapter_id(),
                downloadChapter.getAudio_id(),
                downloadChapter.getAudio_name(),
                downloadChapter.getSequence(),
                downloadChapter.getChapter_name(),
                downloadChapter.getSize(),
                downloadChapter.getDown_speed(),
                downloadChapter.getCurrent_offset(),
                downloadChapter.getDuration(),
                downloadChapter.getNeed_pay(),
                downloadChapter.getAmount(),
                downloadChapter.getPlay_count(),
                downloadChapter.getCreated_at(),
                downloadChapter.getFile_path(),
                downloadChapter.getPath_url(),
                downloadChapter.getDown_status(),
                downloadChapter.getListen_duration(),
                downloadChapter.getChapter_edit_select(),
                downloadChapter.getDown_edit_select()
        );
    }


}
