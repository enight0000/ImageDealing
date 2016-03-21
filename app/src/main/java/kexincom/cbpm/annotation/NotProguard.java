package kexincom.cbpm.annotation; /**
 * 作者：Leon Xie
 * 时间： 2015/11/6 0006
 * 邮箱：xiezhixuan@cbpm-kexin.com
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ccad.com.camera.annotation.NotProguard, Means not proguard something, like class, method, field<br/>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2015-08-07
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface NotProguard {

}
