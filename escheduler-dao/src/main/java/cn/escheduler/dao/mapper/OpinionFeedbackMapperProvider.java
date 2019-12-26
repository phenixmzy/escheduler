package cn.escheduler.dao.mapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * opinion feedback mapper provider
 */
public class OpinionFeedbackMapperProvider {
    private static final String TABLE_NAME = "t_escheduler_opinion_feedback";

    /**
     * count opinion feedback number
     *
     * @param parameter
     * @return
     */
    public String countOpinionFeedbackNumber(Map<String, Object> parameter) {
        return new SQL() {{
            SELECT("count(0)");

            FROM(TABLE_NAME);
        }}.toString();
    }

    /**
     * query opinion feedback list paging
     *
     * @param parameter
     * @return
     */
    public String queryOpinionFeedbackListPaging(Map<String, Object> parameter) {
        return new SQL() {{
            SELECT(
                "id,opinion_type,problem_description,opinion_programme,contact_information,vv_number,name,"
                    + "browse_number,handle_state,praise_number,create_time,update_time ");
            FROM(TABLE_NAME);
            ORDER_BY("update_time  desc limit #{offset},#{pageSize} ");
        }}.toString();
    }

    /**
     * insert opinion feedback
     *
     * @param parameter
     * @return
     */
    public String insert(Map<String, Object> parameter) {
        return new SQL() {
            {
                INSERT_INTO(TABLE_NAME);
                VALUES("`opinion_type`", "#{opinionFeedback.opinionType}");
                VALUES("`problem_description`", "#{opinionFeedback.problemDescription}");
                VALUES("`opinion_programme`", "#{opinionFeedback.opinionProgramme}");
                VALUES("`contact_information`", "#{opinionFeedback.contactInformation}");
                VALUES("`vv_number`", "#{opinionFeedback.vvNumber}");
                VALUES("`name`", "#{opinionFeedback.name}");
                VALUES("`browse_number`", "#{opinionFeedback.browseNumber}");
                VALUES("`praise_number`", "#{opinionFeedback.praiseNumber}");
                VALUES("`handle_state`", "#{opinionFeedback.handleState}");
                VALUES("`create_time`", "#{opinionFeedback.createTime}");
                VALUES("`update_time`", "#{opinionFeedback.updateTime}");
            }
        }.toString();
    }

    /**
     * update browseNumber praiseNumber updateTime and updateTime by id
     *
     * @param parameter
     * @return
     */
    public String updateOpinionFeedbackById(Map<String, Object> parameter) {
        return new SQL() {
            {
                UPDATE(TABLE_NAME);
                Object browseNumber = parameter.get("browseNumber");
                if (browseNumber != null && 0 != Integer.parseInt(browseNumber.toString())) {
                    SET("`browse_number`=#{browseNumber}");
                }
                Object praiseNumber = parameter.get("praiseNumber");
                if (praiseNumber != null && 0 != Integer.parseInt(praiseNumber.toString())) {
                    SET("`praise_number`=#{praiseNumber}");
                }
                Object handleState = parameter.get("handleState");
                if (handleState != null && 0 != Integer.parseInt(handleState.toString())) {
                    SET("`handle_state`=#{handleState}");
                }
                SET("`update_time`=#{updateTime}");
                WHERE("`id`=#{id}");

            }
        }.toString();
    }

    /**
     * update opinion feedback
     *
     * @param parameter
     * @return
     */
    public String update(Map<String, Object> parameter) {
        return new SQL() {
            {
                UPDATE(TABLE_NAME);
                Object opinionType = parameter.get("opinionType");
                if (opinionType != null && 0 != Integer.parseInt(opinionType.toString())) {
                    SET("`opinion_type`=#{opinionType}");
                }
                Object problemDescription = parameter.get("problemDescription");
                if (problemDescription != null && StringUtils.isNotEmpty(problemDescription.toString())) {
                    SET("`problem_description`=#{problemDescription}");
                }
                Object opinionProgramme = parameter.get("opinionProgramme");
                if (opinionProgramme != null && StringUtils.isNotEmpty(opinionProgramme.toString())) {
                    SET("`opinion_programme`=#{opinionProgramme}");
                }
                Object contactInformation = parameter.get("contactInformation");
                if (contactInformation != null && 0 != Integer.parseInt(contactInformation.toString())) {
                    SET("`contact_information`=#{contactInformation}");
                }

                Object vvNumber = parameter.get("vvNumber");
                if (vvNumber != null && StringUtils.isNotEmpty(vvNumber.toString())) {
                    SET("`vv_number`=#{vvNumber}");
                }
                Object name = parameter.get("name");
                if (name != null && StringUtils.isNotEmpty(name.toString())) {
                    SET("`name`=#{name}");
                }
                Object browseNumber = parameter.get("browseNumber");
                if (browseNumber != null && 0 != Integer.parseInt(browseNumber.toString())) {
                    SET("`browse_number`=#{browseNumber}");
                }
                Object praiseNumber = parameter.get("praiseNumber");
                if (praiseNumber != null && 0 != Integer.parseInt(praiseNumber.toString())) {
                    SET("`praise_number`=#{praiseNumber}");
                }
                Object handleState = parameter.get("handleState");
                if (handleState != null && 0 != Integer.parseInt(handleState.toString())) {
                    SET("`handle_state`=#{handleState}");
                }
                SET("`update_time`=#{updateTime}");
                WHERE("`id`=#{id}");
            }
        }.toString();
    }
}
