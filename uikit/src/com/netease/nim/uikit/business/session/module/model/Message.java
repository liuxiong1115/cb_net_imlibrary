
package com.netease.nim.uikit.business.session.module.model;
import java.io.Serializable;

/**
 * Created by mike on 2019/9/9.
 */

public class Message implements Serializable {

    /**
     * attachStatus : def
     * attachment : {"catalog":"lt","chartlet":"lt005","type":3}
     * config : {"enableHistory":true,"enablePersist":true,"enablePush":true,"enablePushNick":true,"enableRoaming":true,"enableRoute":true,"enableSelfSync":true,"enableUnreadCount":true}
     * content : 贴图消息
     * direct : In
     * fromAccount : teac125
     * fromClientType : 1
     * fromNick : sherry
     * memberPushOption : {"forcePush":false}
     * msgType : custom
     * pushContent :
     * pushPayload : {"sessionID":"2676424627","sessionType":1}
     * remoteRead : false
     * sessionId : 2676424627
     * sessionType : Team
     * status : success
     * teamMsgAckCount : 0
     * teamMsgUnAckCount : 0
     * time : 1567760247453
     * uuid : 0aecbb4691044757a20bcc9cb8f41cbd
     */

    private AttachmentBean attachment;
    private String content;
    private String direct;
    private String fromAccount;
    private String fromNick;
    private String msgType;
    private String pushContent;
    private boolean remoteRead;
    private String sessionId;
    private String sessionType;
    private String status;
    private int teamMsgAckCount;
    private int teamMsgUnAckCount;
    private long time;
    private String uuid;


    public AttachmentBean getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentBean attachment) {
        this.attachment = attachment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getFromNick() {
        return fromNick;
    }

    public void setFromNick(String fromNick) {
        this.fromNick = fromNick;
    }



    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }


    public boolean isRemoteRead() {
        return remoteRead;
    }

    public void setRemoteRead(boolean remoteRead) {
        this.remoteRead = remoteRead;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTeamMsgAckCount() {
        return teamMsgAckCount;
    }

    public void setTeamMsgAckCount(int teamMsgAckCount) {
        this.teamMsgAckCount = teamMsgAckCount;
    }

    public int getTeamMsgUnAckCount() {
        return teamMsgUnAckCount;
    }

    public void setTeamMsgUnAckCount(int teamMsgUnAckCount) {
        this.teamMsgUnAckCount = teamMsgUnAckCount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public static class AttachmentBean implements Serializable{
        /**
         * catalog : lt
         * chartlet : lt005
         * type : 3
         */

        /**
         * displayName : 5、 教育教学社会调查（问卷一）.doc
         * extension : doc
         * fileName : d0c967dcd5d6d0c670259cdb522f2ad0
         * forceUpload : false
         * md5 : d0c967dcd5d6d0c670259cdb522f2ad0
         * nosTokenSceneKey : nim_default_im
         * originalUrl : https://nim.nosdn.127.net/NTM2MjYyMQ==/bmltYV82ODUxNTg4MDc3XzE1Njc2NjcyNTA3NTBfNzA2ZTJiZTItOGM3ZC00NmQ4LWE1MWQtNzEzNmUxNmM2NTVm
         * pathForSave : /storage/emulated/0/Android/data/com.testlib.netease/cache/nim/file/d0c967dcd5d6d0c670259cdb522f2ad0
         * size : 21504
         * thumbPathForSave : /storage/emulated/0/Android/data/com.testlib.netease/cache/nim/thumb/d0c967dcd5d6d0c670259cdb522f2ad0
         * url : https://nim.nosdn.127.net/NTM2MjYyMQ==/bmltYV82ODUxNTg4MDc3XzE1Njc2NjcyNTA3NTBfNzA2ZTJiZTItOGM3ZC00NmQ4LWE1MWQtNzEzNmUxNmM2NTVm
         */


        private String catalog;
        private String chartlet;
        private int type;

        private String displayName;
        private String extension;
        private String fileName;
        private boolean forceUpload;
        private String md5;
        private String nosTokenSceneKey;
        private String originalUrl;
        private String pathForSave;
        private int size;
        private String thumbPathForSave;
        private String url;
        /**
         * duration : 5538
         * height : 720
         * thumbPath : /storage/emulated/0/Android/data/com.testlib.netease/cache/nim/thumb/1edbc463af12b396587b5c3c487f2f23
         * thumbUrl : https://nim.nosdn.127.net/NTM2MjYyMQ==/bmltYV82ODUxNTg4MDc3XzE1Njc2NjcyNTA3NTBfYWU2MzgwNWQtZTQ1Yy00Nzk2LTk0ZmMtZmQ5NzIyYzk2NjMy?vframe=1
         * width : 480
         */

        private int duration;
        private int height;
        private String thumbPath;
        private String thumbUrl;
        private int width;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        private String path;
        public String getCatalog() {
            return catalog;
        }

        public void setCatalog(String catalog) {
            this.catalog = catalog;
        }

        public String getChartlet() {
            return chartlet;
        }

        public void setChartlet(String chartlet) {
            this.chartlet = chartlet;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public boolean isForceUpload() {
            return forceUpload;
        }

        public void setForceUpload(boolean forceUpload) {
            this.forceUpload = forceUpload;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getNosTokenSceneKey() {
            return nosTokenSceneKey;
        }

        public void setNosTokenSceneKey(String nosTokenSceneKey) {
            this.nosTokenSceneKey = nosTokenSceneKey;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }

        public String getPathForSave() {
            return pathForSave;
        }

        public void setPathForSave(String pathForSave) {
            this.pathForSave = pathForSave;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getThumbPathForSave() {
            return thumbPathForSave;
        }

        public void setThumbPathForSave(String thumbPathForSave) {
            this.thumbPathForSave = thumbPathForSave;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getThumbPath() {
            return thumbPath;
        }

        public void setThumbPath(String thumbPath) {
            this.thumbPath = thumbPath;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}

