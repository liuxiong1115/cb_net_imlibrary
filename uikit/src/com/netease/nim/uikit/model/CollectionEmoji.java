package com.netease.nim.uikit.model;

import java.util.List;

/**
 * Created by mike on 2020/3/2.
 */

public class CollectionEmoji {

    /**
     * body : {"totalCount":3,"pageSize":10,"totalPage":1,"currPage":1,"list":[{"id":1,"emojiDesc":"1","emojiUrl":"https://classbro-oss.oss-accelerate.aliyuncs.com/statice-resource/20200224/b32f7a18640b41c0a9760a337e86593c{oss}微信图片_20200221141525.png","userAccount":"crm72","createTime":"2020-02-26 19:02:36","updatedTime":"2020-02-26 19:02:36","deleteFlag":"0"},{"id":2,"emojiDesc":"厉害","emojiUrl":"https://classbro-oss.oss-accelerate.aliyuncs.com/statice-resource/20200224/b32f7a18640b41c0a9760a337e86593c{oss}微信图片_20200221141525.png","userAccount":"crm72","createTime":"2020-02-26 19:02:53","updatedTime":"2020-02-26 19:02:53","deleteFlag":"0"},{"id":3,"emojiDesc":"哈哈哈","emojiUrl":"https://classbro-oss.oss-accelerate.aliyuncs.com/statice-resource/20200224/b32f7a18640b41c0a9760a337e86593c{oss}微信图片_20200221141525.png","userAccount":"crm72","createTime":"2020-02-26 19:02:57","updatedTime":"2020-02-26 19:02:57","deleteFlag":"0"}]}
     * status : 200
     */

    private BodyBean body;
    private int status;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class BodyBean {
        /**
         * totalCount : 3
         * pageSize : 10
         * totalPage : 1
         * currPage : 1
         * list : [{"id":1,"emojiDesc":"1","emojiUrl":"https://classbro-oss.oss-accelerate.aliyuncs.com/statice-resource/20200224/b32f7a18640b41c0a9760a337e86593c{oss}微信图片_20200221141525.png","userAccount":"crm72","createTime":"2020-02-26 19:02:36","updatedTime":"2020-02-26 19:02:36","deleteFlag":"0"},{"id":2,"emojiDesc":"厉害","emojiUrl":"https://classbro-oss.oss-accelerate.aliyuncs.com/statice-resource/20200224/b32f7a18640b41c0a9760a337e86593c{oss}微信图片_20200221141525.png","userAccount":"crm72","createTime":"2020-02-26 19:02:53","updatedTime":"2020-02-26 19:02:53","deleteFlag":"0"},{"id":3,"emojiDesc":"哈哈哈","emojiUrl":"https://classbro-oss.oss-accelerate.aliyuncs.com/statice-resource/20200224/b32f7a18640b41c0a9760a337e86593c{oss}微信图片_20200221141525.png","userAccount":"crm72","createTime":"2020-02-26 19:02:57","updatedTime":"2020-02-26 19:02:57","deleteFlag":"0"}]
         */

        private int totalCount;
        private int pageSize;
        private int totalPage;
        private int currPage;
        private List<ListBean> list;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getCurrPage() {
            return currPage;
        }

        public void setCurrPage(int currPage) {
            this.currPage = currPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1
             * emojiDesc : 1
             * emojiUrl : https://classbro-oss.oss-accelerate.aliyuncs.com/statice-resource/20200224/b32f7a18640b41c0a9760a337e86593c{oss}微信图片_20200221141525.png
             * userAccount : crm72
             * createTime : 2020-02-26 19:02:36
             * updatedTime : 2020-02-26 19:02:36
             * deleteFlag : 0
             */

            private int id;
            private String emojiDesc;
            private String emojiUrl;
            private String userAccount;
            private String createTime;
            private String updatedTime;
            private String deleteFlag;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getEmojiDesc() {
                return emojiDesc;
            }

            public void setEmojiDesc(String emojiDesc) {
                this.emojiDesc = emojiDesc;
            }

            public String getEmojiUrl() {
                return emojiUrl;
            }

            public void setEmojiUrl(String emojiUrl) {
                this.emojiUrl = emojiUrl;
            }

            public String getUserAccount() {
                return userAccount;
            }

            public void setUserAccount(String userAccount) {
                this.userAccount = userAccount;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdatedTime() {
                return updatedTime;
            }

            public void setUpdatedTime(String updatedTime) {
                this.updatedTime = updatedTime;
            }

            public String getDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(String deleteFlag) {
                this.deleteFlag = deleteFlag;
            }
        }
    }
}
