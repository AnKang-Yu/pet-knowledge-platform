package xyz.dg.dgpethome.config;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Dugong
 * @date 2021-11-05 18:48
 * @description
 **/
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("articleCreated",new Date(),metaObject);
        this.setFieldValByName("articleModified",new Date(),metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // log.info("当前时间:------------"+new Date());
        this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("articleModified",new Date(),metaObject);
    }
}

