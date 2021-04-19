package lucky.yc.community.mapper;

import lucky.yc.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    //    通过id查询question所有字段
    @Select("select * from question where id = #{id}")
    Question getById(@Param(value = "id") Integer id);

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tags,creator_id) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tags},#{creator_id})")
    void create(Question question);

//    查询question所有字段
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer page, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    //    通过userid查询question所有字段
    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId")String userId, @Param(value = "offset") Integer page, @Param(value = "size") Integer size);

    //    通过userid查询question字段数目
    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param(value = "userId")String userId);
}
