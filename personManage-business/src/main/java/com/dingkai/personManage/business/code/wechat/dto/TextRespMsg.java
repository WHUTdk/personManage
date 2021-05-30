package com.dingkai.personManage.business.code.wechat.dto;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 16:22
 */
public class TextRespMsg extends WechatRespMsgTemplate{
    private static final long serialVersionUID = 5891314190581632904L;

    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
