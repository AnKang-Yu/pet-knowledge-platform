package xyz.dg.dgpethome.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.service.SysDictService;
import xyz.dg.dgpethome.service.SysUserService;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: dgpethome
 * @description:
 * @author: ruihao_ji
 * @create: 2022-06-13 16:42
 **/
@Service
public class AuthUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private SysUserService sysUserServiceImpl;
    @Resource
    private SysDictService sysDictServiceImpl;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser =  sysUserServiceImpl.loadUserByUsername(username);
        if(sysUser == null){
            throw new UsernameNotFoundException(String.format("%s. 用户不存在", username));
        }else{
            // 查找角色
            String role = sysDictServiceImpl.loadRoleByUserRoleId(sysUser.getUserRoleId()).getDictCode();
            List<SimpleGrantedAuthority> authorities = new LinkedList<>();
            authorities.add(new SimpleGrantedAuthority(role));
            sysUser.setAuthorities(authorities);
            return sysUser;
        }

    }
}

