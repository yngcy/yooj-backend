package com.yocy.oj.ums.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.oj.ums.model.entity.UmsMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 25055
* @description 针对表【ums_member(会员表)】的数据库操作Mapper
* @createDate 2024-04-28 10:07:00
* @Entity com.yocy.oj.ums.model.entity.UmsMember
*/
public interface UmsMemberMapper extends BaseMapper<UmsMember> {

    @Select("<script>" +
            " SELECT * from ums_member " +
            " <if test ='nickname !=null and nickname.trim() neq \"\" ' >" +
            "       WHERE nick_name like concat('%',#{nickname},'%')" +
            " </if>" +
            " ORDER BY update_time DESC, create_time DESC" +
            "</script>")
    @Results({
            @Result(id = true, column = "id", property = "id"),
    })
    List<UmsMember> list(Page<UmsMember> page, String nickname);
}




