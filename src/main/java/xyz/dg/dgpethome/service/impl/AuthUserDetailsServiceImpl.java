//package xyz.dg.dgpethome.service.impl;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import xyz.dg.dgpethome.mapper.SysUserMapper;
//import xyz.dg.dgpethome.model.po.AuthUser;
//import xyz.dg.dgpethome.model.po.SysUser;
//import xyz.dg.dgpethome.service.SysDictService;
//
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Collection;
//
///**
// * @author Dugong
// * @date 2021-09-30 8:24
// * @description
// * 要实现UserDetailsService接口，这个接口是security提供的
// **/
//@Service
//public class AuthUserDetailsServiceImpl implements UserDetailsService {
//    //数据库存放用户类型的字典id是1
//    private final Integer USERTYPEID = 1;
//    //数据库用户状态为31即启用
//    private final Integer USERSTATUS = 31;
//    @Resource
//    private SysUserMapper userMapper;
//
//    @Resource
//    private SysDictService DictServiceImpl;
//
//    /**
//     * 通过账号查找用户、角色的信息
//     * @param userAccount
//     * @return
//     * @throws UsernameNotFoundException
//     */
//    @Override
//    public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
//        //根据用户账户名查找用户
//        SysUser user =userMapper.getUserByUserAccount(userAccount);
//
//        if (user==null ){
//            //用户不存在
//            throw new UsernameNotFoundException("用户" + userAccount +"不存在！");
//            //return null;
//        }
//        System.out.println(user.toString());
//        if(!USERSTATUS.equals(user.getUserStatus())){
//            //用户状态不为31已停用
//            throw new UsernameNotFoundException("用户" + userAccount +"状态错误！");
//        }
//        //获取当前用户的角色id
//        Integer userRoleId = user.getUserRoleId();
//        System.out.println(userRoleId);
//        //查下字典
////        Dict dict = DictServiceImpl.selectByPrimaryKey(userRoleId);
////        System.out.println(dict);
////        if(!USERTYPEID.equals(dict.getDictParentId())){
////            //用户字典的角色id的父id不是用户类型
////            throw new UsernameNotFoundException("未知的用户类型！");
////            //return null;
////        }
//        //创建一个权限的集合
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        //添加获取权限
//        authorities.add(new SimpleGrantedAuthority(""+user.getUserRoleId()));
//        //把对象信息（账户名，密码，,状态,权限）存入对象，返回该对象，controller层直接调用
//        AuthUser user2 =new AuthUser(
//                user.getUserAccount(),
//                user.getUserPassword(),
//                user.getUserStatus(),
//                authorities);
//        System.out.println("用户信息："+user2.toString());
//        return user2;
//
//    }
//}
