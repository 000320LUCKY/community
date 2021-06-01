package lucky.yc.community.service;

import lucky.yc.community.dto.NotificationDTO;
import lucky.yc.community.dto.PaginationDTO;
import lucky.yc.community.enums.NotificationStatusEnum;
import lucky.yc.community.enums.NotificationTypeEnum;
import lucky.yc.community.exception.CustomizeErrorCode;
import lucky.yc.community.exception.CustomizeException;
import lucky.yc.community.mapper.NotificationMapper;
import lucky.yc.community.mapper.UserMapper;
import lucky.yc.community.model.Notification;
import lucky.yc.community.model.NotificationExample;
import lucky.yc.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        //列表分页对象
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        //        totalPage表示分页页面数目
        Integer totalPage;
        Integer offset = 0;
//        数据表条目数
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //        小于1 page等于1
        if (page < 1) {
            page = 1;
            paginationDTO.setPage(page);
        }
//        大于总页数 page等于最大页码
        if (page > totalPage) {
            page = totalPage;
        }
        //        分页逻辑
        paginationDTO.setPaginations(totalPage, page);

//        size*(page-1) 偏移量
        offset = size * (page - 1);
        //    通过userid分页条件查询question包含的所有字段
        NotificationExample example = new NotificationExample();
        RowBounds rowBounds = new RowBounds(offset, size);
        example.createCriteria().andReceiverEqualTo(userId);
//        排序
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, rowBounds);
        if (notifications.size() == 0) {
            return paginationDTO;
        }
////        取出notifications的所有通知人
//        Set<Long> disUserIds = notifications.stream().map(notify -> notify.getNotifier()).collect(Collectors.toSet());
//        List<Long> userIds = new ArrayList<>(disUserIds);
//
////        查出所有要通知的人
//        UserExample userExample = new UserExample();
//        userExample.createCriteria().andIdIn(userIds);
//        List<User> users = userMapper.selectByExample(userExample);
//        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));

        //        回复通知列表
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

//        将分页后的数据设置到分页DTO里
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }


    /**
     * 未读通知数
     *
     * @param userId 用户id
     * @return 返回未读通知数
     */
    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    /**
     * 回复通知以读方法
     *
     * @param id   通知的id
     * @param user 被通知用户
     * @return 返回通知实体
     */
    public NotificationDTO read(Long id, User user) {
//        查询对应通知
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
//        校验是否是本人查询
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

//        设置通知为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
//        设置是回复了问题还是评论
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
