package media;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;

public class BeanCopyTest {

    @Test
    public void testCopy(){
        //创建一个1000万对象
        List<User> userList = new ArrayList<>() ;
        for (int i=0;i < 10000000;i++){
            int sex = i % 2;
            userList.add(new User(i,"kweitan"+i,sex)) ;
        }

        //开始计时
        long startTime = System.currentTimeMillis() ;
        userList.stream().forEach(user -> {
            BeanUtils.copyProperties(user,new UserDto());
        });
        //消耗时间
        long beanUtilsTime = System.currentTimeMillis() - startTime ;

        startTime = System.currentTimeMillis() ;
        BeanCopier beanCopier = BeanCopier.create(User.class,UserDto.class,false);
        userList.stream().forEach(user -> {
            beanCopier.copy(user,new UserDto(),null);
        });
        //消耗时间
        long beanCopierTime = System.currentTimeMillis() - startTime ;

        System.out.println("BeanUtils属性拷贝消耗时间："
                +(beanUtilsTime)+" 毫秒！");
        System.out.println("BeanCopier属性拷贝消耗时间："
                +(beanCopierTime)+" 毫秒！");
    }

}
