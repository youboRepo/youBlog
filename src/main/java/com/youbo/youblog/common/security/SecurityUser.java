package com.youbo.youblog.common.security;

import com.alibaba.fastjson.annotation.JSONField;
import com.youbo.youblog.api.user.entity.UserCustom;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 安全用户对象
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SecurityUser implements UserDetails {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 1L;

    private UserCustom user;

    /**
     * 用户标识
     */
    private Integer id;

    /**
     * 用户名字
     */
    private String name;

    private List<GrantedAuthority> authorities;

    public SecurityUser() {

    }

    public SecurityUser(UserCustom user, List<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
