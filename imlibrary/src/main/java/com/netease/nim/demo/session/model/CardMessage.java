package com.netease.nim.demo.session.model;

/**
 * Created by mike on 2020/2/19.
 */

public class CardMessage {


    /**
     * messageId : 0
     * title : 名片消息
     * subtitle : 名片消息
     * type : 801
     * content : {"scStatused":0,"businessCardEntity":{"alias":"","antispamticket":"v2_cfa4e7040033914cfbd6e440284a4713818996c6124066773035fe733bbf14aaa48c54587b61d2317e14c9a9b6723e89c65db955e9a19fb1d9669829e449d392cebe53d4c8d09b90d4e70e0cf4dd8e8e5b8f583508397db0e5ea0098687108e227e7c78e411302570428c15f533be06378acc159a81941bf3bb73e26d28390437c6cc0fb8020bf1a9bfa0afc0a5795d0be915ab454cd23e6b28b0c6db2a03173fbcef3b888093e79e1fd7bca5911a791d55fb7bba30f054ab77a4877f990c052@stranger","bigheadimgurl":"http://wx.qlogo.cn/mmhead/ver_1/mxQ6DZA2CO4NNnh4kqNrkpZ9ZnH0RN0ia9GgPgON9v9gqzZ3CfrRqXgzCYxZB825a9MPGtiaa7qvia64liahWtH2HkwyiaSpqClAW9gRlwIvuTyQ/0","imagestatus":"3","nickname":"-_-","province":"","regionCode":"CN","scene":"17","sex":"1","smallheadimgurl":"http://wx.qlogo.cn/mmhead/ver_1/mxQ6DZA2CO4NNnh4kqNrkpZ9ZnH0RN0ia9GgPgON9v9gqzZ3CfrRqXgzCYxZB825a9MPGtiaa7qvia64liahWtH2HkwyiaSpqClAW9gRlwIvuTyQ/132","username":"v1_5b8227fc7a110070013c77ee0917f8e301b2bbbd3c5de260a6f549895aa756dc@stranger"}}
     * picType : 0
     */

    private int messageId;
    private String title;
    private String subtitle;
    private int type;
    private ContentBean content;
    private int picType;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getPicType() {
        return picType;
    }

    public void setPicType(int picType) {
        this.picType = picType;
    }

    public static class ContentBean {
        /**
         * scStatused : 0
         * businessCardEntity : {"alias":"","antispamticket":"v2_cfa4e7040033914cfbd6e440284a4713818996c6124066773035fe733bbf14aaa48c54587b61d2317e14c9a9b6723e89c65db955e9a19fb1d9669829e449d392cebe53d4c8d09b90d4e70e0cf4dd8e8e5b8f583508397db0e5ea0098687108e227e7c78e411302570428c15f533be06378acc159a81941bf3bb73e26d28390437c6cc0fb8020bf1a9bfa0afc0a5795d0be915ab454cd23e6b28b0c6db2a03173fbcef3b888093e79e1fd7bca5911a791d55fb7bba30f054ab77a4877f990c052@stranger","bigheadimgurl":"http://wx.qlogo.cn/mmhead/ver_1/mxQ6DZA2CO4NNnh4kqNrkpZ9ZnH0RN0ia9GgPgON9v9gqzZ3CfrRqXgzCYxZB825a9MPGtiaa7qvia64liahWtH2HkwyiaSpqClAW9gRlwIvuTyQ/0","imagestatus":"3","nickname":"-_-","province":"","regionCode":"CN","scene":"17","sex":"1","smallheadimgurl":"http://wx.qlogo.cn/mmhead/ver_1/mxQ6DZA2CO4NNnh4kqNrkpZ9ZnH0RN0ia9GgPgON9v9gqzZ3CfrRqXgzCYxZB825a9MPGtiaa7qvia64liahWtH2HkwyiaSpqClAW9gRlwIvuTyQ/132","username":"v1_5b8227fc7a110070013c77ee0917f8e301b2bbbd3c5de260a6f549895aa756dc@stranger"}
         */

        private int scStatused;
        private BusinessCardEntityBean businessCardEntity;

        public int getScStatused() {
            return scStatused;
        }

        public void setScStatused(int scStatused) {
            this.scStatused = scStatused;
        }

        public BusinessCardEntityBean getBusinessCardEntity() {
            return businessCardEntity;
        }

        public void setBusinessCardEntity(BusinessCardEntityBean businessCardEntity) {
            this.businessCardEntity = businessCardEntity;
        }

        public static class BusinessCardEntityBean {
            /**
             * alias :
             * antispamticket : v2_cfa4e7040033914cfbd6e440284a4713818996c6124066773035fe733bbf14aaa48c54587b61d2317e14c9a9b6723e89c65db955e9a19fb1d9669829e449d392cebe53d4c8d09b90d4e70e0cf4dd8e8e5b8f583508397db0e5ea0098687108e227e7c78e411302570428c15f533be06378acc159a81941bf3bb73e26d28390437c6cc0fb8020bf1a9bfa0afc0a5795d0be915ab454cd23e6b28b0c6db2a03173fbcef3b888093e79e1fd7bca5911a791d55fb7bba30f054ab77a4877f990c052@stranger
             * bigheadimgurl : http://wx.qlogo.cn/mmhead/ver_1/mxQ6DZA2CO4NNnh4kqNrkpZ9ZnH0RN0ia9GgPgON9v9gqzZ3CfrRqXgzCYxZB825a9MPGtiaa7qvia64liahWtH2HkwyiaSpqClAW9gRlwIvuTyQ/0
             * imagestatus : 3
             * nickname : -_-
             * province :
             * regionCode : CN
             * scene : 17
             * sex : 1
             * smallheadimgurl : http://wx.qlogo.cn/mmhead/ver_1/mxQ6DZA2CO4NNnh4kqNrkpZ9ZnH0RN0ia9GgPgON9v9gqzZ3CfrRqXgzCYxZB825a9MPGtiaa7qvia64liahWtH2HkwyiaSpqClAW9gRlwIvuTyQ/132
             * username : v1_5b8227fc7a110070013c77ee0917f8e301b2bbbd3c5de260a6f549895aa756dc@stranger
             */

            private String alias;
            private String antispamticket;
            private String bigheadimgurl;
            private String imagestatus;
            private String nickname;
            private String province;
            private String regionCode;
            private String scene;
            private String sex;
            private String smallheadimgurl;
            private String username;

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getAntispamticket() {
                return antispamticket;
            }

            public void setAntispamticket(String antispamticket) {
                this.antispamticket = antispamticket;
            }

            public String getBigheadimgurl() {
                return bigheadimgurl;
            }

            public void setBigheadimgurl(String bigheadimgurl) {
                this.bigheadimgurl = bigheadimgurl;
            }

            public String getImagestatus() {
                return imagestatus;
            }

            public void setImagestatus(String imagestatus) {
                this.imagestatus = imagestatus;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getRegionCode() {
                return regionCode;
            }

            public void setRegionCode(String regionCode) {
                this.regionCode = regionCode;
            }

            public String getScene() {
                return scene;
            }

            public void setScene(String scene) {
                this.scene = scene;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getSmallheadimgurl() {
                return smallheadimgurl;
            }

            public void setSmallheadimgurl(String smallheadimgurl) {
                this.smallheadimgurl = smallheadimgurl;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}
