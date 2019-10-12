package media;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LambdaTest
 * @Description LambdaTest
 * @Author pc
 * @Date 2019/10/7 13:13
 * @Version 1.0
 **/
public class LambdaTest {

    @Test
    public void handlerList(){
        //创建一个10万数据的集合
        List<User> userList = new ArrayList<>() ;
        for (int i=0;i < 1000000;i++){
            User user = new User() ;
            user.setId(i);
            user.setUserName("kweitan"+i);
            user.setSex(i);
            userList.add(user) ;
        }
        //开始计时
        long startTime = System.currentTimeMillis() ;
        //使用for来遍历
        for (int i=0;i < userList.size();i ++){
            System.out.println(userList.get(i).toString());
        }
        //消耗时间
        System.out.println("for遍历消耗时间："+
                (System.currentTimeMillis() - startTime)+" 毫秒！");
    }
    @Test
    public void handlerListLambda(){
        //创建一个10万数据的集合
        List<User> userList = new ArrayList<>() ;
        for (int i=0;i < 1000000;i++){
            User user = new User() ;
            user.setId(i);
            user.setUserName("kweitan"+i);
            user.setSex(i);
            userList.add(user) ;
        }
        //开始计时
        long startTime = System.currentTimeMillis() ;
        //使用lambda来遍历
        userList.stream().forEach(user -> {System.out.println(user.toString());});
        //消耗时间
        System.out.println("lambda遍历消耗时间："
                +(System.currentTimeMillis() - startTime)+" 毫秒！");
    }

    @Test
    public void lambda(){
        //创建一个500万数据的集合
        List<User> userList = new ArrayList<>() ;
        for (int i=0;i < 5000000;i++){
            int sex = i % 2;
            userList.add(new User(i,"kweitan"+i,sex)) ;
        }
        //开始计时
        long startTime = System.currentTimeMillis() ;
        //********************************************************************************
        //使用for来遍历
        for (int i=0;i < userList.size();i ++){
            System.out.println(userList.get(i).toString());
        }
        //消耗时间
        long forTime = System.currentTimeMillis() - startTime ;

        //********************************************************************************
        startTime = System.currentTimeMillis() ;
        //使用lambda来遍历
        userList.stream().forEach(user -> {System.out.println(user.toString());});
        //消耗时间
        long lambdaStreamTime = System.currentTimeMillis() - startTime ;

        //********************************************************************************
        startTime = System.currentTimeMillis() ;
        //使用lambda并行流来遍历
        userList.parallelStream().forEach(user -> {System.out.println(user.toString());});
        //消耗时间
        long lambdaParallelStreamTime = System.currentTimeMillis() - startTime ;

        System.out.println("for遍历消耗时间："
                +(forTime)+" 毫秒！");
        System.out.println("lambda遍历消耗时间："
                +(lambdaStreamTime)+" 毫秒！");
        System.out.println("lambda并行流遍历消耗时间："
                +(lambdaParallelStreamTime)+" 毫秒！");
    }
}
