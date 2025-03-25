package com.yocy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.system.dto.UserAuthInfo;
import com.yocy.system.model.bo.UserBO;
import com.yocy.system.model.bo.UserFormBO;
import com.yocy.system.model.bo.UserProfileBO;
import com.yocy.system.model.entity.SysUser;
import com.yocy.system.model.query.UserPageQuery;
import com.yocy.system.model.vo.UserExportVO;
import com.yocy.common.mybatis.annotation.DataPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_user(用户信息表)】的数据库操作Mapper
* @createDate 2024-04-20 00:27:20
* @Entity generator.domain.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 获取用户分页列表
     *
     * @param page        分页参数
     * @param queryParams 查询参数
     * @return {@link List < UserBO >}
     */
    @DataPermission(deptAlias = "u")
    Page<UserBO> getUserPage(Page<UserBO> page, UserPageQuery queryParams);

    /**
     * 获取用户表单详情
     *
     * @param userId 用户ID
     * @return {@link UserFormBO}
     */
    UserFormBO getUserDetail(Long userId);

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link UserAuthInfo}
     */
    UserAuthInfo getUserAuthInfo(String username);

    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return {@link List<UserExportVO>}
     */
    @DataPermission(deptAlias = "u")
    List<UserExportVO> listExportUsers(UserPageQuery queryParams);

    /**
     * 获取用户个人中心信息
     *
     * @param userId 用户ID
     * @return {@link UserProfileBO}
     */
    UserProfileBO getUserProfile(Long userId);
}




