//package xyz.dg.dgpethome.utils;
//
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//
///**
// * @author Dugong
// * @date 2021-09-30 23:03
// * @description
// *  缓存管理token
// */
//public class TokenCache {
//
//    private static final String TOKEN_KEY = "token_";
//    private static Cache<String,String> cache = CacheBuilder.newBuilder().build();
//
//    /**
//     * 保存
//     * @param token
//     */
//    public static void setToken(String userAccount,String token) {
//
//        cache.put(TOKEN_KEY+userAccount,token);
//    }
//
//    /**
//     * 取
//     * @return
//     */
//    public static String getTokenFromCache(String userAccount){
//        return cache.getIfPresent(TOKEN_KEY+userAccount);
//    }
//
//}
