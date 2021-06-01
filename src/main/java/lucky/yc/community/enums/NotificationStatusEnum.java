package lucky.yc.community.enums;

public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1)
    ;

    private Integer status;

    NotificationStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
