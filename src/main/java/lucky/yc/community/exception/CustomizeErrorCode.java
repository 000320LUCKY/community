package lucky.yc.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND("服务太热了，要不然稍等下试试!!!");

    private String message;

    CustomizeErrorCode (String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
