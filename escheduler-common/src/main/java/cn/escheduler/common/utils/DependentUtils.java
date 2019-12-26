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

import cn.escheduler.common.enums.DependResult;
import cn.escheduler.common.enums.DependentRelation;
import cn.escheduler.common.model.DateInterval;
import cn.escheduler.common.utils.dependent.DependentDateUtils;
import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DependentUtils {

    private static final Logger logger = LoggerFactory.getLogger(DependentUtils.class);

    public static DependResult getDependResultForRelation(DependentRelation relation,
                                                          List<DependResult> dependResultList){

        DependResult dependResult = DependResult.SUCCESS;

        switch (relation){
            case AND:
                if(dependResultList.contains(DependResult.FAILED)){
                    dependResult = DependResult.FAILED;
                }else if(dependResultList.contains(DependResult.WAITING)){
                    dependResult = DependResult.WAITING;
                }else{
                    dependResult = DependResult.SUCCESS;
                }
                break;
            case OR:
                if(dependResultList.contains(DependResult.SUCCESS)){
                    dependResult = DependResult.SUCCESS;
                }else if(dependResultList.contains(DependResult.WAITING)){
                    dependResult = DependResult.WAITING;
                }else{
                    dependResult = DependResult.FAILED;
                }
                break;
            default:
               break;
        }
        return dependResult;
    }


    /**
     * get date interval list by business date and date value.
     * @param businessDate
     * @param dateValue
     * @return
     */
    public static List<DateInterval> getDateIntervalList(Date businessDate, String dateValue){
        List<DateInterval> result = new ArrayList<>();
        switch (dateValue){
            case "last1Hour":
                result =  DependentDateUtils.getLastHoursInterval(businessDate, 1,dateValue);
                break;
            case "last2Hours":
                result =  DependentDateUtils.getLastHoursInterval(businessDate, 2,dateValue);
                break;
            case "last3Hours":
                result =  DependentDateUtils.getLastHoursInterval(businessDate, 3,dateValue);
                break;
            case "today":
                result =  DependentDateUtils.getTodayInterval(DateUtil.offsetHour(businessDate, 8),dateValue);
                break;
            case "last1Days":
                result =  DependentDateUtils.getLastDayInterval(DateUtil.offsetHour(businessDate, 8), 1,dateValue);
                break;
            case "last2Days":
                result =  DependentDateUtils.getLastDayInterval(DateUtil.offsetHour(businessDate, 8), 2,dateValue);
                break;
            case "last3Days":
                result = DependentDateUtils.getLastDayInterval(DateUtil.offsetHour(businessDate, 8), 3,dateValue);
                break;
            case "last7Days":
                result = DependentDateUtils.getLastDayInterval(DateUtil.offsetHour(businessDate, 8), 7,dateValue);
                break;
            case "thisWeek":
                result = DependentDateUtils.getThisWeekInterval(DateUtil.offsetHour(businessDate, 8),dateValue);
                break;
            case "lastWeek":
                result = DependentDateUtils.getLastWeekInterval(DateUtil.offsetHour(businessDate, 8),dateValue);
                break;
            case "lastMonday":
                result = DependentDateUtils.getLastWeekOneDayInterval(DateUtil.offsetHour(businessDate, 8), 1,dateValue);
                break;
            case "lastTuesday":
                result = DependentDateUtils.getLastWeekOneDayInterval(DateUtil.offsetHour(businessDate, 8), 2,dateValue);
                break;
            case "lastWednesday":
                result = DependentDateUtils.getLastWeekOneDayInterval(DateUtil.offsetHour(businessDate, 8), 3,dateValue);
                break;
            case "lastThursday":
                result = DependentDateUtils.getLastWeekOneDayInterval(DateUtil.offsetHour(businessDate, 8), 4,dateValue);
                break;
            case "lastFriday":
                result = DependentDateUtils.getLastWeekOneDayInterval(DateUtil.offsetHour(businessDate, 8), 5,dateValue);
                break;
            case "lastSaturday":
                result = DependentDateUtils.getLastWeekOneDayInterval(DateUtil.offsetHour(businessDate, 8), 6,dateValue);
                break;
            case "lastSunday":
                result = DependentDateUtils.getLastWeekOneDayInterval(DateUtil.offsetHour(businessDate, 8), 7,dateValue);
                break;
            case "thisMonth":
                result = DependentDateUtils.getThisMonthInterval(DateUtil.offsetHour(businessDate, 8),dateValue);
                break;
            case "lastMonth":
                result = DependentDateUtils.getLastMonthInterval(DateUtil.offsetHour(businessDate, 8),dateValue);
                break;
            case "lastMonthBegin":
                result = DependentDateUtils.getLastMonthBeginInterval(DateUtil.offsetHour(businessDate, 8), true,dateValue);
                break;
            case "lastMonthEnd":
                result = DependentDateUtils.getLastMonthBeginInterval(DateUtil.offsetHour(businessDate, 8), false,dateValue);
                break;
            default:
                break;
        }
        return result;
    }


}
