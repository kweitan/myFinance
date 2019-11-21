package media;

import lombok.Data;

import java.util.Optional;

/**
 * 创建时间 2019 - 11 -11
 * Option NULL 空指针类设置
 * @author kweitan
 */
@Data
public class Address {
    private final Optional<Instant> validFrom;
}
