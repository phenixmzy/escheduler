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
package cn.escheduler.common.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * send utils
 */
public class SendUtils {

  private static final Logger logger = LoggerFactory.getLogger(SendUtils.class);


  /**
   * 封装告警内容
   * @param projectName 项目名称
   * @param definitionName 工作流名称
   * @param instanceName 实例名称
   * @param taskName 节点名称
   * @param content 告警内容
   * @param alertGroupName 告警组名称
   * @param serverType 服务器类型
   * @param host 主机
   * @return
   */
  public static String packageAlertContent(String projectName,String definitionName,String instanceName,String taskName,String content,String alertGroupName,String serverType,String host){
      StringBuffer stringBuffer = new StringBuffer();
      if(StrUtil.isNotEmpty(projectName)){
        stringBuffer.append("--------项目名称-------- \n");
        stringBuffer.append(""+ projectName + " \n");
      }
      if(StrUtil.isNotEmpty(definitionName)){
        stringBuffer.append("--------工作流名称-------- \n");
        stringBuffer.append(""+ definitionName + " \n");
      }
      if(StrUtil.isNotEmpty(instanceName)){
        stringBuffer.append("--------实例名称-------- \n");
        stringBuffer.append(""+ instanceName + " \n");
      }
      if(StrUtil.isNotEmpty(taskName)){
        stringBuffer.append("--------节点名称-------- \n");
        stringBuffer.append(""+ taskName + " \n");
      }
      if(StrUtil.isNotEmpty(serverType)) {
        stringBuffer.append("--------服务器类型-------- \n");
        stringBuffer.append("" + serverType + " \n");
      }
      if(StrUtil.isNotEmpty(host)) {
        stringBuffer.append("--------主机-------- \n");
        stringBuffer.append("" + host + " \n");
      }
      if(StrUtil.isNotEmpty(content)){
        stringBuffer.append("--------告警内容-------- \n");
        stringBuffer.append(""+ content + " \n");
      }
      if(StrUtil.isNotEmpty(alertGroupName)){
        stringBuffer.append("--------告警组名称-------- \n");
        stringBuffer.append(""+alertGroupName+" \n");
      }
      stringBuffer.append("--------北京时间-------- \n");
      stringBuffer.append(DateUtil.offsetHour(DateUtil.parse(DateUtil.now()),8) + " \n");
      return stringBuffer.toString();
  }

}
