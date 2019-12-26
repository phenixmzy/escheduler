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
package cn.escheduler.common.job.db;

import cn.escheduler.common.utils.CommonUtils;
import cn.escheduler.common.utils.MongoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


/**
 * data source of mongodb
 */
public class MongodbDataSource extends BaseDataSource {

  private static final Logger logger = LoggerFactory.getLogger(MongodbDataSource.class);

  /**
   * gets the JDBC url for the data source connection
   * @return
   */
  @Override
  public String getJdbcUrl() {
    String address = getAddress();
    if (address.lastIndexOf("/") != (address.length() - 1)) {
      address += "/";
    }
    String jdbcUrl = address + getDatabase();
    if (StringUtils.isNotEmpty(getOther())) {
      jdbcUrl += "?" + getOther();
    }
    return jdbcUrl;
  }

  /**
   * test whether the data source can be connected successfully
   */
  @Override
  public void isConnectable(){
    String[] hostsPorts = CommonUtils.getHostsAndPort(getAddress());
    try {
      MongoUtils mongoUtils = new MongoUtils(hostsPorts[0], hostsPorts[1], getUser(), getPassword(), getDatabase(), null);
      boolean isConnection = mongoUtils.getConnectionState();
      if(!isConnection){
        logger.error("mongodb datasource try conn close conn error");
      }
    } catch (Exception e) {
      logger.error("mongodb datasource try conn close conn error", e);
      throw e;
    }
  }
}
