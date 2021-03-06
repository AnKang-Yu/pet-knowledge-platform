package xyz.dg.dgpethome;

import cn.hutool.core.util.DesensitizedUtil;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.service.BArticleService;
import xyz.dg.dgpethome.service.SysDictService;
import xyz.dg.dgpethome.service.SysUserService;
import xyz.dg.dgpethome.utils.FilesUtils;



import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;

@SpringBootTest
@Slf4j
class DgpethomeApplicationTests {

    @Resource
    private SysDictService sysDictServiceImpl;

    @Resource
    private SysUserService sysUserServiceImpl;

    @Resource
    private BArticleService bArticleServiceImpl;

    @Test
    public void testAllDict() throws FileNotFoundException {
        List<CascaderSysDictVo> test =  sysDictServiceImpl.findAllDictByParentId(0);
        System.out.println(test.toString());
        //String str = IOUtils.toString(new FileInputStream("/temp/a.txt"), StandardCharsets.UTF_8);
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
    public static String[] chars = new String[]
            {
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z"
            };
    @Test
    public void testUUID(){
        StringBuffer stringBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++)
        {
            String str 		= uuid.substring(i * 4, i * 4 + 4);
            int strInteger 	= Integer.parseInt(str, 16);
            stringBuffer.append(chars[strInteger % 0x3E]);
        }

        System.out.println( stringBuffer.toString());
    }


    @Test
    public void testSalt(){

        System.out.println("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92".length());
    }
    @Test
    public void test_Category(){
//        List<Map<String , Object>> list = bArticleServiceImpl.findAllArticleCategoryList();
//        list.forEach(System.out::println);
        System.out.println(new Date());
    }

    @Test
    public  void tttt() throws IOException {
        String path = "D:/tmp/images/article_1/articleThumbnail.jpg";
        byte[] aa = new FilesUtils().getFile(path);

        log.info("" + aa.length);
    }
    @Test
    public void a(){
        String secret ="";
        String guess = "";
        int index = 1;

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

    @Test
    public void testUtil(){
        String name  = DesensitizedUtil.chineseName("嬴政");
        System.out.println(name);

    }
}
