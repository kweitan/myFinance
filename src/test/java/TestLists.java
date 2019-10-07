import com.sinjee.dto.MongoTestDto;
import com.sinjee.entity.MongoTestEntity;
import com.sinjee.tools.BeanConversionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TestLists
 * @Description TODO
 * @Author pc
 * @Date 2019/10/6 9:43
 * @Version 1.0
 **/
public class TestLists {

    @Test
    public void copyLists(){
        System.out.println("test");
        MongoTestEntity source = new MongoTestEntity() ;
        source.setId(200);
        source.setUserName("kweitan");
        source.setAge("20");

        MongoTestEntity source1 = new MongoTestEntity() ;
        source1.setId(201);
        source1.setUserName("guixin");
        source1.setAge("28");

        List<MongoTestEntity> sourceList = new ArrayList<>() ;
        sourceList.add(source);
        sourceList.add(source1);

        List<MongoTestDto> destList = new ArrayList<>() ;
//        BeanConversionUtils.listCopyToAnotherList(sourceList,destList);
    }
}
