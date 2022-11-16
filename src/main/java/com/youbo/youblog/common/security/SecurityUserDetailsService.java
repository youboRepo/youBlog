package com.youbo.youblog.common.security;

import com.youbo.youblog.api.user.entity.UserCustom;
import com.youbo.youblog.api.user.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户核心接口实现
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Resource
    private UserService userService;

    /*@Resource
    private PermissionService permissionService;*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.hasText(username, "用户名不能为空");

        // 获取用户对象
        UserCustom user = userService.getByUsername(username);

        Assert.notNull(user, "用户名不存在");
        Assert.isTrue(user.getEnabled(), "用户已禁用");

        // 用户角色权限
        //Integer userId = isAdmin ? null : user.getId();
        List<GrantedAuthority> authorities = new ArrayList<>();
        //permissionService.getAuthPermissionCode(userId).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        // 创建安全用户
        SecurityUser securityUser = new SecurityUser(user, authorities);
        securityUser.setId(user.getId());
        securityUser.setName(user.getName());

        return securityUser;
    }
}
