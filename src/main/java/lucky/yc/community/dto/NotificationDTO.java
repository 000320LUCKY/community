package lucky.yc.community.dto;

import lombok.Data;
import lucky.yc.community.model.Notification;
import lucky.yc.community.model.User;

@Data
public class NotificationDTO extends Notification {
    private String outerTitle;
    private String notifierName;
    private String typeName;

}
