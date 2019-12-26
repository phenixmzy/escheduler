/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.escheduler.alert.runner;

import cn.escheduler.alert.manager.EmailManager;
import cn.escheduler.alert.manager.EnterpriseWeChatManager;
import cn.escheduler.alert.utils.Constants;
import cn.escheduler.alert.utils.MessageUtil;
import cn.escheduler.common.enums.AlertStatus;
import cn.escheduler.common.enums.AlertType;
import cn.escheduler.dao.AlertDao;
import cn.escheduler.dao.model.Alert;
import cn.escheduler.dao.model.AlertGroup;
import cn.escheduler.dao.model.User;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * alert sender
 */
public class AlertSender{

    private static final Logger logger = LoggerFactory.getLogger(AlertSender.class);

    private static final EmailManager emailManager= new EmailManager();
    private static final EnterpriseWeChatManager weChatManager= new EnterpriseWeChatManager();


    private List<Alert> alertList;
    private AlertDao alertDao;

    public AlertSender(){}
    public AlertSender(List<Alert> alertList, AlertDao alertDao){
        super();
        this.alertList = alertList;
        this.alertDao = alertDao;
    }

/*    public void run() {

        List<User> users;

        Map<String, Object> retMaps = null;
        for(Alert alert:alertList){
            users = alertDao.listUserByAlertgroupId(alert.getAlertGroupId());



            // receiving group list
            List<String> receviersList = new ArrayList<String>();
            for(User user:users){
                receviersList.add(user.getEmail());
            }
            // custom receiver
            String receivers = alert.getReceivers();
            if (StringUtils.isNotEmpty(receivers)){
                String[] splits = receivers.split(",");
                for (String receiver : splits){
                    receviersList.add(receiver);
                }
            }

            // copy list
            List<String> receviersCcList = new ArrayList<String>();


            // Custom Copier
            String receiversCc = alert.getReceiversCc();

            if (StringUtils.isNotEmpty(receiversCc)){
                String[] splits = receiversCc.split(",");
                for (String receiverCc : splits){
                    receviersCcList.add(receiverCc);
                }
            }

            if (CollectionUtils.isEmpty(receviersList) && CollectionUtils.isEmpty(receviersCcList)) {
                logger.warn("alert send error : At least one receiver address required");
                alertDao.updateAlert(AlertStatus.EXECUTION_FAILURE, "execution failure,At least one receiver address required.", alert.getId());
                continue;
            }

            if (alert.getAlertType() == AlertType.EMAIL){
                retMaps = emailManager.send(receviersList,receviersCcList, alert.getTitle(), alert.getContent(),alert.getShowType());

                alert.setInfo(retMaps);
            }else if (alert.getAlertType() == AlertType.SMS){
                retMaps = emailManager.send(getReciversForSMS(users), alert.getTitle(), alert.getContent(),alert.getShowType());
                alert.setInfo(retMaps);
            }

            boolean flag = Boolean.parseBoolean(String.valueOf(retMaps.get(Constants.STATUS)));
            if (flag){
                alertDao.updateAlert(AlertStatus.EXECUTION_SUCCESS, "execution success", alert.getId());
                logger.info("alert send success");
                try {
                    String token = EnterpriseWeChatUtils.getToken();
                    weChatManager.send(alert,token);
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                }
            }else {
                alertDao.updateAlert(AlertStatus.EXECUTION_FAILURE,String.valueOf(retMaps.get(Constants.MESSAGE)),alert.getId());
                logger.info("alert send error : {}" , String.valueOf(retMaps.get(Constants.MESSAGE)));
            }
        }

    }*/

    /*public void run() {

        Map<String, Object> retMaps = new HashMap<>();
        for(Alert alert:alertList){
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("id",alert.getAlertGroupId());
            String jsonStr = JSONUtil.toJsonStr(paramMap);
            String result = HttpRequest.post("http://xxl-quality.logsss.com/quality/sys-alarm-group/getById").body(jsonStr).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(result);
            int code = (int) jsonObject.get("code");
            if(code == 200){
                JSONObject sysAlarmGroup = (JSONObject) jsonObject.get("data");
                String vv = null;
                String weixin = null;
                String email = null;
                String[] userIds = sysAlarmGroup.get("userIds").toString().split(",");
                for (int i = 0; i < userIds.length ; i++) {
                    String userId = userIds[i];
                    HashMap<String, Object> userParamMap = new HashMap<>();
                    userParamMap.put("id",Integer.parseInt(userId));
                    String userJsonStr = JSONUtil.toJsonStr(userParamMap);
                    String userResult = HttpRequest.post("http://xxl-quality.logsss.com/quality/sys-user/getById").body(userJsonStr).execute().body();
                    JSONObject userJsonObject = JSONUtil.parseObj(userResult);
                    int userCode = (int) userJsonObject.get("code");
                    if(userCode == 200){
                        JSONObject sysUser = (JSONObject) userJsonObject.get("data");
                        if(i == 0) {
                            vv = sysUser.get("vv").toString();
                            weixin = sysUser.get("weixin").toString();
                            email = sysUser.get("email").toString();
                        }else{
                            vv = vv + "," + sysUser.get("vv").toString();
                            weixin = weixin + "," + sysUser.get("weixin").toString();
                            email = email + "," + sysUser.get("email").toString();
                        }
                    }

                }
                String sendTitle = "【鹰眼监控】";
                StringBuffer stringBuffer = new StringBuffer();

                if(alert.getTitle().split(",").length>1){
                    String projectName = alert.getTitle().split(",")[0];
                    String definitionName = alert.getTitle().split(",")[1];
                    String instanceName = alert.getTitle().split(",")[2];
                    String taskName = alert.getTitle().split(",")[3];
                    String content = alert.getTitle().split(",")[4];
                    stringBuffer.append("--------项目名称-------- \n");
                    stringBuffer.append(""+ projectName + " \n");
                    stringBuffer.append("--------工作流名称-------- \n");
                    stringBuffer.append(""+ definitionName + " \n");
                    stringBuffer.append("--------实例名称-------- \n");
                    stringBuffer.append(""+ instanceName + " \n");
                    stringBuffer.append("--------节点名称-------- \n");
                    stringBuffer.append(""+ taskName + " \n");
                    stringBuffer.append("--------告警内容-------- \n");
                    stringBuffer.append(""+ content + " \n");
                    stringBuffer.append("--------北京时间-------- \n");
                    stringBuffer.append(DateUtil.now() + " \n");
                    MessageUtil.send(sendTitle, stringBuffer.toString(), "vv", vv, "");
                    MessageUtil.send(sendTitle, stringBuffer.toString(), "weixin", weixin, "");
                    MessageUtil.send(sendTitle, stringBuffer.toString(), "yxhf", email, "");
                }
            }

            retMaps.put(Constants.STATUS, true);

            alert.setInfo(retMaps);

            boolean flag = Boolean.parseBoolean(String.valueOf(retMaps.get(Constants.STATUS)));
            if (flag){
                alertDao.updateAlert(AlertStatus.EXECUTION_SUCCESS, "execution success", alert.getId());
                logger.info("alert send success");
            }else {
                alertDao.updateAlert(AlertStatus.EXECUTION_FAILURE,String.valueOf(retMaps.get(Constants.MESSAGE)),alert.getId());
                logger.info("alert send error : {}" , String.valueOf(retMaps.get(Constants.MESSAGE)));
            }
        }
    }*/

    public void run() {

        List<User> users;

        Map<String, Object> retMaps = new HashMap<>();
        for(Alert alert:alertList){
            users = alertDao.listUserByAlertgroupId(alert.getAlertGroupId());
            String vv = null;
            String weixin = null;
            String email = null;
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                if(i == 0) {
                    if(StrUtil.isNotEmpty(user.getVv())){
                        vv = user.getVv();
                    }
                    if(StrUtil.isNotEmpty(user.getWeixin())) {
                        weixin = user.getWeixin();
                    }
                    if(StrUtil.isNotEmpty(user.getEmail())) {
                        email = user.getEmail();
                    }
                }else{
                    if(StrUtil.isNotEmpty(user.getVv())) {
                        vv = vv + "," + user.getVv();
                    }
                    if(StrUtil.isNotEmpty(user.getWeixin())) {
                        weixin = weixin + "," + user.getWeixin();
                    }
                    if(StrUtil.isNotEmpty(user.getEmail())) {
                        email = email + "," + user.getEmail();
                    }
                }
            }
            String sendTitle = "【鹰眼监控】";
            String[] alertTypes = alert.getAlertType().split(",");
            for (int j = 0; j < alertTypes.length; j++) {
                if("vv".equals(alertTypes[j])){
                    MessageUtil.send(sendTitle, alert.getTitle(), "vv", vv, "");
                }else if("weixin".equals(alertTypes[j])){
                    MessageUtil.send(sendTitle, alert.getTitle(), "weixin", weixin, "");
                }else if("email".equals(alertTypes[j])){
                    MessageUtil.send(sendTitle, alert.getTitle(), "yxhf", email, "");
                }
            }
            retMaps.put(Constants.STATUS, true);

            alert.setInfo(retMaps);

            boolean flag = Boolean.parseBoolean(String.valueOf(retMaps.get(Constants.STATUS)));
            if (flag){
                alertDao.updateAlert(AlertStatus.EXECUTION_SUCCESS, "execution success", alert.getId());
                logger.info("alert send success");
            }else {
                alertDao.updateAlert(AlertStatus.EXECUTION_FAILURE,String.valueOf(retMaps.get(Constants.MESSAGE)),alert.getId());
                logger.info("alert send error : {}" , String.valueOf(retMaps.get(Constants.MESSAGE)));
            }
        }
    }



    /**
     * get a list of SMS users
     * @param users
     * @return
     */
    private List<String> getReciversForSMS(List<User> users){
        List<String> list = new ArrayList<>();
        for (User user : users){
            list.add(user.getPhone());
        }
        return list;
    }
}
