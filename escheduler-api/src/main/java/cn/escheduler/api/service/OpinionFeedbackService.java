package cn.escheduler.api.service;

import cn.escheduler.api.enums.Status;
import cn.escheduler.api.utils.Constants;
import cn.escheduler.api.utils.PageInfo;
import cn.escheduler.dao.mapper.OpinionFeedbackMapper;
import cn.escheduler.dao.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * opinion feedback service
 */
@Service
public class OpinionFeedbackService extends BaseDAGService {
    private static final Logger logger = LoggerFactory.getLogger(OpinionFeedbackService.class);

    @Autowired
    private OpinionFeedbackMapper opinionFeedbackMapper;

    /**
     * query opinion feedback list paging
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Map<String, Object> queryOpinionFeedbackListPaging(Integer pageNo, Integer pageSize) {

        Map<String, Object> result = new HashMap<>(5);

        Integer count = opinionFeedbackMapper.countOpinionFeedbackNumber();

        PageInfo pageInfo = new PageInfo<ProcessData>(pageNo, pageSize);
        List<OpinionFeedback> resourceList = opinionFeedbackMapper.queryOpinionFeedbackListPaging(pageInfo.getStart(),
            pageSize);
        pageInfo.setTotalCount(count);
        pageInfo.setLists(resourceList);
        result.put(Constants.DATA_LIST, pageInfo);
        putMsg(result, Status.SUCCESS);

        return result;
    }

    /**
     * save opinion feedback
     *
     * @param opinionType
     * @param problemDescription
     * @param opinionProgramme
     * @param contactInformation
     * @param vvNumber
     * @param name
     * @param browseNumber
     * @param praiseNumber
     * @param handleState
     * @return
     */
    @Transactional(value = "TransactionManager", rollbackFor = Exception.class)
    public Map<String, Object> insertOpinionFeedback(int opinionType, String problemDescription,
                                                     String opinionProgramme,
                                                     int contactInformation, String vvNumber,
                                                     String name, int browseNumber, int praiseNumber, int handleState)
        throws IOException {

        Map<String, Object> result = new HashMap<String, Object>(5);

        OpinionFeedback opinionFeedback = new OpinionFeedback();
        Date now = new Date();

        opinionFeedback.setOpinionType(opinionType);
        opinionFeedback.setProblemDescription(problemDescription);
        opinionFeedback.setOpinionProgramme(opinionProgramme);

        opinionFeedback.setContactInformation(contactInformation);
        opinionFeedback.setVvNumber(vvNumber);
        opinionFeedback.setName(name);
        opinionFeedback.setBrowseNumber(browseNumber);
        opinionFeedback.setPraiseNumber(praiseNumber);
        opinionFeedback.setHandleState(handleState);
        opinionFeedback.setCreateTime(now);
        opinionFeedback.setUpdateTime(now);
        if (opinionFeedbackMapper.insert(opinionFeedback) > 0) {
            putMsg(result, Status.SUCCESS);
        } else {
            putMsg(result, Status.CREATE_OPINION_FEEDBACK_ERROR);
        }
        return result;
    }

    /**
     * update browseNumber praiseNumber updateTime and updateTime by id
     *
     * @param browseNumber
     * @param praiseNumber
     * @param handleState
     * @param id
     * @return
     */
    @Transactional(value = "TransactionManager", rollbackFor = Exception.class)
    public Map<String, Object> updateOpinionFeedbackById(int browseNumber, int praiseNumber, int handleState, int id)
        throws IOException {
        Map<String, Object> result = new HashMap<String, Object>(5);
        Date now = new Date();
        int updateOpinionFeedbackById = opinionFeedbackMapper.updateOpinionFeedbackById(browseNumber, praiseNumber,
            handleState, now, id);
        if (updateOpinionFeedbackById > 0) {
            putMsg(result, Status.SUCCESS);
        } else {
            putMsg(result, Status.UPDATE_OPINION_FEEDBACK_ERROR);
        }
        return result;
    }

    /**
     * update opinion feedback
     *
     * @param browseNumber
     * @param praiseNumber
     * @param handleState
     * @param id
     * @return
     */
    @Transactional(value = "TransactionManager", rollbackFor = Exception.class)
    public Map<String, Object> updateOpinionFeedback(int opinionType, String problemDescription,
                                                     String opinionProgramme,
                                                     int contactInformation, String vvNumber,
                                                     String name, int browseNumber, int praiseNumber, int handleState,
                                                     int id) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>(5);
        Date now = new Date();
        int updateOpinionFeedback = opinionFeedbackMapper.update(browseNumber, praiseNumber, handleState, now, id,
            opinionType, problemDescription, opinionProgramme, contactInformation, vvNumber, name);
        if (updateOpinionFeedback > 0) {
            putMsg(result, Status.SUCCESS);
        } else {
            putMsg(result, Status.UPDATE_OPINION_FEEDBACK_ERROR);
        }
        return result;
    }
}
