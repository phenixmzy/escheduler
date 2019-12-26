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
package cn.escheduler.dao;

import cn.escheduler.common.enums.AlertStatus;
import cn.escheduler.common.enums.AlertType;
import cn.escheduler.common.enums.ShowType;
import cn.escheduler.common.utils.SendUtils;
import cn.escheduler.dao.datasource.ConnectionFactory;
import cn.escheduler.dao.mapper.AlertGroupMapper;
import cn.escheduler.dao.mapper.AlertMapper;
import cn.escheduler.dao.mapper.UserAlertGroupMapper;
import cn.escheduler.dao.model.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AlertDao extends AbstractBaseDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AlertMapper alertMapper;

    @Autowired
    private AlertGroupMapper alertGroupMapper;

    @Autowired
    private UserAlertGroupMapper userAlertGroupMapper;

    @Override
    protected void init() {
        alertMapper = ConnectionFactory.getSqlSession().getMapper(AlertMapper.class);
        alertGroupMapper = ConnectionFactory.getSqlSession().getMapper(AlertGroupMapper.class);
        userAlertGroupMapper = ConnectionFactory.getSqlSession().getMapper(UserAlertGroupMapper.class);
    }

    /**
     * insert alert
     * @param alert
     * @return
     */
    public int addAlert(Alert alert){
        return alertMapper.insert(alert);
    }

    /**
     * update alert
     * @param alertStatus
     * @param log
     * @param id
     * @return
     */
    public int updateAlert(AlertStatus alertStatus,String log,int id){
        return alertMapper.update(alertStatus, log, new Date(), id);
    }

    /**
     * query user list by alert group id
     * @param alerGroupId
     * @return
     */
    public List<User> queryUserByAlertGroupId(int alerGroupId){
        return userAlertGroupMapper.queryForUser(alerGroupId);
    }
    /**
     * MasterServer or WorkerServer stoped
     */
    public void sendServerStopedAlert(int alertgroupId,String host,String serverType){
        Alert alert = new Alert();
        String content = String.format("[{'type':'%s','host':'%s','event':'server down','warning level':'serious'}]",
                serverType, host);
        //alert.setTitle("Fault tolerance warning");
        String alertGroupName = null;
        AlertGroup alertGroup = alertGroupMapper.queryById(alertgroupId);
        if(alertGroup != null && StrUtil.isNotEmpty(alertGroup.getGroupName())){
            alertGroupName = alertGroup.getGroupName();
            alert.setAlertType(alertGroup.getGroupType());
        }
        alert.setTitle(SendUtils.packageAlertContent(null,null,null,null,"服务器故障",alertGroupName,serverType,host));
        alert.setShowType(ShowType.TABLE);
        alert.setContent(content);
        alert.setAlertGroupId(alertgroupId);
        alert.setCreateTime(new Date());
        alert.setUpdateTime(new Date());
        alertMapper.insert(alert);
    }

    /**
     * process time out alert
     * @param processInstance
     * @param processDefinition
     */
    public void sendProcessTimeoutAlert(ProcessInstance processInstance, ProcessDefinition processDefinition){
        int alertgroupId = processInstance.getWarningGroupId();
        String receivers = processDefinition.getReceivers();
        String receiversCc = processDefinition.getReceiversCc();
        Alert alert = new Alert();
        String content = String.format("[{'id':'%d','name':'%s','event':'timeout','warnLevel':'middle'}]",
                processInstance.getId(), processInstance.getName());
        //alert.setTitle("Process Timeout Warn");
        String projectName = processInstance.getProcessDefinition().getProjectName();
        String definitionName = processInstance.getProcessDefinition().getName();
        String instanceName = processInstance.getName();
        String alertGroupName = null;
        AlertGroup alertGroup = alertGroupMapper.queryById(alertgroupId);
        if(alertGroup != null && StrUtil.isNotEmpty(alertGroup.getGroupName())){
            alertGroupName = alertGroup.getGroupName();
            alert.setAlertType(alertGroup.getGroupType());
        }
        alert.setTitle(SendUtils.packageAlertContent(projectName,definitionName,instanceName,null,"工作流超时",alertGroupName,null,null));
        alert.setShowType(ShowType.TABLE);
        alert.setContent(content);
        alert.setAlertGroupId(alertgroupId);
        if (StringUtils.isNotEmpty(receivers)) {
            alert.setReceivers(receivers);
        }
        if (StringUtils.isNotEmpty(receiversCc)) {
            alert.setReceiversCc(receiversCc);
        }
        alert.setCreateTime(new Date());
        alert.setUpdateTime(new Date());
        alertMapper.insert(alert);
    }

    /**
     * task timeout warn
     */
    public void sendTaskTimeoutAlert(int alertgroupId,String receivers,String receiversCc,int taskId,String taskName,String projectName,String definitionName,String instanceName){
        Alert alert = new Alert();
        String content = String.format("[{'id':'%d','name':'%s','event':'timeout','warnLevel':'middle'}]",taskId,taskName);
        //alert.setTitle("Task Timeout Warn");
        String alertGroupName = null;
        AlertGroup alertGroup = alertGroupMapper.queryById(alertgroupId);
        if(alertGroup != null && StrUtil.isNotEmpty(alertGroup.getGroupName())){
            alertGroupName = alertGroup.getGroupName();
            alert.setAlertType(alertGroup.getGroupType());
        }
        alert.setTitle(SendUtils.packageAlertContent(projectName,definitionName,instanceName,taskName,"节点超时",alertGroupName,null,null));
        alert.setShowType(ShowType.TABLE);
        alert.setContent(content);
        alert.setAlertGroupId(alertgroupId);
        if (StringUtils.isNotEmpty(receivers)) {
            alert.setReceivers(receivers);
        }
        if (StringUtils.isNotEmpty(receiversCc)) {
            alert.setReceiversCc(receiversCc);
        }
        alert.setCreateTime(new Date());
        alert.setUpdateTime(new Date());
        alertMapper.insert(alert);
    }

    /**
     * list the alert information of waiting to be executed
     * @return
     */
    public List<Alert> listWaitExecutionAlert(){
        return alertMapper.listAlertByStatus(AlertStatus.WAIT_EXECUTION);
    }

    /**
     * list user information by alert group id
     * @param alertgroupId
     * @return
     */
    public List<User> listUserByAlertgroupId(int alertgroupId){
        return userAlertGroupMapper.listUserByAlertgroupId(alertgroupId);
    }

    public AlertGroup queryAlertGroupByAlertgroupId(int alertgroupId){
        return alertGroupMapper.queryById(alertgroupId);
    }


}
