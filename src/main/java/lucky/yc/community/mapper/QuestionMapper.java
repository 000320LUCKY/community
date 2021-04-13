package lucky.yc.community.mapper;

import lucky.yc.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tags) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tags})")
    void create(Question question);

//    查询question所有字段
    @Select("select * from question")
    List<Question> list();
}
