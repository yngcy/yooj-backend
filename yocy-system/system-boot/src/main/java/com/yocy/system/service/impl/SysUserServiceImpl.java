package com.yocy.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yocy.system.converter.UserConverter;
import com.yocy.system.dto.UserAuthInfo;
import com.yocy.system.mapper.SysUserMapper;
import com.yocy.system.model.bo.UserBO;
import com.yocy.system.model.bo.UserFormBO;
import com.yocy.system.model.bo.UserProfileBO;
import com.yocy.system.model.entity.SysUser;
import com.yocy.system.model.form.UserForm;
import com.yocy.system.model.form.UserRegisterForm;
import com.yocy.system.model.query.UserPageQuery;
import com.yocy.system.model.vo.UserExportVO;
import com.yocy.system.model.vo.UserInfoVO;
import com.yocy.system.model.vo.UserPageVO;
import com.yocy.system.model.vo.UserProfileVO;
import com.yocy.system.service.SysRoleService;
import com.yocy.system.service.SysUserService;
import com.yocy.system.service.SysUserRoleService;
import com.yocy.common.constant.GlobalConstants;
import com.yocy.common.constant.RedisConstants;
import com.yocy.common.constant.SystemConstants;
import com.yocy.common.security.service.PermissionService;
import com.yocy.common.security.utils.SecurityUtils;
import com.yocy.common.sms.property.AliyunSmsProperties;
import com.yocy.common.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 25055
 * @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
 * @createDate 2024-04-20 00:27:20
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    private final PasswordEncoder passwordEncoder;

    private final SysUserRoleService userRoleService;

    private final SysRoleService roleService;

    private final UserConverter userConverter;

    private final PermissionService permissionService;

    private final SmsService smsService;

    private final AliyunSmsProperties aliyunSmsProperties;

    private final StringRedisTemplate redisTemplate;


    @Override
    public Page<UserPageVO> getUserPage(UserPageQuery queryParams) {
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();
        Page<UserBO> page = new Page<>(pageNum, pageSize);

        // 查询数据
        Page<UserBO> userBoPage = this.baseMapper.getUserPage(page, queryParams);

        // 实体转换
        return userConverter.bo2VO(userBoPage);
    }

    @Override
    public UserForm getUserForm(Long userId) {
        UserFormBO userFormBO = this.baseMapper.getUserDetail(userId);
        return userConverter.bo2Form(userFormBO);
    }

    @Override
    public boolean saveUser(UserForm userForm) {

        String username = userForm.getUsername();

        long count = this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        Assert.isTrue(count == 0, "用户名已存在");

        // 实体转换 form->entity
        SysUser entity = userConverter.form2Entity(userForm);

        // 设置默认加密密码
        String defaultEncryptPwd = passwordEncoder.encode(SystemConstants.DEFAULT_PASSWORD);
        entity.setPassword(defaultEncryptPwd);

        // 新增用户
        boolean result = this.save(entity);

        if (result) {
            // 保存用户角色
            userRoleService.saveUserRoles(entity.getId(), userForm.getRoleIds());
        }
        return result;
    }

    @Override
    public boolean updateUser(Long userId, UserForm userForm) {
        String username = userForm.getUsername();

        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .ne(SysUser::getId, userId)
        );
        Assert.isTrue(count == 0, "用户名已存在");

        // form -> entity
        SysUser entity = userConverter.form2Entity(userForm);

        // 修改用户
        boolean result = this.updateById(entity);

        if (result) {
            // 保存用户角色
            userRoleService.saveUserRoles(entity.getId(), userForm.getRoleIds());
        }
        return result;
    }

    @Override
    public boolean deleteUsers(String idsStr) {
        List<Long> ids = Arrays.stream(idsStr.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(ids);
    }

    @Override
    public boolean updatePassword(Long userId, String password) {
        return this.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPassword, passwordEncoder.encode(password))
        );
    }

    @Override
    public UserAuthInfo getUserAuthInfo(String username) {
        UserAuthInfo userAuthInfo = this.baseMapper.getUserAuthInfo(username);
        if (userAuthInfo != null) {
            Set<String> roles = userAuthInfo.getRoles();
            if (CollectionUtil.isNotEmpty(roles)) {
                // 获取最大范围的数据权限(目前设定DataScope越小，拥有的数据权限范围越大，所以获取得到角色列表中最小的DataScope)
                Integer dataScope = roleService.getMaxDataRangeDataScope(roles);
                userAuthInfo.setDataScope(dataScope);
            }
        }
        return userAuthInfo;
    }

    @Override
    public List<UserExportVO> listExportUsers(UserPageQuery queryParams) {
        return this.baseMapper.listExportUsers(queryParams);
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        // 登录用户entity
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, SecurityUtils.getUsername())
                .select(
                        SysUser::getId,
                        SysUser::getNickname,
                        SysUser::getAvatar));
        // entity->VO
        UserInfoVO userInfoVO = userConverter.entity2UserInfoVO(user);

        // 获取用户角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfoVO.setRoles(roles);

        // 获取用户权限集合
        if (CollectionUtil.isNotEmpty(roles)) {
            Set<String> perms = permissionService.getRolePermsFormCache(roles);
            userInfoVO.setPerms(perms);
        }

        return userInfoVO;
    }

    @Override
    public boolean logout() {
        String jti = SecurityUtils.getJti();
        // 使用Optional处理可能的null值
        Optional<Long> expireTimeOpt = Optional.ofNullable(SecurityUtils.getExp());

        // 当前时间（单位：秒）
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        expireTimeOpt.ifPresent(expireTime -> {
            if (expireTime > currentTimeInSeconds) {
                // token 未过期，添加至缓存黑名单，缓存时间为 token 剩余的有效时间
                long remainingTimeInSeconds = expireTime - currentTimeInSeconds;
                redisTemplate.opsForValue().set(RedisConstants.TOKEN_BLACKLIST_PREFIX + jti, "", remainingTimeInSeconds, TimeUnit.SECONDS);
            }
        });

        if (expireTimeOpt.isEmpty()) {
            // token 永不过期，则永久加入黑名单
            redisTemplate.opsForValue().set(RedisConstants.TOKEN_BLACKLIST_PREFIX + jti, "");
        }
        return true;
    }

    @Override
    public boolean registerUser(UserRegisterForm userRegisterForm) {
        String mobile = userRegisterForm.getMobile();
        String code = userRegisterForm.getCode();
        // 校验验证码
        String cacheCode = redisTemplate.opsForValue().get(RedisConstants.REGISTER_SMS_CODE_PREFIX + mobile);
        if (!StrUtil.equals(code, cacheCode)) {
            log.warn("验证码不匹配或不存在: {}", mobile);
            return false; // 验证码不匹配或不存在时返回false
        }
        // 校验通过，删除验证码
        redisTemplate.delete(RedisConstants.REGISTER_SMS_CODE_PREFIX + mobile);

        // 校验手机号是否已注册
        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getMobile, mobile)
                .or()
                .eq(SysUser::getUsername, mobile)
        );
        Assert.isTrue(count == 0, "手机号已注册");

        SysUser entity = new SysUser();
        entity.setUsername(mobile);
        entity.setMobile(mobile);
        entity.setStatus(GlobalConstants.STATUS_YES);

        // 设置默认加密密码
        String defaultEncryptPwd = passwordEncoder.encode(SystemConstants.DEFAULT_PASSWORD);
        entity.setPassword(defaultEncryptPwd);

        // 新增用户，并直接返回结果
        return this.save(entity);
    }

    @Override
    public boolean sendRegistrationSmsCode(String mobile) {
        // 获取短信模板代码
        String templateCode = aliyunSmsProperties.getTemplateCodes().get("register");

        // 生成随机4位验证码
        String code = RandomUtil.randomNumbers(4);

        // 短信模板：您的验证码：${code}，该验证码5分钟内有效，请勿泄露给其他人。
        // 其中 ${code} 是模板参数，使用时需要替换成实际的值。
        String templateParams = JSONUtil.toJsonStr(Collections.singletonMap("code", code));

        boolean result = smsService.sendSms(mobile, templateCode, templateParams);
        if (result) {
            // 将验证码存入redis，有效期5分钟
            redisTemplate.opsForValue().set(RedisConstants.REGISTER_SMS_CODE_PREFIX + mobile, code, 5, TimeUnit.MINUTES);

            // TODO 考虑记录每次发送短信信息的详情，如发送时间、手机号、短信内容等，以便后续审核分析短信发送效果
        }

        return result;
    }

    @Override
    public UserProfileVO getUserProfile() {
        Long userId = SecurityUtils.getUserId();
        // 获取用户个人中心信息
        UserProfileBO userProfileBO = this.baseMapper.getUserProfile(userId);
        return userConverter.userProfileBO2VO(userProfileBO);
    }
}




