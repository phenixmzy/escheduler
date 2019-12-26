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

import cn.escheduler.common.Constants;
import cn.escheduler.common.enums.ResUploadType;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static cn.escheduler.common.Constants.*;
import static cn.escheduler.common.utils.PropertyUtils.getBoolean;
import static cn.escheduler.common.utils.PropertyUtils.getString;

/**
 * common utils
 */
public class CommonUtils {

  private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

  /**
   * @return get the path of system environment variables
   */
  public static String getSystemEnvPath() {
    String envPath = getString(ESCHEDULER_ENV_PATH);
    if (StringUtils.isEmpty(envPath)) {
      envPath = System.getProperty("user.home") + File.separator + ".bash_profile";
    }

    return envPath;
  }

  /**
   * @return get queue implementation name
   */
  public static String getQueueImplValue(){
    return getString(Constants.SCHEDULER_QUEUE_IMPL);
  }

  /**
   * 
   * @return is develop mode
   */
  public static boolean isDevelopMode() {
    return getBoolean(DEVELOPMENT_STATE);
  }



  /**
   * if upload resource is HDFS and kerberos startup is true , else false
   * @return
   */
  public static boolean getKerberosStartupState(){
    String resUploadStartupType = PropertyUtils.getString(cn.escheduler.common.Constants.RES_UPLOAD_STARTUP_TYPE);
    ResUploadType resUploadType = ResUploadType.valueOf(resUploadStartupType);
    Boolean kerberosStartupState = getBoolean(cn.escheduler.common.Constants.HADOOP_SECURITY_AUTHENTICATION_STARTUP_STATE);
    return resUploadType == ResUploadType.HDFS && kerberosStartupState;
  }

  /**
   * @Description 替换日期格式
   * @Author chongzi
   * @Date 2019/1/23 14:24
   * @Param
   * @return
   **/
  public static String replaceDateFormat(String name) {
    String day = null;
    if(name.contains("#today")){
      day = DateUtil.format(DateUtil.offsetHour(DateUtil.parse(DateUtil.now()), -8),"yyyy-MM-dd");
    }else if(name.contains("#yesterday")){
      day = DateUtil.format(DateUtil.offsetHour(DateUtil.yesterday(),-8),"yyyy-MM-dd");
    }
    if(StrUtil.isNotEmpty(day)) {
      String yyyy = DateUtil.format(DateUtil.parseDate(day), "yyyy");
      String MM = DateUtil.format(DateUtil.parseDate(day), "MM");
      String dd = DateUtil.format(DateUtil.parseDate(day), "dd");

      name = name.replaceAll("#today", "");
      name = name.replaceAll("#yesterday", "");
      name = name.replaceAll("#yyyy", yyyy);
      name = name.replaceAll("#MM", MM);
      name = name.replaceAll("#dd", dd);
    }
    return name;
  }

  public static String[] getHostsAndPort(String address) {
    String[] result = new String[2];
    String[] tmpArray = address.split(cn.escheduler.common.Constants.DOUBLE_SLASH);
    String hostsAndPorts = tmpArray[tmpArray.length - 1];
    StringBuilder hosts = new StringBuilder();
    String[] hostPortArray = hostsAndPorts.split(cn.escheduler.common.Constants.COMMA);
    String port = hostPortArray[0].split(cn.escheduler.common.Constants.COLON)[1];
    for (String hostPort : hostPortArray) {
      hosts.append(hostPort.split(cn.escheduler.common.Constants.COLON)[0]).append(cn.escheduler.common.Constants.COMMA);
    }
    hosts.deleteCharAt(hosts.length() - 1);
    result[0] = hosts.toString();
    result[1] = port;
    return result;
  }
}
