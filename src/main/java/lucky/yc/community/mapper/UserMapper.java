package lucky.yc.community.mapper;

import lucky.yc.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    //通过id和accountid查找user所有属性
    @Select("select * from user where account_id = #{id} and id = #{creatorid}")
    User findByAccountId(@Param("id") String id, @Param("creatorid")Integer creatorid);

    //通过accountid查找user所有属性
    @Select("select * from user where  account_id = #{creator}")
    User findById(@Param("creator")String creator);
}
