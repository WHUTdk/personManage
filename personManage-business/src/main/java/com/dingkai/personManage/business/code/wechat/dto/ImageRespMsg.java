package com.dingkai.personManage.business.code.wechat.dto;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 16:25
 */
public class ImageRespMsg extends WechatRespMsgTemplate {
    private static final long serialVersionUID = -6215714304322928014L;

    private Image Image;

    public Image getImage() {
        return Image;
    }

    public void setImage(Image Image) {
        this.Image = Image;
    }

    public static class Image {
        private String MediaId;

        public String getMediaId() {
            return MediaId;
        }

        public void setMediaId(String mediaId) {
            MediaId = mediaId;
        }
    }
}
