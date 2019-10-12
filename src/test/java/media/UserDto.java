package media;

import lombok.Data;

@Data
public class UserDto {

    private String userName ;

    private int sex ;

    public String toString(){
        return "userName:"+userName+";sex:"+sex ;
    }

    public UserDto(String userName,int sex){
        this.userName = userName ;
        this.sex = sex ;
    }

    public UserDto(){}
}
