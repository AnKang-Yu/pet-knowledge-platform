package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

import xyz.dg.dgpethome.mapper.SysUserMapper;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.mapper.SysDictMapper;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.service.SysDictService;
/**
 * @author  Dugong
 * @date  2021-10-03 1:21
 * @description
 **/
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService{

}
