package com.yocy.oj.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yocy.oj.ums.model.entity.UmsAddress;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 25055
* @description 针对表【ums_address(会员地址表)】的数据库操作Mapper
* @createDate 2024-04-28 10:09:51
* @Entity generator.domain.UmsAddress
*/
public interface UmsAddressMapper extends BaseMapper<UmsAddress> {

    @Select("<script>" +
            "SELECT * FROM ums_address WHERE member_id =#{userId}" +
            "</script>")
    List<UmsAddress> listByUserId(Long userId);
}




