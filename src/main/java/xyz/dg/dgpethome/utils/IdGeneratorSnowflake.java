package xyz.dg.dgpethome.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Dugong
 * @date 2021-09-29 19:48
 * @description
 **/
@Slf4j
@Component
public class IdGeneratorSnowflake {
    /**
     * 工作机器id
     */
    private long workerId = 0;
    /**
     * 数据中心id
     */
    private long datacenterId = 1;

    private Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);

    @PostConstruct
    public void init(){
        try{
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workId:{}",workerId);
        }catch (Exception e){
            e.printStackTrace();
            log.warn("当前机器的workId获取不成功",e);
            workerId = NetUtil.getLocalhostStr().hashCode();
        }finally {

        }

    }

    /**
     * 利用糊涂工具包的雪花算法获取id
     * @return
     */
    public synchronized long snowflakeId(){
        return snowflake.nextId();
    }

    /**
     * 自定义传入机器id和数据中心id生成id
     * @param workerId
     * @param datacenterId
     * @return
     */
    public synchronized long snowflakeId(long workerId,long datacenterId){
        Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

}
