package com.yocy.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yocy.system.mapper.SysRoleMenuMapper;
import com.yocy.system.model.bo.RolePermsBO;
import com.yocy.system.model.entity.SysRoleMenu;
import com.yocy.system.service.SysRoleMenuService;
import com.yocy.common.constant.RedisConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
* @author 25055
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2024-04-20 00:27:16
*/
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
    implements SysRoleMenuService {

    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void initRolePermsCache() { refreshRolePermsCache(); }

    @Override
    public void refreshRolePermsCache() {
        // 清理缓存
        redisTemplate.opsForHash().delete(RedisConstants.ROLE_PERMS_PREFIX, "*");

        List<RolePermsBO> rolePerms = this.baseMapper.getRolePermsList(null);
        if (CollectionUtil.isNotEmpty(rolePerms)) {
            rolePerms.forEach(item -> {
                String roleCode = item.getRoleCode();
                Set<String> perms = item.getPerms();
                redisTemplate.opsForHash().put(RedisConstants.ROLE_PERMS_PREFIX, roleCode, perms);
            });
        }
    }

    @Override
    public void refreshRolePermsCache(String oldRoleCode, String newRoleCode) {
        // 清理缓存
        redisTemplate.opsForHash().delete(RedisConstants.ROLE_PERMS_PREFIX, oldRoleCode);
        updateRolePermsCache(newRoleCode);
    }

    @Override
    public void refreshRolePermsCache(String roleCode) {
        // 清理缓存
        redisTemplate.opsForHash().delete(RedisConstants.ROLE_PERMS_PREFIX, roleCode);
        updateRolePermsCache(roleCode);
    }

    /**
     * 更新指定角色的权限缓存。
     *
     * @param roleCode 角色代码。
     */
    private void updateRolePermsCache(String roleCode) {
        List<RolePermsBO> rolePerms = baseMapper.getRolePermsList(roleCode);
        if (CollectionUtil.isNotEmpty(rolePerms)) {
            // 假设第一个元素总是代表角色的权限集合
            RolePermsBO firstOfRolePerms = rolePerms.get(0);
            if (firstOfRolePerms != null) {
                Set<String> perms = firstOfRolePerms.getPerms();
                // 更新缓存
                redisTemplate.opsForHash().put(RedisConstants.ROLE_PERMS_PREFIX, roleCode, perms);
            }
        }
    }

    @Override
    public List<Long> listMenuIdsByRoleId(Long roleId) {
        return this.baseMapper.listMenuIdsByRoleId(roleId);
    }
}




