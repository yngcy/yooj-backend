package com.yocy.system.listener.excel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yocy.system.converter.UserConverter;
import com.yocy.system.model.entity.SysRole;
import com.yocy.system.model.entity.SysUser;
import com.yocy.system.model.entity.SysUserRole;
import com.yocy.system.model.vo.UserImportVO;
import com.yocy.system.service.SysRoleService;
import com.yocy.system.service.SysUserRoleService;
import com.yocy.system.service.SysUserService;
import com.yocy.common.base.IBaseEnum;
import com.yocy.common.constant.SystemConstants;
import com.yocy.common.enums.GenderEnum;
import com.yocy.common.enums.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户导入监听器
 * <br/>
 * 参考：<a href="https://easyexcel.opensource.alibaba.com/docs/current/quickstart/read">https://easyexcel.opensource.alibaba.com/docs/current/quickstart/read</a>
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Slf4j
public class UserImportListener extends MyAnalysisEventListener<UserImportVO> {

    /**
     * 有效条数
     */
    private int validCount;

    /**
     * 无效条数
     */
    private int invalidCount;

    /**
     * 导入返回信息
     */
    StringBuilder msg = new StringBuilder();

    private final Long deptId;

    private final SysUserService userService;

    private final PasswordEncoder passwordEncoder;

    private final UserConverter userConverter;

    private final SysRoleService roleService;

    private final SysUserRoleService userRoleService;

    public UserImportListener(Long deptId) {
        this.deptId = deptId;
        this.userService = SpringUtil.getBean(SysUserService.class);
        this.passwordEncoder = SpringUtil.getBean(PasswordEncoder.class);
        this.userConverter = SpringUtil.getBean(UserConverter.class);
        this.roleService = SpringUtil.getBean(SysRoleService.class);
        this.userRoleService = SpringUtil.getBean(SysUserRoleService.class);
    }


    @Override
    public String getMsg() {
        // 总结信息
        String summaryMsg = StrUtil.format("导入用户结束：成功{}条，失败{}条，<br/>{}", validCount, invalidCount, msg);
        return summaryMsg;
    }

    @Override
    public void invoke(UserImportVO importVO, AnalysisContext analysisContext) {
        log.info("解析到一条用户数据:{}", JSONUtil.toJsonStr(importVO));
        // 校验数据
        StringBuilder validationMsg = new StringBuilder();

        String username = importVO.getUsername();
        if (StrUtil.isBlank(username)) {
            validationMsg.append("用户名为空；");
        } else {
            long count = userService.count(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, username));
            if (count > 0) {
                validationMsg.append("用户名已存在；");
            }
        }

        String nickname = importVO.getNickname();
        if (StrUtil.isBlank(nickname)) {
            validationMsg.append("用户昵称为空；");
        }

        String mobile = importVO.getMobile();
        if (StrUtil.isBlank(mobile)) {
            validationMsg.append("手机号码为空");
        } else {
            if (!Validator.isMobile(mobile)) {
                validationMsg.append("手机号码不正确；");
            }
        }

        if (validationMsg.length() == 0) {
            // 校验通过，持久化至数据库
            SysUser entity = userConverter.importVO2Entity(importVO);
            entity.setDeptId(deptId);   // 部门
            entity.setPassword(passwordEncoder.encode(SystemConstants.DEFAULT_PASSWORD));   // 默认密码
            // 性别翻译
            String genderLabel = importVO.getGender();
            if (StrUtil.isNotBlank(genderLabel)) {
                Integer genderValue = (Integer) IBaseEnum.getValueByLabel(genderLabel, GenderEnum.class);
                entity.setGender(genderValue);
            }

            // 角色解析
            String roleCodes = importVO.getRoleCodes();
            List<Long> roleIds = null;
            if (StrUtil.isNotBlank(roleCodes)) {
                roleIds = roleService.list(
                                new LambdaQueryWrapper<SysRole>()
                                        .in(SysRole::getCode, roleCodes.split(","))
                                        .eq(SysRole::getStatus, StatusEnum.ENABLE.getValue())
                                        .select(SysRole::getId)
                        ).stream()
                        .map(role -> role.getId())
                        .collect(Collectors.toList());
            }


            boolean saveResult = userService.save(entity);
            if (saveResult) {
                validCount++;
                // 保存用户角色关联
                if (CollectionUtil.isNotEmpty(roleIds)) {
                    List<SysUserRole> userRoles = roleIds.stream()
                            .map(roleId -> new SysUserRole(entity.getId(), roleId))
                            .collect(Collectors.toList());
                    userRoleService.saveBatch(userRoles);
                }
            } else {
                invalidCount++;
                msg.append("第" + (validCount + invalidCount) + "行数据保存失败；<br/>");
            }
        } else {
            invalidCount++;
            msg.append("第" + (validCount + invalidCount) + "行数据校验失败：").append(validationMsg + "<br/>");
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 所有数据解析完成后调用

    }
}
