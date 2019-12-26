package cn.escheduler.dao.model;


/**
 * command
 */

public class AlertGroupUserNames {
    /**
     * id
     */
    private int id;

    /**
     * alert group name
     */
    private String groupName;

    /**
     * user names
     */
    private String userNames;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    @Override
    public String toString() {
        return "AlertGroupUserNames{" +
            "id=" + id +
            ", groupName='" + groupName + '\'' +
            ", userNames='" + userNames + '\'' +
            '}';
    }
}
