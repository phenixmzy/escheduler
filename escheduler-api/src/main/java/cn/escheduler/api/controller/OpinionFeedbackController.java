package cn.escheduler.api.controller;

import cn.escheduler.api.enums.Status;
import cn.escheduler.api.service.OpinionFeedbackService;
import cn.escheduler.api.utils.Constants;
import cn.escheduler.api.utils.Result;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import static cn.escheduler.api.enums.Status.*;

/**
 * opinion feedback controller
 */
@Api(tags = "OpinionFeedback_TAG")
@RestController
@RequestMapping("/opinion-feedback")
public class OpinionFeedbackController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OpinionFeedbackController.class);

    @Autowired
    private OpinionFeedbackService opinionFeedbackService;

    @ApiOperation(value = "queryOpinionFeedbackListPaging", notes= "QUERY_OPINION_FEEDBACK_LIST_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNo", value = "PAGE_NO", required = true, dataType = "Int"),
        @ApiImplicitParam(name = "pageSize", value = "PAGE_SIZE", required = true, dataType ="Int")
    })
    @PostMapping(value = "/queryOpinionFeedbackListPaging")
    @ResponseStatus(HttpStatus.CREATED)
    public Result queryOpinionFeedbackListPaging(@RequestParam("pageNo") Integer pageNo,
                                                 @RequestParam("pageSize") Integer pageSize) {
        try {
            logger.info("query opinion feedback list paging,  pageNo:{}, pageSize:{}", pageNo,pageSize);
            Map<String, Object> result = checkPageParams(pageNo, pageSize);
            if (result.get(Constants.STATUS) != Status.SUCCESS) {
                return returnDataListPaging(result);
            }
            result = opinionFeedbackService.queryOpinionFeedbackListPaging(pageNo, pageSize);
            return returnDataListPaging(result);
        } catch (Exception e) {
            logger.error(QUERY_OPINION_FEEDBACK_LIST_PAGING_ERROR.getMsg(), e);
            return error(QUERY_OPINION_FEEDBACK_LIST_PAGING_ERROR.getCode(),
                QUERY_OPINION_FEEDBACK_LIST_PAGING_ERROR.getMsg());
        }

    }


    /**
     * create opinion feedback controller
     *
     * @param opinionType
     * @param problemDescription
     * @param opinionProgramme
     * @param contactInformation
     * @param vvNumber
     * @param name
     * @return
     */
    @ApiOperation(value = "save", notes= "CREATE_OPINION_FEEDBACK_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "opinionType", value = "OPINION_TYPE", required = true, dataType = "Int"),
        @ApiImplicitParam(name = "problemDescription", value = "PROBLEM_DESCRIPTION", required = true, dataType ="String"),
        @ApiImplicitParam(name = "opinionProgramme", value = "OPINION_PROGRAMME",dataType ="String"),
        @ApiImplicitParam(name = "contactInformation", value = "CONTACT_INFORMATION", required = true, dataType ="Int"),
        @ApiImplicitParam(name = "vvNumber", value = "VV_NAME", dataType ="String"),
        @ApiImplicitParam(name = "name", value = "NAME", dataType ="String")
    })
    @PostMapping(value = "/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Result createOpinionFeedback(@RequestParam(value = "opinionType") Integer opinionType,
                                          @RequestParam(value = "problemDescription") String problemDescription,
                                          @RequestParam(value = "opinionProgramme", required = false) String opinionProgramme,
                                          @RequestParam(value = "contactInformation") Integer contactInformation,
                                          @RequestParam(value = "vvNumber", required = false) String vvNumber,
                                          @RequestParam(value = "name", required = false) String name) {

        try {
            logger.info("opinionType :{}, problemDescription: {}, opinionProgramme: {}, " +
                    "contactInformation: {}, vvNumber: {} name:{}",
                opinionType, problemDescription, opinionProgramme, contactInformation, vvNumber, name);
            Map<String, Object> result = opinionFeedbackService.insertOpinionFeedback(opinionType,
                problemDescription, opinionProgramme, contactInformation, vvNumber, name, 0, 0, 1);
            return returnDataList(result);
        } catch (Exception e) {
            logger.error(CREATE_OPINION_FEEDBACK_ERROR.getMsg(), e);
            return error(CREATE_OPINION_FEEDBACK_ERROR.getCode(), CREATE_OPINION_FEEDBACK_ERROR.getMsg());
        }
    }


    /**
     * update process definition
     *
     * @param id
     * @param browseNumber
     * @param praiseNumber
     * @param handleState
     * @return
     */
    @ApiOperation(value = "updateOpinionFeedbackById", notes= "UPDATE_OPINION_FEEDBACK_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Int"),
        @ApiImplicitParam(name = "browseNumber", value = "BROWSENUMBER", dataType ="Int"),
        @ApiImplicitParam(name = "praiseNumber", value = "PRAISENUMBER",  dataType ="Int"),
        @ApiImplicitParam(name = "handleState", value = "HANDLESTATE", dataType ="Int")
    })
    @PostMapping(value = "/updateOpinionFeedbackById")
    @ResponseStatus(HttpStatus.OK)
    public Result updateOpinionFeedbackById(@RequestParam(value = "id") int id,
                                           @RequestParam(value = "browseNumber", required = false) int browseNumber,
                                           @RequestParam(value = "praiseNumber", required = false) int praiseNumber,
                                           @RequestParam(value = "handleState", required = false) int handleState) {

        try {
            logger.info("id {}, browseNumber: {}, praiseNumber: {}, " +
                    "handleState:{}",
                id, browseNumber, praiseNumber,handleState);
            Map<String, Object> result =opinionFeedbackService.updateOpinionFeedbackById( browseNumber,  praiseNumber,  handleState, id);
            return returnDataList(result);
        }catch (Exception e){
            logger.error(UPDATE_OPINION_FEEDBACK_ERROR.getMsg(),e);
            return error(UPDATE_OPINION_FEEDBACK_ERROR.getCode(), Status.UPDATE_OPINION_FEEDBACK_ERROR.getMsg());
        }
    }


    /**
     * update opinion feedback
     *
     * @param opinionType
     * @param problemDescription
     * @param opinionProgramme
     * @param contactInformation
     * @param vvNumber
     * @param name
     * @param id
     * @return
     */
    @ApiOperation(value = "updateOpinionFeedback", notes= "UPDATE_OPINION_FEEDBACK_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "opinionType", value = "OPINION_TYPE",  dataType = "Int"),
        @ApiImplicitParam(name = "problemDescription", value = "PROBLEM_DESCRIPTION",  dataType ="String"),
        @ApiImplicitParam(name = "opinionProgramme", value = "OPINION_PROGRAMME",  dataType ="String"),
        @ApiImplicitParam(name = "contactInformation", value = "CONTACT_INFORMATION",  dataType ="Int"),
        @ApiImplicitParam(name = "vvNumber", value = "VV_NAME",  dataType ="String"),
        @ApiImplicitParam(name = "name", value = "NAME",  dataType ="String"),
        @ApiImplicitParam(name = "browseNumber", value = "BROWSENUMBER",  dataType ="Int"),
        @ApiImplicitParam(name = "praiseNumber", value = "PRAISENUMBER",  dataType ="Int"),
        @ApiImplicitParam(name = "handleState", value = "HANDLESTATE",  dataType ="Int"),
        @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Int"),
    })
    @PostMapping(value = "/updateOpinionFeedback")
    @ResponseStatus(HttpStatus.OK)
    public Result updateOpinionFeedback(@RequestParam(value = "opinionType", required = false) Integer opinionType,
                                           @RequestParam(value = "problemDescription", required = false) String problemDescription,
                                           @RequestParam(value = "opinionProgramme", required = false) String opinionProgramme,
                                           @RequestParam(value = "contactInformation", required = false) Integer contactInformation,
                                           @RequestParam(value = "vvNumber", required = false) String vvNumber,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "browseNumber", required = false) int browseNumber,
                                           @RequestParam(value = "praiseNumber", required = false) int praiseNumber,
                                           @RequestParam(value = "handleState", required = false) int handleState,
                                           @RequestParam(value = "id") Integer id) {

        try {
            logger.info("opinionType :{}, problemDescription: {}, opinionProgramme: {}, " +
                    "contactInformation: {}, vvNumber: {}, name:{}, id:{}",
                opinionType, problemDescription, opinionProgramme, contactInformation, vvNumber, name,id);
            Map<String, Object> result =opinionFeedbackService.updateOpinionFeedback(  opinionType,  problemDescription, opinionProgramme, contactInformation,  vvNumber, name,  browseNumber,  praiseNumber,  handleState, id);
            return returnDataList(result);
        }catch (Exception e){
            logger.error(UPDATE_OPINION_FEEDBACK_ERROR.getMsg(),e);
            return error(UPDATE_OPINION_FEEDBACK_ERROR.getCode(), Status.UPDATE_OPINION_FEEDBACK_ERROR.getMsg());
        }
    }
}
