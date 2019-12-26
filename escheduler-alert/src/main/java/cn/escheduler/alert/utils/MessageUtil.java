package cn.escheduler.alert.utils;

import cn.escheduler.dao.model.Message;
import cn.escheduler.dao.model.MessageData;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description 消息发送工具类
 * @Author chongzi
 * @Date 2019/1/7 18:30
 **/

public class MessageUtil {

    public static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    private static String url = "http://msgcenter100.com/api-source/index";
    private static String testUrl = "http://branched.msgcenter100.com/api-source/index";
    private static String account = "dayu";
    private static String password = "Dayu123!@#";
    private static String apiKey = "RukcFOEzefNdJ2b2iMy6";
    private static String testApiKey = "M2IFTJWxAcipmZY6cZhL";

    /**
     * @Description 发送消息
     * @Author chongzi
     * @Date 2019/1/7 18:34
     * @Param channel=yxhf,vv,weixin
     * @return 
     **/
    public static void send(String title, String content, String channel, String to, String link){
        MessageData messageData = new MessageData();
        messageData.setTitle(title);
        messageData.setContent(content);
        messageData.setChannel(channel);
        messageData.setTo(to);
        if(StrUtil.isNotEmpty(link)){
            messageData.setUrl(link);
        }
        List<MessageData> messageDataList = new LinkedList<>();
        messageDataList.add(messageData);
        Message message = new Message();
        message.setAccount(account);
        message.setPassword(password);
        message.setApi_key(apiKey);
        message.setData(messageDataList);
        String json = JSONUtil.toJsonStr(message);
        HttpUtil.post(url,json);
    }
}
