package xyz.dg.dgpethome.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.AuthenticationException;


import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.mapper.SysUserMapper;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.myexceptions.MyAuthenticationException;
import xyz.dg.dgpethome.service.SysDictService;
import xyz.dg.dgpethome.service.SysUserService;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;
import xyz.dg.dgpethome.utils.MailUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author  Dugong
 * @date  2021-10-03 1:20
 * @description
 **/
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper,SysUser> implements SysUserService {

//    @Autowired
//    BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;
    @Resource
    private  SysUserMapper sysUserMapper;
    @Resource
    private SysDictService sysDictServiceImpl;


    //用户状态   字典里id是31
    private static final Integer USER_STATE = 31;

    /**
     * redis缓存
     */
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 通过账号查询用户
     * @param userName
     * @return
     */
    @Override
    public SysUser getUserByUserUsername(String userName) {
        SysUser user = null;
        Object o = redisTemplate.opsForValue().get("userName_"+userName);
        if(o!=null){
            //缓存中有数据
            log.info("读取到redis缓存userName_"+userName);
            user=(SysUser)o;
        }else{
            // 缓存中没有，就进入数据库查询
            LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //查询条件：全匹配用户名，和状态为1的账号
            lambdaQueryWrapper
                    .eq(SysUser::getUserName,userName);
            //.eq(SysUser::getUserStatus,USER_STATE);
            //用getOne查询一个对象出来
            user  = this.baseMapper.selectOne(lambdaQueryWrapper);
            if(user!=null){
                log.info("redis缓存了userName_"+userName);
                redisTemplate.opsForValue().set("userName_"+userName ,user);
            }
        }
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
    /**
     * 用户查询分页 并进行一下封装
     * @param sysUserPageParam
     * @return
     */
    @Override
    public IPage<SysUserVo> findUserList(SysUserPageParam sysUserPageParam) {

        Long currentPage = sysUserPageParam.getCurrentPage();
        Long pageSize = sysUserPageParam.getPageSize();
        //初始化
        IPage<SysUserVo> userVoIPage = sysUserMapper.findUserList(new Page<SysUserVo>(currentPage,pageSize),sysUserPageParam);
        //IPage<SysUser> temp = new Page<SysUser>(currentPage,pageSize);
        //构造用户表查询条件
//        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(SysUser::getUserRoleId,sysUserPageParam.getUserRoleId());
        //用户名不为空就查用户名
//        String userName  = sysUserPageParam.getUserName();
//        if(StringUtils.isNotEmpty(userName)){
//            queryWrapper.lambda().like(SysUser::getUserName,userName);
//        }
//        temp = sysUserMapper.selectPage(temp,queryWrapper);
//        List<SysUserVo> limitList =  temp.getRecords().stream().map(item->BeanUtil.copyProperties(item,SysUserVo.class)).collect(Collectors.toList());
//        userVoIPage.setRecords(limitList);
//        userVoIPage.setTotal(temp.getTotal());
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

    /**
     * 数据脱敏
     * @param sysUser
     * @return
     */
    @Override
    public Map<String, Object> dataMaskUserInfo(SysUser sysUser) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId",sysUser.getUserId());
        // 用户账户
        data.put("userAccount", sysUser.getUserAccount());
        data.put("userMaskAccount", DesensitizedUtil.chineseName(sysUser.getUserAccount()));
        // 手机号
        data.put("userPhone",sysUser.getUserPhone());
        data.put("userMaskPhone",DesensitizedUtil.mobilePhone(sysUser.getUserPhone()));
        // 邮箱
        data.put("userEmail",sysUser.getUserEmail());
        data.put("userMaskEmail",DesensitizedUtil.email(sysUser.getUserEmail()));
        data.put("userSex",sysUser.getUserSex());
        data.put("userRoleName",sysDictServiceImpl.getById(sysUser.getUserRoleId()).getDictValue());
        data.put("userStatusName",sysDictServiceImpl.getById(sysUser.getUserStatus()).getDictValue());

        return data;
    }

    /**
     *  注册用户
     * @param sysUser
     * @param code
     * @return
     */
    @Override
    public JsonResult registerUser(SysUser sysUser, String code) {
        if("1234".equals(code)){
            // 验证码通过
            //判断用户是否存在，如果存在且密码正确，则保存该用户对象
            SysUser existUser = this.getUserByUserUsername(sysUser.getUserName());
            if(existUser != null){
                return JsonResultUtils.error("用户名已存在");
            }
            // 设置和用户名同名的账户名
            sysUser.setUserAccount(sysUser.getUserName());
            // 23普通用户
            sysUser.setUserRoleId(23);
            // 31启用状态
            sysUser.setUserStatus(31);
            Integer rows = this.baseMapper.insert(sysUser);
            if(rows > 0){
                //200
                return JsonResultUtils.success("注册成功");
            }
            //500
            return JsonResultUtils.error("注册失败");
        }
        //500
        return JsonResultUtils.error("验证码错误");
    }

    @Autowired
    private MailUtils mailUtils;
    /**
     * 邮箱发送验证码
     * @return
     */
    @Override
    public JsonResult getRegisterCode(String userEmail) {
        if(userEmail == null){
            return JsonResultUtils.error("验证码发送失败，邮箱为空");
        }
        //  邮件发送
        String title = " 用户注册验证码";
        String code = RandomUtil.randomString(4);
        String content = "<html><body>用户注册验证码为: <b>"+ code+ "</b></body></html>";
        mailUtils.sendSimpleEmail(userEmail,title,content);
        return JsonResultUtils.success("验证发已发送");
    }


}
