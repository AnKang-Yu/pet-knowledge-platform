package xyz.dg;

import cn.dev33.satoken.SaManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("xyz.dg.dgpethome.mapper")
@EnableTransactionManagement
public class DgpethomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DgpethomeApplication.class, args);
        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }

}
