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
package cn.escheduler.common.utils.dependent;

import cn.escheduler.common.model.DateInterval;
import cn.escheduler.common.utils.DateUtils;
import cn.hutool.core.date.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DependentDateUtils {

    /**
     * get last day interval list
     * @param businessDate
     * @param hourNumber
     * @return
     */
    public static List<DateInterval> getLastHoursInterval(Date businessDate, int hourNumber,String cycle){
        List<DateInterval> dateIntervals = new ArrayList<>();
        for(int index = hourNumber; index > 0; index--){
            Date lastHour = DateUtils.getSomeHourOfDay(businessDate, index);
            Date beginTime = DateUtils.getStartOfHour(lastHour);
            Date endTime = DateUtils.getEndOfHour(lastHour);
            dateIntervals.add(new DateInterval(beginTime, endTime,cycle));
        }
        return dateIntervals;
    }

    /**
     * get today day interval list
     * @param businessDate
     * @return
     */
    public static List<DateInterval> getTodayInterval(Date businessDate,String cycle){

        List<DateInterval> dateIntervals = new ArrayList<>();
        Date beginTime = DateUtils.getStartOfDay(businessDate);
        Date endTime = DateUtils.getEndOfDay(businessDate);
        dateIntervals.add(new DateInterval(beginTime, endTime,cycle));
        return dateIntervals;
    }

    /**
     * get last day interval list
     * @param businessDate
     * @param someDay
     * @return
     */
    public static List<DateInterval> getLastDayInterval(Date businessDate, int someDay,String cycle){

        List<DateInterval> dateIntervals = new ArrayList<>();
        for(int index = someDay; index > 0; index--){
            Date lastDay = DateUtils.getSomeDay(businessDate, -index);

            Date beginTime = DateUtils.getStartOfDay(lastDay);
            Date endTime = DateUtils.getEndOfDay(lastDay);
            dateIntervals.add(new DateInterval(beginTime, endTime,cycle));
        }
        return dateIntervals;
    }

    /**
     * get interval between this month first day and businessDate
     * @param businessDate
     * @return
     */
    public static List<DateInterval> getThisMonthInterval(Date businessDate,String cycle) {
        Date firstDay = DateUtils.getFirstDayOfMonth(businessDate);
        return getDateIntervalListBetweenTwoDates(firstDay, businessDate,cycle);
    }

    /**
     * get interval between last month first day and last day
     * @param businessDate
     * @return
     */
    public static List<DateInterval> getLastMonthInterval(Date businessDate,String cycle) {

        Date firstDayThisMonth = DateUtils.getFirstDayOfMonth(businessDate);
        Date lastDay = DateUtils.getSomeDay(firstDayThisMonth, -1);
        Date firstDay = DateUtils.getFirstDayOfMonth(lastDay);
        return getDateIntervalListBetweenTwoDates( firstDay, lastDay,cycle);
    }


    /**
     * get interval on first/last day of the last month
     * @param businessDate
     * @param isBeginDay
     * @return
     */
    public static List<DateInterval> getLastMonthBeginInterval(Date businessDate, boolean isBeginDay,String cycle) {

        Date firstDayThisMonth = DateUtils.getFirstDayOfMonth(businessDate);
        Date lastDay = DateUtils.getSomeDay(firstDayThisMonth, -1);
        Date firstDay = DateUtils.getFirstDayOfMonth(lastDay);
        if(isBeginDay){
            return getDateIntervalListBetweenTwoDates(firstDay, firstDay,cycle);
        }else{
            return getDateIntervalListBetweenTwoDates(lastDay, lastDay,cycle);
        }
    }

    /**
     * get interval between monday to businessDate of this week
     * @param businessDate
     * @return
     */
    public static List<DateInterval> getThisWeekInterval(Date businessDate,String cycle) {
        Date mondayThisWeek = DateUtils.getMonday(businessDate);
        return getDateIntervalListBetweenTwoDates(mondayThisWeek, businessDate,cycle);
    }

    /**
     * get interval between monday to sunday of last week
     * default set monday the first day of week
     * @param businessDate
     * @return
     */
    public static List<DateInterval> getLastWeekInterval(Date businessDate,String cycle) {
        Date mondayThisWeek = DateUtils.getMonday(businessDate);
        Date sunday = DateUtils.getSomeDay(mondayThisWeek, -1);
        Date monday = DateUtils.getMonday(sunday);
        return getDateIntervalListBetweenTwoDates(monday, sunday,cycle);
    }

    /**
     * get interval on the day of last week
     * default set monday the first day of week
     * @param businessDate
     * @param dayOfWeek monday:1,tuesday:2,wednesday:3,thursday:4,friday:5,saturday:6,sunday:7
     * @return
     */
    public static List<DateInterval> getLastWeekOneDayInterval(Date businessDate, int dayOfWeek,String cycle) {
        Date mondayThisWeek = DateUtils.getMonday(businessDate);
        Date sunday = DateUtils.getSomeDay(mondayThisWeek, -1);
        Date monday = DateUtils.getMonday(sunday);
        Date destDay = DateUtils.getSomeDay(monday, dayOfWeek -1);
        return getDateIntervalListBetweenTwoDates(destDay, destDay,cycle);
    }

    public static List<DateInterval> getDateIntervalListBetweenTwoDates(Date firstDay, Date lastDay,String cycle) {
        List<DateInterval> dateIntervals = new ArrayList<>();
        while(!firstDay.after(lastDay)){
            Date beginTime = DateUtils.getStartOfDay(firstDay);
            Date endTime = DateUtils.getEndOfDay(firstDay);
            dateIntervals.add(new DateInterval(beginTime, endTime,cycle));
            firstDay = DateUtils.getSomeDay(firstDay, 1);
        }
        return dateIntervals;
    }
}
