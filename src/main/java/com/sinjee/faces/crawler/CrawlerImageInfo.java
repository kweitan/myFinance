package com.sinjee.faces.crawler;

import com.baidu.aip.face.AipFace;
import com.sinjee.faces.AipFaceDTO;
import com.sinjee.faces.AipFaceUtil;
import com.sinjee.faces.FaceListDTO;
import com.sinjee.faces.common.DownloadPicFromURL;
import com.sinjee.faces.common.GsonUtil;
import com.sinjee.tools.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

/**
 * @author 小小极客
 * 时间 2019/11/8 21:38
 * @ClassName CrawlerImageInfo
 * 描述 启动爬虫
 **/
@Slf4j
public class CrawlerImageInfo implements Runnable {

    public static void main(String[] args){
        new Thread(new CrawlerImageInfo()).start();
    }

    public void handlerInfo(String mainUrl){

        String path="G:/data/beautify/";

        //http://www.umei.cc/p/gaoqing/cn/
        Document doc = null ;
        int i = 1 ;
        AipFace client = AipFaceUtil.getInstance().getAipFace() ;

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age,gender,beauty,qualities");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "LOW");
        while(true){
            if (i == 1){
                doc = getJsoupPage(mainUrl) ;
            }else {
                doc = getJsoupPage(mainUrl+i+".htm") ;
            }
            if(null != doc){
                Elements liEles = doc.select("div[class=wrap] div[class=TypeList] ul li") ;
                if(liEles.size()>0){
                    for (Element liEle:liEles) {
                        Element imgEle = liEle.select("a[class=TypeBigPics] img").first() ;
                        Element titleEle = liEle.select("a[class=TypeBigPics] div[class=ListTit]").first() ;
                        if(null != imgEle && null != titleEle){
                            String imgUrl = imgEle.attr("src") ;
                            String title = titleEle.text();
                            log.info("imgUrl={}",imgUrl);

                            //人脸识别 开始
                            String imageType = "URL";

                            // 人脸检测
                            JSONObject res = client.detect(imgUrl, imageType, options);

                            //先判断是否成功
                            if (null != res && res.getString("error_msg").equals("SUCCESS")){
                                String result = res.getJSONObject("result").toString();
                                AipFaceDTO aipFaceDTO = GsonUtil.json2Bean(result,AipFaceDTO.class);

                                //没有人脸
                                if (aipFaceDTO.getFace_num() == 0){
                                    continue ;
                                }

                                for (FaceListDTO faceListDTO:aipFaceDTO.getFace_list()){
                                    //人脸置信度太低
                                    if (faceListDTO.getFace_probability() < 0.6){
                                        continue;
                                    }

                                    //颜值低于50
                                    if (faceListDTO.getBeauty() < 50){
                                        continue;
                                    }

                                    //性别非女性
                                    if (!faceListDTO.getGender().getType().equals("female")){
                                        continue;
                                    }

                                    //成功下载图片 title imgUrl title_faceListDTO.getBeauty()_.jpg
                                    DownloadPicFromURL.downloadPicture(imgUrl,path+ DateUtils.getCurrentDate()+"_"+faceListDTO.getBeauty()+".jpg");
                                    break ;
                                }

                                log.info(aipFaceDTO.toString());
                            }else{
                                log.error("识别失败");
                            }
                            //人脸识别 结束

                            sleep(10000);
                        }
                    }
                }
                i++ ;
            }else {
                log.info("线程执行结束！！！");
                break;
            }

        }
    }

    private Document getJsoupPage(String url) {
        int count = 0;
        Document doc = null;
        while (count < 5) {
            count++;
            try {
                String referrer = "http://www.umei.cc/";
                doc = Jsoup
                        .connect(url)
                        .userAgent(
                                "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)")
                        .referrer(referrer).timeout(1000 * 60 * 5).get();

                sleep(1000);
                return doc;
            } catch (Exception e) {
                String message = e.getMessage().trim();
                log.error("========================: 第 " + count
                        + " 次，webClient访问网络连接异常,访问URL:" + url + "，原因："
                        + message);

                if (message.startsWith("403")) {// IP被禁止访问
                    log.info("本机IP被禁止访问，休眠30分钟...");
                    sleep(60 * 3000);
                }

                sleep(20000);
            }
        }
        return Jsoup.parse("<div></div>");
    }


    private void sleep(int time){
        try {
            Thread.sleep(time);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
    }

    @Override
    public void run() {
        handlerInfo("http://www.umei.cc/p/gaoqing/cn/") ;
    }
}
