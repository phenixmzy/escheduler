package cn.escheduler.dao.mapper;

import cn.escheduler.dao.model.OpinionFeedback;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * opinion feedback
 */
public interface OpinionFeedbackMapper {

    /**
     * count opinion feedback number
     * @return
     */
    @SelectProvider(type = OpinionFeedbackMapperProvider.class, method = "countOpinionFeedbackNumber")
    Integer countOpinionFeedbackNumber();


    /**
     * query definition list paging

     * @param offset
     * @param pageSize
     * @return
     */
    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "opinionType", column = "opinion_type", javaType = Integer.class, jdbcType = JdbcType.TINYINT),
        @Result(property = "problemDescription", column = "problem_description", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "opinionProgramme", column = "opinion_programme", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "contactInformation", column = "contact_information", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "vvNumber", column = "vv_number", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "browseNumber", column = "browse_number", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "praiseNumber", column = "praise_number", javaType = Integer.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "handleState", column = "handle_state", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "createTime", column = "create_time", javaType = Timestamp.class, jdbcType = JdbcType.DATE),
        @Result(property = "updateTime", column = "update_time", javaType = Timestamp.class, jdbcType = JdbcType.DATE)
    })
    @SelectProvider(type = OpinionFeedbackMapperProvider.class, method = "queryOpinionFeedbackListPaging")
    List<OpinionFeedback> queryOpinionFeedbackListPaging(@Param("offset") int offset,
                                                @Param("pageSize") int pageSize);

    /**
     * insert opinion feedback
     * @param opinionFeedback
     * @return
     */
    @InsertProvider(type = OpinionFeedbackMapperProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "opinionFeedback.id")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "opinionFeedback.id", before = false, resultType = int.class)
    int insert(@Param("opinionFeedback") OpinionFeedback opinionFeedback);


    /**
     * update browseNumber praiseNumber updateTime and updateTime by id
     * @param browseNumber
     * @param praiseNumber
     * @param id
     * @return
     */
    @UpdateProvider(type = OpinionFeedbackMapperProvider.class, method = "updateOpinionFeedbackById")
    int updateOpinionFeedbackById(@Param("browseNumber") int browseNumber,
                                 @Param("praiseNumber") int praiseNumber,
                                  @Param("handleState") int handleState,
                                  @Param("updateTime") Date updateTime,
                                 @Param("id") int id);

    /**
     * update opinion feedback
     * @param browseNumber
     * @param praiseNumber
     * @param handleState
     * @param updateTime
     * @param id
     * @param opinionType
     * @param problemDescription
     * @param opinionProgramme
     * @param contactInformation
     * @param vvNumber
     * @param name
     * @return
     */
    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "opinionType", column = "opinion_type", javaType = Integer.class, jdbcType = JdbcType.TINYINT),
        @Result(property = "problemDescription", column = "problem_description", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "opinionProgramme", column = "opinion_programme", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "contactInformation", column = "contact_information", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "vvNumber", column = "vv_number", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "browseNumber", column = "browse_number", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "praiseNumber", column = "praise_number", javaType = Integer.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "handleState", column = "handle_state", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "updateTime", column = "update_time", javaType = Timestamp.class, jdbcType = JdbcType.DATE)
    })
    @UpdateProvider(type = OpinionFeedbackMapperProvider.class, method = "update")
    int update(@Param("browseNumber") int browseNumber,
               @Param("praiseNumber") int praiseNumber,
               @Param("handleState") int handleState,
               @Param("updateTime") Date updateTime,
               @Param("id") int id,
               @Param("opinionType") int opinionType,
               @Param("problemDescription") String problemDescription,
               @Param("opinionProgramme") String opinionProgramme,
               @Param("contactInformation") int contactInformation,
               @Param("vvNumber") String vvNumber,
               @Param("name") String name);
}
