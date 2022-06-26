//package xyz.dg.dgpethome.config;
//
//import cn.dev33.satoken.stp.SaTokenInfo;
//import cn.dev33.satoken.stp.StpInterface;
//import cn.dev33.satoken.stp.StpUtil;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Dugong
// * @date 2021-10-03 21:51
// * @description
// * 自定义权限验证接口扩展
// */
//@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
//public class StpInterfaceImpl implements StpInterface {
//
//    //20 - 超级管理员
//    private static final Integer superAdmin = 20;
//    //21 - 管理员
//    private static final Integer admin = 21;
//    /**
//     * 返回一个账号所拥有的权限码集合
//     */
//    @Override
//    public List<String> getPermissionList(Object loginId, String loginType) {
//        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
//        List<String> list = new ArrayList<>();
//        if(String.valueOf(superAdmin).equals(loginType)){
//            list.add("*");
//        }
//        if(String.valueOf(admin).equals(loginType)){
//            list.add("admin-get");
//            list.add("dict-get");
//            list.add("user*");
//        }
//
//        return list;
//    }
//
//    /**
//     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
//     */
//    @Override
//    public List<String> getRoleList(Object loginId, String loginType) {
//        // 获取当前会话的token信息参数
////        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
////        tokenInfo.getLoginType()
//
//        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
//        List<String> list = new ArrayList<String>();
//        if(String.valueOf(superAdmin).equals(loginType)){
//            list.add("20");
//        }
//        if(String.valueOf(admin).equals(loginType)){
//            list.add("21");
//        }
//        return list;
//    }
//
//}