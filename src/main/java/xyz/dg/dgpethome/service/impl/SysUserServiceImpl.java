package xyz.dg.dgpethome.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.AuthenticationException;


import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.mapper.SysUserMapper;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.myexceptions.MyAuthenticationException;
import xyz.dg.dgpethome.service.SysUserService;

import java.util.List;

/**
 * @author  Dugong
 * @date  2021-10-03 1:20
 * @description
 **/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper,SysUser> implements SysUserService {

//    @Autowired
//    BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;
    @Resource
    private  SysUserMapper sysUserMapper;
    //用户状态   字典里id是31
    private static final Integer USER_STATE = 31;
    /**
     * 通过账号查询用户
     * @param userName
     * @return
     */
    @Override
    public SysUser getUserByUserUsername(String userName) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询条件：全匹配用户名，和状态为1的账号
        lambdaQueryWrapper
                .eq(SysUser::getUserName,userName);
                //.eq(SysUser::getUserStatus,USER_STATE);
        //用getOne查询一个对象出来
        SysUser user = this.baseMapper.selectOne(lambdaQueryWrapper);
        return  user;
    }

    /**
     * 验证登录
     * @param userName
     * @param rawPassword
     * @return
     * @throws AuthenticationException
     */
    @Override
    public SysUser checkLogin(String userName, String rawPassword) throws MyAuthenticationException {
        //判断用户是否存在，如果存在且密码正确，则保存该用户对象
        SysUser user = this.getUserByUserUsername(userName);
        if(user == null){
            //System.out.println("checkLogin--------->账号不存在，请重新尝试！");
            //设置友好提示
            throw  new MyAuthenticationException("账号不存在，请重新尝试！");
        }
        //31是启用用户状态
        System.out.println(user.getUserStatus());
        if( !USER_STATE.equals(user.getUserStatus()) ){
            //System.out.println("checkLogin--------->账户已停用，请更换账户尝试！");
            //设置友好提示
            throw  new MyAuthenticationException("账户状态非启用，请更换账户尝试！");
        }
        //获取数据库里加密的密码
        String encodedPassword = user.getUserPassword();
        //String salt = user.getUserSalt();
        // md5加盐加密: md5(md5(str) + md5(salt))
        //和加密后的密码进行比配
        if(!encodedPassword.equals(SaSecureUtil.sha256(rawPassword))) {
            //System.out.println("checkLogin--------->密码不正确！");
            //设置友好提示
            throw new MyAuthenticationException("密码不正确！");
        }

        return  user;
    }
//    @Override
//    public IPage<SysUserVo> testPage(SysUserPageParam sysUserPageParam){
//        IPage<SysUserVo> userVoIPage = new Page<>();
//        userVoIPage.setPages(sysUserPageParam.getPageSize());
//        userVoIPage.setCurrent(sysUserPageParam.getCurrentPage());
//        List<SysUserVo> list = sysUserMapper.testPage(sysUserPageParam);
//        System.out.println(list.toString());
//
//        //List<SysUserVo> newList = (List<SysUserVo>) list.stream().map(item->BeanUtil.copyProperties(item,SysUserVo.class));
//        userVoIPage.setRecords(list);
//
//
//        return   userVoIPage;
//    }
    /**
     * 分页
     * @param sysUserPageParam
     * @return
     */
    @Override
    public IPage<SysUserVo> findUserList(SysUserPageParam sysUserPageParam) {
        //旧方式不能连接多表
//        //原分页对象
//        IPage<SysUser> userPage = new Page<>();
//        userPage.setPages(sysUserPageParam.getPageSize());
//        userPage.setCurrent(sysUserPageParam.getCurrentPage());
//        //构造用户表查询条件
//        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
//        //用户名不为空就查用户名
//        String userName  = sysUserPageParam.getUserName();
//        //要查询的用户id
//        Integer userRoleId = sysUserPageParam.getUserRoleId();
//        if(userRoleId != null){
//            queryWrapper.lambda().eq(SysUser::getUserRoleId,userRoleId);
//        }
//        if(StringUtils.isNotEmpty(userName)){
//            queryWrapper.lambda().like(SysUser::getUserName,userName);
//        }
//        return  this.baseMapper.selectPage(userPage,queryWrapper)
//                .convert(item-> BeanUtil.copyProperties(item,SysUserVo.class));

        IPage<SysUserVo> userVoIPage = new Page<>();
        userVoIPage.setPages(sysUserPageParam.getPageSize());
        userVoIPage.setCurrent(sysUserPageParam.getCurrentPage());
        
        List<SysUserVo> list = sysUserMapper.findUserList(sysUserPageParam);
        System.out.println(list.toString());

        //List<SysUserVo> newList = (List<SysUserVo>) list.stream().map(item->BeanUtil.copyProperties(item,SysUserVo.class));
        userVoIPage.setRecords(list);

        return   userVoIPage;
    }

    /**
     * 密码加密
     * @param sysUser
     * @return
     */
    @Override
    public SysUser passwordToEncode(SysUser sysUser) {
        sysUser.setUserPassword(SaSecureUtil.sha256(sysUser.getUserPassword()));
        return sysUser;
    }


}
