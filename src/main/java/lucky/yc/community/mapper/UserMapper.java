package lucky.yc.community.mapper;

import lucky.yc.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    //            通过id查找user所有属性
    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);

    //通过accountid查找user所有属性
    @Select("select * from user where  account_id = #{creatorId}")
    User findByAccountId(@Param("creatorId") String accountId);

    @Update("update user set name = #{name},token = #{token}, gmt_modified = #{gmtModified}, avatar_url = #{avatarUrl} where id = #{id}")
    void update(User user);
}
