package xyz.dg.dgpethome;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.service.SysDictService;
import xyz.dg.dgpethome.service.SysUserService;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import xyz.dg.dgpethome.model.po.AuthUser;
//import xyz.dg.dgpethome.utils.JwtTokenUtil;


import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
class DgpethomeApplicationTests {

    @Resource
    private SysDictService sysDictServiceImpl;

    @Resource
    private SysUserService sysUserServiceImpl;


    @Test
    public void testAllDict(){
        List<CascaderSysDictVo> test =  sysDictServiceImpl.findAllDictByParentId();
        System.out.println(test.toString());
    }


    /**
     * 测试分页
     */
    @Test
    public void testPageUser(){
        IPage<SysUser> userPage = new Page<>(2, 10);//参数一是当前页，参数二是每页个数
        userPage = sysUserServiceImpl.getBaseMapper().selectPage(userPage, null);
        List<SysUser> list = userPage.getRecords();
         for(SysUser user : list){
                 System.out.println(user);
             }
    }

    /**
     * 测试分页2
     */
    @Test
    public void testPageUser2(){
        SysUserPageParam sysUserPageParam = new SysUserPageParam();
        sysUserPageParam.setPageSize(10L);
        sysUserPageParam.setCurrentPage(2L);

        IPage<SysUserVo> iPage = sysUserServiceImpl.findUserList(sysUserPageParam);
        for(SysUserVo i : iPage.getRecords()){
            System.out.println(i.toString());
        }
    }

    @Test
    public void testSalt(){
        System.out.println(SaSecureUtil.md5BySalt("123456", "kksk"));
        System.out.println(SaSecureUtil.md5BySalt("123456", "kksk"));
        System.out.println(SaSecureUtil.md5BySalt("123451231246", "Dugong"));
        System.out.println(SaSecureUtil.md5BySalt("123456", "Dugong"));
        System.out.println(SaSecureUtil.md5BySalt("123456", "114514"));
        System.out.println(SaSecureUtil.md5BySalt("123456", "wcnm"));
        System.out.println("19db17a0eac0ada71785ebeb4483eccf".length());
        System.out.println("---------------------");
        System.out.println(SaSecureUtil.sha256("123456"));
        System.out.println(SaSecureUtil.sha256("123456"));
        System.out.println("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92".length());
    }

//    @Autowired
//    JwtTokenUtil jwtTokenUtil;
//
//    @Test
//    void contextLoads() {
//    }
//    @Test
//    public void tokenCreate(){//创建一个权限的集合
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        //添加获取权限
//        authorities.add(new SimpleGrantedAuthority(""+20));
//        UserDetails authUser = new AuthUser("admin","123456",1,authorities);
//        String token = jwtTokenUtil.generateToken(authUser);
//        System.out.println(token.length());
//        System.out.println(token);
//    }
//
//    @Test
//    public void testMethod() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
////        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
////        System.out.println(encoder.encode("123456"));
////        ac4E5hzsyVWiId/dDw4U9Q==
////        +mdCmQ3SbQFJE3DG6hHQ==
//        //$2a$10$6KBgsGfijoGOoo5jwInNF.wArSmlHWPi/yRGCZZbmLzx6ABrd5RPm
//        //$10$5ooQI8dir8jv0/gCa1Six.GpzAdIPf6pMqdminZ/3ijYzivCyPlfK
//        String pass = "123456";
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        final String passHash = encoder.encode(pass);
//        System.out.println(passHash);
//
//        final boolean matches = encoder.matches(pass, passHash);
//        System.out.println(matches);
//    }
}
