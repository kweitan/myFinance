package com.sinjee.tools;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @ClassName PageModel
 * @Description 分页实体
 * @Author pc
 * @Date 2019/10/6 11:47
 * @Version 1.0
 **/
@Data
public class PageModel<T> {
    //结果集
    private List<T> datas;
    //查询总计记录数
    private Long rowCount;

    //包含页码 , 每页多少条数
    private Pageable pageable;

    public PageModel(List<T> datas, Long rowCount, Pageable pageable) {
        this.datas = datas;
        this.rowCount = rowCount;
        this.pageable = pageable;
    }
}
