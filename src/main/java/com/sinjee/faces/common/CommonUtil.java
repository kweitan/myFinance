package com.sinjee.faces.common;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 创建时间 2019 - 11 -11
 * 通用工具类
 * @author kweitan
 */
public class CommonUtil {

    public static <T> List<T> toList(Optional<T> option) {
        return option.
                map(Collections::singletonList).
                orElse(Collections.emptyList());
    }


}
