package lucky.yc.community.enums;

public enum CommentTypeEnum {

    QUESTION(1),
    COMMENT(2);

    private Integer type;

    /**
     * 判断评论类型
     * @param type 1回复问题，2回复评论
     * @return
     */
    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }


    public Integer getType () {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
