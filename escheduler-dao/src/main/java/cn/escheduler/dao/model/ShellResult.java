package cn.escheduler.dao.model;


import java.util.List;

/**
 * @author zhougenggeng createTime  2019/9/29
 */


public class ShellResult {
    private boolean success;
    private String resultMessage;
    private int exitValue;
    private List<String> stdOut;
    private List<String> stdErr;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public int getExitValue() {
        return exitValue;
    }

    public void setExitValue(int exitValue) {
        this.exitValue = exitValue;
    }

    public List<String> getStdOut() {
        return stdOut;
    }

    public void setStdOut(List<String> stdOut) {
        this.stdOut = stdOut;
    }

    public List<String> getStdErr() {
        return stdErr;
    }

    public ShellResult() {
    }

    @Override
    public String toString() {
        return "ShellResult{" +
            "success=" + success +
            ", resultMessage='" + resultMessage + '\'' +
            ", exitValue=" + exitValue +
            ", stdOut=" + stdOut +
            ", stdErr=" + stdErr +
            '}';
    }

    public ShellResult(boolean success, String resultMessage, int exitValue, List<String> stdOut,
                       List<String> stdErr) {
        this.success = success;
        this.resultMessage = resultMessage;
        this.exitValue = exitValue;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    public void setStdErr(List<String> stdErr) {
        this.stdErr = stdErr;
    }

    public static ShellResult buildSuccessResult(int exitValue,
                                                 List<String> stdOut,
                                                 List<String> stdErr) {
        ShellResult shellResult = new ShellResult();
        shellResult.setSuccess(true);
        shellResult.setResultMessage("");
        shellResult.setExitValue(exitValue);
        shellResult.setStdOut(stdOut);
        shellResult.setStdErr(stdErr);

        return shellResult;
    }

    public static ShellResult buildFailResult(String resultMessage) {
        ShellResult shellResult = new ShellResult();
        shellResult.setSuccess(false);
        shellResult.setResultMessage(resultMessage);

        return shellResult;
    }

}
