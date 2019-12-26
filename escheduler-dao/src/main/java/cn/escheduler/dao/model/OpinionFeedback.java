package cn.escheduler.dao.model;

import java.util.Date;

/**
 * opinion feedback
 */
public class OpinionFeedback {
    /**
     * id
     */
    private int id;

    /**
     * opinionType
     */
    private int opinionType;

    /**
     * problemDescription
     */
    private String problemDescription;

    /**
     * opinionProgramme
     */
    private String opinionProgramme;

    /**
     * contactInformation
     */
    private int contactInformation;

    /**
     * vvNumber
     */
    private String vvNumber;

    /**
     * name
     */
    private String name;

    /**
     * browseNumber
     */
    private int browseNumber;

    /**
     * praiseNumber
     */
    private int praiseNumber;

    /**
     * handleState
     */
    private int handleState;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateTime
     */
    private Date updateTime;

    public OpinionFeedback() {
    }

    public OpinionFeedback(int id, int opinionType, String problemDescription, String opinionProgramme,
                           int contactInformation, String vvNumber, String name, int browseNumber, int praiseNumber,
                           int handleState, Date createTime, Date updateTime) {
        this.id = id;
        this.opinionType = opinionType;
        this.problemDescription = problemDescription;
        this.opinionProgramme = opinionProgramme;
        this.contactInformation = contactInformation;
        this.vvNumber = vvNumber;
        this.name = name;
        this.browseNumber = browseNumber;
        this.praiseNumber = praiseNumber;
        this.handleState = handleState;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpinionType() {
        return opinionType;
    }

    public void setOpinionType(int opinionType) {
        this.opinionType = opinionType;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getOpinionProgramme() {
        return opinionProgramme;
    }

    public void setOpinionProgramme(String opinionProgramme) {
        this.opinionProgramme = opinionProgramme;
    }

    public int getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(int contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getVvNumber() {
        return vvNumber;
    }

    public void setVvNumber(String vvNumber) {
        this.vvNumber = vvNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrowseNumber() {
        return browseNumber;
    }

    public void setBrowseNumber(int browseNumber) {
        this.browseNumber = browseNumber;
    }

    public int getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(int praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public int getHandleState() {
        return handleState;
    }

    public void setHandleState(int handleState) {
        this.handleState = handleState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "OpinionFeedback{" +
            "id=" + id +
            ", opinionType=" + opinionType +
            ", problemDescription='" + problemDescription + '\'' +
            ", opinionProgramme='" + opinionProgramme + '\'' +
            ", contactInformation=" + contactInformation +
            ", vvNumber='" + vvNumber + '\'' +
            ", name='" + name + '\'' +
            ", browseNumber=" + browseNumber +
            ", praiseNumber=" + praiseNumber +
            ", handleState=" + handleState +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }
}
