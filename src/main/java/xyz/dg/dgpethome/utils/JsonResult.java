package xyz.dg.dgpethome.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @author Dugong
 * @date 2021-09-30 16:18
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult<T> {
    private String msg;
    private Integer code;
    private T data;
}