package com.dingkai.personManage.business.code.wechat.constants;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/11 10:20
 */
public class WechatUrlConstant {

    public static final String accessTokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=#{appId}&secret=#{secret}";

    public static final String createMenuUrl="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=#{accessToken}";

    public static final String uploadUrl="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=#{accessToken}&type=#{type}";

    public static final String tempQrCodeCreateUrl="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=#{accessToken}";

    public static final String tempQrCodeShowUrl="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=#{ticket}";

}
