package media;

import lombok.Data;

import java.util.Optional;

/**
 * 创建时间 2019 - 11 -11
 * Option NULL 空指针类
 * @author kweitan
 */
@Data
public class Person {

    private final Optional<Address> address ;

}
