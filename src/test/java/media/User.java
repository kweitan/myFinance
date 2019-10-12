package media;

import lombok.Data;

/**
 * @ClassName User
 * @Description User
 * @Author pc
 * @Date 2019/10/7 13:15
 * @Version 1.0
 **/
@Data
public class User {

    private int id ;

    private String userName ;

    private int sex ;

    public String toString(){
        return "id:"+id+";userName:"+userName+";sex:"+sex ;
    }

    public User(int id,String userName,int sex){
        this.id = id ;
        this.userName = userName ;
        this.sex = sex ;
    }

    public User(){}

}
