package com.yocy.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.system.dto.UserAuthInfo;
import com.yocy.system.model.entity.SysUser;
import com.yocy.system.model.form.UserForm;
import com.yocy.system.model.form.UserRegisterForm;
import com.yocy.system.model.query.UserPageQuery;
import com.yocy.system.model.vo.UserExportVO;
import com.yocy.system.model.vo.UserInfoVO;
import com.yocy.system.model.vo.UserPageVO;
import com.yocy.system.model.vo.UserProfileVO;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_user(用户信息表)】的数据库操作Service
* @createDate 2024-04-20 00:27:20
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户分页列表
     *
     * @return {@link IPage < UserPageVO >}
     */
    Page<UserPageVO> getUserPage(UserPageQuery queryParams);


    /**
     * 获取用户表单数据
     *
     * @param userId 用户ID
     * @return {@link UserForm}
     */
    UserForm getUserForm(Long userId);


    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return {@link Boolean}
     */
    boolean saveUser(UserForm userForm);

    /**
     * 修改用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单对象
     * @return {@link Boolean}
     */
    boolean updateUser(Long userId, UserForm userForm);


    /**
     * 删除用户
     *
     * @param idsStr 用户ID，多个以英文逗号(,)分割
     * @return {@link Boolean}
     */
    boolean deleteUsers(String idsStr);


    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param password 用户密码
     * @return {@link Boolean}
     */
    boolean updatePassword(Long userId, String password);

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
     * @return {@link List <UserExportVO>}
     */
    List<UserExportVO> listExportUsers(UserPageQuery queryParams);


    /**
     * 获取登录用户信息
     *
     * @return {@link UserInfoVO}
     */
    UserInfoVO getCurrentUserInfo();

    /**
     * 注销登出
     *
     * @return {@link Boolean}
     */
    boolean logout();

    /**
     * 注册用户
     *
     * @param userRegisterForm 用户注册表单对象
     * @return {@link Boolean}
     */
    boolean registerUser(UserRegisterForm userRegisterForm);


    /**
     * 发送注册短信验证码
     *
     * @param mobile 手机号
     * @return {@link Boolean} 是否发送成功
     */
    boolean sendRegistrationSmsCode(String mobile);


    /**
     * 获取用户个人中心信息
     *
     * @return {@link UserProfileVO}
     */
    UserProfileVO getUserProfile();
}
