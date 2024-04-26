package com.yocy.common.security.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.yocy.common.constant.RedisConstants;
import com.yocy.common.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import java.util.*;

/**
 * SpringSecurity 权限校验
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Service("ss")
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean hasPerm(String requirePerm) {

        if (StrUtil.isBlank(requirePerm)) {
            return false;
        }
        // 超级管理员，放行
        if (SecurityUtils.isRoot()) {
            return true;
        }

        // 获取当前登录用户的角色编码集合
        Set<String> roleCodes = SecurityUtils.getRoles();
        if (CollectionUtil.isEmpty(roleCodes)) {
            return false;
        }

        // 获取当前登录用户的所有角色的权限列表
        Set<String> rolePerms = this.getRolePermsFormCache(roleCodes);
        if (CollectionUtil.isEmpty(rolePerms)) {
            return false;
        }

        // 判断当前登录用户的所有角色的权限列表中是否包含所需权限
        boolean hasPermission = rolePerms.stream()
                .anyMatch(rolePerm ->
                        // 匹配权限，支持通配符
                        PatternMatchUtils.simpleMatch(rolePerm, requirePerm));

        if (!hasPermission) {
            log.error("用户无操作权限");
        }
        return hasPermission;
    }

    /**
     * 从缓存中根据角色代码集获取对应的角色权限集合。
     *
     * @param roleCodes 角色代码的集合，用于查询角色对应的权限。
     * @return 返回一个字符串集，包含指定角色代码集的所有权限代码。
     */
    public Set<String> getRolePermsFormCache(Set<String> roleCodes) {
        if (CollectionUtil.isEmpty(roleCodes)) {
            return Collections.emptySet();
        }

        Set<String> perms = new HashSet<>();
        // 从缓存中一次性获取所有角色的权限
        Collection<Object> roleCodesAsObjects = new ArrayList<>(roleCodes);
        List<Object> rolePermsList = redisTemplate.opsForHash().multiGet(RedisConstants.ROLE_PERMS_PREFIX, roleCodesAsObjects);

        for (Object rolePermsObject : rolePermsList) {
            if (rolePermsObject instanceof Set) {
                Set<String> rolePerms = (Set<String>) rolePermsObject;
                perms.addAll(rolePerms);
            }
        }
        return perms;
    }
}
