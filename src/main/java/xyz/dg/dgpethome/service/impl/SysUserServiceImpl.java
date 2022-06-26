package xyz.dg.dgpethome.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.mapper.SysUserMapper;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.service.SysDictService;
import xyz.dg.dgpethome.service.SysUserService;
import xyz.dg.dgpethome.utils.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @author  Dugong
 * @date  2021-10-03 1:20
 * @description
 **/
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper,SysUser> implements SysUserService {

    @Resource
    BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;
    @Resource
    private  SysUserMapper sysUserMapper;
    @Resource
    private SysDictService sysDictServiceImpl;
    @Resource
    private RedisDao redisDao;
    @Resource
    private TokenDao tokenDao;

    //用户状态   字典里id是31
    private static final Integer USER_STATE = 31;

    @Override
    public SysUser loadUserByUsername(String userName)  {
        return sysUserMapper.loadUserByUsername(userName);
    }

    @Override
    public SysUser getUserById(Integer userId) {
        return sysUserMapper.getUserById(userId);
    }


    /**
     * 用户查询分页 并进行一下封装
     * @param sysUserPageParam
     * @return
     */
    @Override
    public IPage<SysUserVo> findUserList(SysUserPageParam sysUserPageParam,Integer userId) {

        Long currentPage = sysUserPageParam.getCurrentPage();
        Long pageSize = sysUserPageParam.getPageSize();
        //初始化
        IPage<SysUserVo> userVoIPage = sysUserMapper.findUserList(new Page<SysUserVo>(currentPage,pageSize),sysUserPageParam,userId);
        return   userVoIPage;
    }

    /**
     * 密码加密
     * @param sysUser
     * @return
     */
    @Override
    public SysUser passwordToEncode(SysUser sysUser) {
        sysUser.setUserPassword(bCryptPasswordEncoderUtil.encode(sysUser.getUserPassword()));
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
        data.put("userName",sysUser.getUsername());
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
        String key = "registerCode_"+sysUser.getUserEmail();
        boolean hasKey = redisDao.hasRedisKey(key);
        if(!hasKey){
            // 缓存中没有
            return JsonResultUtils.error("验证码无效或已过期");
        }
        String redisCode = redisDao.getRedisCacheCode(key);
        if(redisCode.equals(code)){
            // 验证码通过
            // 判断用户名是否存在，如果存在则不予注册
            SysUser existUser = this.loadUserByUsername(sysUser.getUsername());
            if(existUser != null){
                return JsonResultUtils.error("用户名已存在");
            }
            // 设置和用户名同名的账户名
            sysUser.setUserAccount(sysUser.getUsername());
            // 23普通用户
            sysUser.setUserRoleId(23);
            // 31启用状态
            sysUser.setUserStatus(31);
            // 加密
            SysUser encodeUser = this.passwordToEncode(sysUser);
            Integer rows = this.baseMapper.insert(encodeUser);

            if(rows > 0){
                //200
                // 删除缓存
                redisDao.deleteRedisKey(key);
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
     * 邮箱发送注册验证码
     * @return
     */
    @Override
    public JsonResult getRegisterCode(String userEmail) throws MessagingException {
        if(userEmail == null){
            return JsonResultUtils.error("验证码发送失败，邮箱为空");
        }
        //  邮件发送
        String title = " 用户注册验证码";
        String key = "registerCode_"+userEmail;
        String code = RandomUtil.randomString(4);
        // 5分钟后过期
        redisDao.setRedisCacheCode(key,code);
        String content = "<body>用户注册验证码为: <b>"+ code+ "</b>，五分钟后过期</body>";
        mailUtils.sendSimpleEmail(userEmail,title,content);
        return JsonResultUtils.success("验证发已发送");
    }

    /**
     * 更改当前用户信息
     * @param sysUser
     * @return
     */
    @Override
    public JsonResult editCurrentUserInfo(SysUser sysUser) {
        Boolean rows = this.updateById(sysUser);
        // 重新设置上下文
        SecureUtils.setSpringSecurityUser(sysUser);
        if(rows){
            //200
            return JsonResultUtils.success("编辑成功");
        }
        return JsonResultUtils.success("编辑失败");
    }

    /**
     * 忘记密码的发送验证码
     * @param sysUser
     * @return
     * @throws MessagingException
     */
    @Override
    public JsonResult getRetrieveCode(SysUser sysUser) throws MessagingException {
        if(sysUser.getUserEmail() == null){
            return JsonResultUtils.error("验证码发送失败，邮箱为空");
        }
        //  邮件发送
        String title = " 用户验证码";
        String key = "retrieveCode_"+sysUser.getUsername();
        String code = RandomUtil.randomString(4);
        // 5分钟后过期
        redisDao.setRedisCacheCode(key,code);
        String content = "<body>用户验证码为: <b>"+ code+ "</b>，五分钟后过期</body>";
        mailUtils.sendSimpleEmail(sysUser.getUserEmail(),title,content);
        return JsonResultUtils.success("验证发已发送");
    }

    @Override
    public JsonResult resetUserPwd(String userName,String newPwd, String code) {
        if(userName == null || newPwd == null || code == null){
            return JsonResultUtils.error("请求参数错误");
        }
        String key = "retrieveCode_"+userName;
        boolean hasKey = redisDao.hasRedisKey(key);
        if(!hasKey){
            return JsonResultUtils.error("验证码无效或已过期");
        }
        String redisCode = redisDao.getRedisCacheCode(key);
        if(redisCode.equals(code)){
            // 验证码通过
            // 取用户
            SysUser existUser = this.loadUserByUsername(userName);
            existUser.setUserPassword(newPwd);
            // 加密
            SysUser encodeUser = this.passwordToEncode(existUser);
            Boolean rows = this.updateById(encodeUser);

            if(rows ){
                // 删除缓存
                //200
                tokenDao.cleanUserToken(existUser.getUsername());
                redisDao.deleteRedisKey(key);
                return JsonResultUtils.success("更新密码成功");
            }
            //500
            return JsonResultUtils.error("更改密码失败");
        }
        //500
        return JsonResultUtils.error("验证码错误");
    }
}
