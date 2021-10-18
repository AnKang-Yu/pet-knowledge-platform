package xyz.dg.dgpethome.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dugong
 * @date 2021-10-15 15:27
 * @description
 **/
@Configuration
@ConditionalOnClass(value = {PaginationInterceptor.class})
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor mybatisPlusInterceptor() {
        PaginationInterceptor interceptor = new PaginationInterceptor();
        //interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }
}
