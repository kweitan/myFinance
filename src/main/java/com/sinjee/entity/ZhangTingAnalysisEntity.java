package com.sinjee.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2019/10/13 17:50
 * @ClassName ZhangTingAnalysisEntity
 * 描述 涨停分析数据库实体类
 **/
@Data
public class ZhangTingAnalysisEntity extends BaseEntity implements Serializable {

    //股票代码
    private String ts_code ;

    //交易日期
    private int trade_date ;

    //开盘价
    private double open ;

    //最高价
    private double high ;

    //最低价
    private double low ;

    //收盘价
    private double close ;

    //上一个交易日收盘价
    private double pre_close ;

    //涨跌额
    private double change ;

    //涨跌幅
    private double pct_chg ;

    //成交量 （手）
    private double vol ;

    //成交额 （千元）
    private double amount ;

    //股票名称
    private String stockName ;

    //股票区域
    private String area ;

    //股票行业
    private String industry ;

    //上市日期
    private int list_date ;

    //是否涨停
    private int isZhangting ;

    //是否跌停
    private int isDieting ;

    //是否吃面
    private int isEatBread ;

    //是否一字涨停
    private int isZhangtingOne ;

    //是否一字跌停
    private int isDietingOne ;

    //是否炸板
    private int isZhaban ;

    //吃面数量
    private double eatBreadNum ;

    //是否实体涨停
    private int isZTShiti ;

    //是否T字涨停
    private int isZTT ;

    //是否是倒T字跌停
    private int isDTT ;

    //涨停大长腿
    private double ZTBigLeg ;

    //是否大长腿
    private int isBigLeg ;

    //是摸到到涨停后回落吃大面的状态
    private int isEatZTBread ;

    //是摸到到涨停后回落吃大面的状态幅度
    private double isEatZTBreadLen ;

    //是否是涨停开盘炸板
    private int isFitBaopo ;

    //是否地天板
    private int isEatDTBan ;

    //是否天地板
    private int isEatTDBan ;

    //计算K线长度
    private int ZTKsize ;

    //计算开盘涨幅
    private double OpenLen ;

    //连扳数量包括缩量 一字板
    private int isLianban ;

    //连扳数量实体
    private int isLianbanST ;

    //表示昨日涨停今日跌停
    private int preZTcurDT ;

    //连扳被砸
    private int isLianbanBZ ;

    //统计涨停数量
    private int zhangtingNum ;

}
