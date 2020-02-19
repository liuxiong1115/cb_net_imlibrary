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
     * content : {"scStatused":0,"businessCardEntity":{"uin":"2015606831","contactFlag":"0","alias":"","antispamticket":"v2_63cfdfc355b26b7e40afe805ed05113940758ce40794178116e6226447de09255f693d4553ae5b4bd80728d760c64d5b401788bf64477354bd3dec0b36e09727a8672fca04e81fc4e1f1eae79283c72b48545e02e8b68030ecb2a53046b9c73793fe9bd9134f5accd8e9d165fc093eb22bdb791d8979de10b4f3edd6f699a605965c9a3e5c598d9b6f0d2d1ad6b72a01e2c52e6038b51c468c26281c59d48830115c6c2175a65951956843664a1989cce2c9931e387b16037ab820cfa62a1ec2258313bd618b2ac444b51397f1435a28@stranger","bigheadimgurl":"http://wx.qlogo.cn/mmhead/ver_1/2gwicia8zoSeY5sJswGrlvjTV0fA7EBibVib2z86Cee2bMwxEibyAHOTWrLtauM7EDT8qic44fiaCGXuthy4pNJuPdiaMIvPBf5sz2ibGg7cETJJkobc/0","imagestatus":"3","nickname":"Jenny","province":"","regionCode":"CN","scene":"17","sex":"2","smallheadimgurl":"http://wx.qlogo.cn/mmhead/ver_1/2gwicia8zoSeY5sJswGrlvjTV0fA7EBibVib2z86Cee2bMwxEibyAHOTWrLtauM7EDT8qic44fiaCGXuthy4pNJuPdiaMIvPBf5sz2ibGg7cETJJkobc/132","username":"v1_067b110a215198b2ed4dd1b908624870a74369d24da5595dc557549d645227c68675aafecc316fd482066e65da8a4f20@stranger","wechatUserName":"wxid_t9bw330z0dou22"}}
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
         * businessCardEntity : {"uin":"2015606831","contactFlag":"0","alias":"","antispamticket":"v2_63cfdfc355b26b7e40afe805ed05113940758ce40794178116e6226447de09255f693d4553ae5b4bd80728d760c64d5b401788bf64477354bd3dec0b36e09727a8672fca04e81fc4e1f1eae79283c72b48545e02e8b68030ecb2a53046b9c73793fe9bd9134f5accd8e9d165fc093eb22bdb791d8979de10b4f3edd6f699a605965c9a3e5c598d9b6f0d2d1ad6b72a01e2c52e6038b51c468c26281c59d48830115c6c2175a65951956843664a1989cce2c9931e387b16037ab820cfa62a1ec2258313bd618b2ac444b51397f1435a28@stranger","bigheadimgurl":"http://wx.qlogo.cn/mmhead/ver_1/2gwicia8zoSeY5sJswGrlvjTV0fA7EBibVib2z86Cee2bMwxEibyAHOTWrLtauM7EDT8qic44fiaCGXuthy4pNJuPdiaMIvPBf5sz2ibGg7cETJJkobc/0","imagestatus":"3","nickname":"Jenny","province":"","regionCode":"CN","scene":"17","sex":"2","smallheadimgurl":"http://wx.qlogo.cn/mmhead/ver_1/2gwicia8zoSeY5sJswGrlvjTV0fA7EBibVib2z86Cee2bMwxEibyAHOTWrLtauM7EDT8qic44fiaCGXuthy4pNJuPdiaMIvPBf5sz2ibGg7cETJJkobc/132","username":"v1_067b110a215198b2ed4dd1b908624870a74369d24da5595dc557549d645227c68675aafecc316fd482066e65da8a4f20@stranger","wechatUserName":"wxid_t9bw330z0dou22"}
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
             * uin : 2015606831
             * contactFlag : 0
             * alias :
             * antispamticket : v2_63cfdfc355b26b7e40afe805ed05113940758ce40794178116e6226447de09255f693d4553ae5b4bd80728d760c64d5b401788bf64477354bd3dec0b36e09727a8672fca04e81fc4e1f1eae79283c72b48545e02e8b68030ecb2a53046b9c73793fe9bd9134f5accd8e9d165fc093eb22bdb791d8979de10b4f3edd6f699a605965c9a3e5c598d9b6f0d2d1ad6b72a01e2c52e6038b51c468c26281c59d48830115c6c2175a65951956843664a1989cce2c9931e387b16037ab820cfa62a1ec2258313bd618b2ac444b51397f1435a28@stranger
             * bigheadimgurl : http://wx.qlogo.cn/mmhead/ver_1/2gwicia8zoSeY5sJswGrlvjTV0fA7EBibVib2z86Cee2bMwxEibyAHOTWrLtauM7EDT8qic44fiaCGXuthy4pNJuPdiaMIvPBf5sz2ibGg7cETJJkobc/0
             * imagestatus : 3
             * nickname : Jenny
             * province :
             * regionCode : CN
             * scene : 17
             * sex : 2
             * smallheadimgurl : http://wx.qlogo.cn/mmhead/ver_1/2gwicia8zoSeY5sJswGrlvjTV0fA7EBibVib2z86Cee2bMwxEibyAHOTWrLtauM7EDT8qic44fiaCGXuthy4pNJuPdiaMIvPBf5sz2ibGg7cETJJkobc/132
             * username : v1_067b110a215198b2ed4dd1b908624870a74369d24da5595dc557549d645227c68675aafecc316fd482066e65da8a4f20@stranger
             * wechatUserName : wxid_t9bw330z0dou22
             */

            private String uin;
            private String contactFlag;
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
            private String wechatUserName;

            public String getUin() {
                return uin;
            }

            public void setUin(String uin) {
                this.uin = uin;
            }

            public String getContactFlag() {
                return contactFlag;
            }

            public void setContactFlag(String contactFlag) {
                this.contactFlag = contactFlag;
            }

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

            public String getWechatUserName() {
                return wechatUserName;
            }

            public void setWechatUserName(String wechatUserName) {
                this.wechatUserName = wechatUserName;
            }
        }
    }
}
