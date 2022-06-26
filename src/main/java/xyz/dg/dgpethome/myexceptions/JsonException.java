package xyz.dg.dgpethome.myexceptions;

/**
 * @program: dgpethome
 * @description:
 * @author: ruihao_ji
 * @create: 2022-06-13 10:49
 **/
public class JsonException extends RuntimeException {
    public JsonException(String msg) {
        super(msg);
    }
}
