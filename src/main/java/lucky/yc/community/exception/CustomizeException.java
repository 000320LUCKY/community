package lucky.yc.community.exception;

public class CustomizeException extends RuntimeException{
    private String message;
    private Integer code;

    /**
     * 自定义异常
     * @param errorCode 错误信息
     */
    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }


    public Integer getCode() {
        return code;
    }
}
