package lucky.yc.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你的问题找不到了，要不要换一个试试？"),
    TARGET_PATAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试！"),
    SYS_ERROR(2004,"服务冒烟了，要不然稍等下试试!!!"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在！"),
    COMMENT_NOT_FOUND(2006,"你回复的评论不存在，要不要换一个试试？"),
    CONTENT_IS_EMPTY(2007,"输入的内容不能为空！")
    ;

    private String message;
    private Integer code;

    /**
     *
     * @param code 错误码
     * @param message 错误信息
     */
    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer  getCode() {
        return code;
    }
}
