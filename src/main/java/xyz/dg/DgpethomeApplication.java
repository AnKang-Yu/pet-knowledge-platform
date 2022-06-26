package xyz.dg;


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
    }

}
