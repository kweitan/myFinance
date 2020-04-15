package com.sinjee.test;

import com.sinjee.tools.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author 小小极客
 * 时间 2020/3/5 0:12
 * @ClassName Crawler
 * 描述 Crawler
 **/
@Slf4j
public class Crawler {

    public static void  main(String[] args){
        crawler() ;
    }

    //爬取数据
    public static void crawler(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60*1000);
        requestFactory.setReadTimeout(60*1000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        restTemplate.getRequestFactory().

        Map<String,String> map = new HashMap<>() ;

        map.put("Korea","韩国") ;
        map.put("Iran","伊朗") ;
        map.put("Italy","意大利") ;
        map.put("Japan","日本本土") ;
        map.put("France","法国") ;
        map.put("Germany","德国") ;
        map.put("Spain","西班牙") ;
        map.put("United States","美国") ;
        map.put("Singapore","新加坡") ;
        map.put("United Kingdom","英国") ;
        map.put("Switzerland","瑞士") ;
        map.put("Netherlands","荷兰") ;
        map.put("Austria","奥地利") ;


        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        StringBuffer sb = new StringBuffer() ;
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            String url = "https://api.inews.qq.com/newsqa/v1/query/pubished/daily/list?country=" + entry.getValue();
            Datas datas = restTemplate.getForObject(url,Datas.class) ;
            log.info("datas={}", GsonUtil.getInstance().toStr(datas));
            for (int i=0;i<datas.getData().size();i++){
                CountryVO vo = datas.getData().get(i) ;
                String date = vo.getDate() ;
                String[] arrs = date.split("\\.") ;
                sb.append(entry.getValue()+","+entry.getKey()+","+vo.getConfirm()+",2020-"+arrs[0]+"-"+arrs[1]).append("\n") ;
            }

        }

        System.out.println(sb.toString());
    }
}
