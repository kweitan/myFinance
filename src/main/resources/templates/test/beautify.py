# coding: utf-8

import time
import os
import re

import requests
from lxml import etree

from aip import AipFace

# 百度云 人脸检测 申请信息
# 唯一必须填的信息就这三行
APP_ID = "17720950"
API_KEY = "25tEq8YOadkZrVYKri3XkWHw"
SECRET_KEY = "ZmsktVfDwukYw9MKGSVqZPs39G1Yrm6l"

# 文件存放目录名，相对于当前目录
DIR = "image"
# 过滤颜值阈值，存储空间大的请随意
BEAUTY_THRESHOLD = 45

# 浏览器中打开知乎，在开发者工具复制一个，无需登录
# 如何替换该值下文有讲述
AUTHORIZATION = "oauth c3cef7c66a1843f8b3a9e6a1e3160e20"

# 以下皆无需改动

# 每次请求知乎的讨论列表长度，不建议设定太长，注意节操
LIMIT = 5

# 这是话题『美女』的 ID，其是『颜值』（20013528）的父话题
SOURCE = "19552207"

# 爬虫假装下正常浏览器请求
USER_AGENT = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.55.3 (KHTML, like Gecko) Version/5.1.5 Safari/534.55.3"
# 爬虫假装下正常浏览器请求
REFERER = "https://www.zhihu.com/topic/%s/newest" % SOURCE
# 某话题下讨论列表请求 url
BASE_URL = "https://www.zhihu.com/api/v4/topics/%s/feeds/timeline_activity"
# 初始请求 url 附带的请求参数
URL_QUERY = "?include=data%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.content%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.is_normal%2Ccomment_count%2Cvoteup_count%2Ccontent%2Crelevant_info%2Cexcerpt.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Darticle%29%5D.target.content%2Cvoteup_count%2Ccomment_count%2Cvoting%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Dpeople%29%5D.target.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.content%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Darticle%29%5D.target.content%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dquestion%29%5D.target.comment_count&limit=" + str(
    LIMIT)


# 指定 url，获取对应原始内容 / 图片
def fetch_image(url):
    try:
        headers = {
            "User-Agent": USER_AGENT,
            "Referer": REFERER,
            "authorization": AUTHORIZATION
        }
        s = requests.get(url, headers=headers)
    except Exception as e:
        print("fetch last activities fail. " + url)
        raise e

    return s.content


# 指定 url，获取对应 JSON 返回 / 话题列表
def fetch_activities(url):
    try:
        headers = {
            "User-Agent": USER_AGENT,
            "Referer": REFERER,
            "authorization": AUTHORIZATION
        }
        s = requests.get(url, headers=headers)
    except Exception as e:
        print("fetch last activities fail. " + url)
        raise e

    return s.json()


# 处理返回的话题列表
def process_activities(datums, face_detective):
    for data in datums["data"]:

        target = data["target"]
        if "content" not in target or "question" not in target or "author" not in target:
            continue

        # 解析列表中每一个元素的内容
        html = etree.HTML(target["content"])

        seq = 0

        # question_url = target["question"]["url"]
        question_title = target["question"]["title"]

        author_name = target["author"]["name"]
        # author_id = target["author"]["url_token"]

        print("current answer: " + question_title + " author: " + author_name)

        # 获取所有图片地址
        images = html.xpath("//img/@src")
        for image in images:
            if not image.startswith("http"):
                continue
            s = fetch_image(image)

            # 请求人脸检测服务
            scores = face_detective(s)

            for score in scores:
                filename = ("%d--" % score) + author_name + "--" + question_title + ("--%d" % seq) + ".jpg"
                filename = re.sub(r'(?u)[^-\w.]', '_', filename)
                # 注意文件名的处理，不同平台的非法字符不一样，这里只做了简单处理，特别是 author_name / question_title 中的内容
                seq = seq + 1
                with open(os.path.join(DIR, filename), "wb") as fd:
                    fd.write(s)

            # 人脸检测 免费，但有 QPS 限制
            time.sleep(2)

    if not datums["paging"]["is_end"]:
        # 获取后续讨论列表的请求 url
        return datums["paging"]["next"]
    else:
        return None


def get_valid_filename(s):
    s = str(s).strip().replace(' ', '_')
    return re.sub(r'(?u)[^-\w.]', '_', s)


import base64


def detect_face(image, token):
    try:
        URL = "https://aip.baidubce.com/rest/2.0/face/v3/detect"
        params = {
            "access_token": token
        }
        data = {
            "face_field": "age,gender,beauty,qualities",
            "image_type": "BASE64",
            "image": base64.b64encode(image)
        }
        s = requests.post(URL, params=params, data=data)
        return s.json()["result"]
    except Exception as e:
        print("detect face fail. " + url)
        raise e


def fetch_auth_token(api_key, secret_key):
    try:
        URL = "https://aip.baidubce.com/oauth/2.0/token"
        params = {
            "grant_type": "client_credentials",
            "client_id": api_key,
            "client_secret": secret_key
        }
        s = requests.post(URL, params=params)
        return s.json()["access_token"]
    except Exception as e:
        print("fetch baidu auth token fail. " + url)
        raise e


def init_face_detective(app_id, api_key, secret_key):
    # client = AipFaceUtil(app_id, api_key, secret_key)
    # 百度云 V3 版本接口，需要先获取 access token
    token = fetch_auth_token(api_key, secret_key)

    def detective(image):
        # r = client.detect(image, options)
        # 直接使用 HTTP 请求
        r = detect_face(image, token)
        # 如果没有检测到人脸
        if r is None or r["face_num"] == 0:
            return []

        scores = []
        for face in r["face_list"]:
            # 人脸置信度太低
            if face["face_probability"] < 0.6:
                continue
            # 颜值低于阈值
            if face["beauty"] < BEAUTY_THRESHOLD:
                continue
            # 性别非女性
            if face["gender"]["type"] != "female":
                continue
            scores.append(face["beauty"])

        return scores

    return detective


def init_env():
    if not os.path.exists(DIR):
        os.makedirs(DIR)


init_env()
face_detective = init_face_detective(APP_ID, API_KEY, SECRET_KEY)

url = BASE_URL % SOURCE + URL_QUERY
while url is not None:
    print("current url: " + url)
    datums = fetch_activities(url)
    url = process_activities(datums, face_detective)
    # 注意节操，爬虫休息间隔不要调小
    time.sleep(5)


    # vim: set ts=4 sw=4 sts=4 tw=100 et:


    "E:\Program Files\Java\jdk1.8.0_181\bin\java.exe" -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:54796,suspend=y,server=n -javaagent:C:\Users\pc\.IntelliJIdea2018.3\system\captureAgent\debugger-agent.jar -Dfile.encoding=UTF-8 -classpath "E:\Program Files\Java\jdk1.8.0_181\jre\lib\charsets.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\deploy.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\access-bridge-64.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\cldrdata.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\dnsns.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jaccess.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jfxrt.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\localedata.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\nashorn.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunec.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunjce_provider.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunmscapi.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunpkcs11.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\zipfs.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\javaws.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\jce.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\jfr.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\jfxswt.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\jsse.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\management-agent.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\plugin.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\resources.jar;E:\Program Files\Java\jdk1.8.0_181\jre\lib\rt.jar;G:\project\myFinance\target\classes;F:\repository\org\springframework\boot\spring-boot-starter-web\2.1.9.RELEASE\spring-boot-starter-web-2.1.9.RELEASE.jar;F:\repository\org\springframework\boot\spring-boot-starter\2.1.9.RELEASE\spring-boot-starter-2.1.9.RELEASE.jar;F:\repository\org\springframework\boot\spring-boot\2.1.9.RELEASE\spring-boot-2.1.9.RELEASE.jar;F:\repository\org\springframework\boot\spring-boot-autoconfigure\2.1.9.RELEASE\spring-boot-autoconfigure-2.1.9.RELEASE.jar;F:\repository\org\springframework\boot\spring-boot-starter-logging\2.1.9.RELEASE\spring-boot-starter-logging-2.1.9.RELEASE.jar;F:\repository\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar;F:\repository\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;F:\repository\org\apache\logging\log4j\log4j-to-slf4j\2.11.2\log4j-to-slf4j-2.11.2.jar;F:\repository\org\slf4j\jul-to-slf4j\1.7.28\jul-to-slf4j-1.7.28.jar;F:\repository\org\yaml\snakeyaml\1.23\snakeyaml-1.23.jar;F:\repository\org\springframework\boot\spring-boot-starter-json\2.1.9.RELEASE\spring-boot-starter-json-2.1.9.RELEASE.jar;F:\repository\com\fasterxml\jackson\core\jackson-databind\2.9.9.3\jackson-databind-2.9.9.3.jar;F:\repository\com\fasterxml\jackson\core\jackson-annotations\2.9.0\jackson-annotations-2.9.0.jar;F:\repository\com\fasterxml\jackson\core\jackson-core\2.9.9\jackson-core-2.9.9.jar;F:\repository\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.9.9\jackson-datatype-jdk8-2.9.9.jar;F:\repository\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.9.9\jackson-datatype-jsr310-2.9.9.jar;F:\repository\com\fasterxml\jackson\module\jackson-module-parameter-names\2.9.9\jackson-module-parameter-names-2.9.9.jar;F:\repository\org\hibernate\validator\hibernate-validator\6.0.17.Final\hibernate-validator-6.0.17.Final.jar;F:\repository\javax\validation\validation-api\2.0.1.Final\validation-api-2.0.1.Final.jar;F:\repository\org\jboss\logging\jboss-logging\3.3.3.Final\jboss-logging-3.3.3.Final.jar;F:\repository\com\fasterxml\classmate\1.4.0\classmate-1.4.0.jar;F:\repository\org\springframework\spring-web\5.1.10.RELEASE\spring-web-5.1.10.RELEASE.jar;F:\repository\org\springframework\spring-webmvc\5.1.10.RELEASE\spring-webmvc-5.1.10.RELEASE.jar;F:\repository\org\springframework\spring-context\5.1.10.RELEASE\spring-context-5.1.10.RELEASE.jar;F:\repository\org\springframework\spring-expression\5.1.10.RELEASE\spring-expression-5.1.10.RELEASE.jar;F:\repository\javax\annotation\javax.annotation-api\1.3.2\javax.annotation-api-1.3.2.jar;F:\repository\org\slf4j\slf4j-api\1.7.28\slf4j-api-1.7.28.jar;F:\repository\org\springframework\spring-core\5.1.10.RELEASE\spring-core-5.1.10.RELEASE.jar;F:\repository\org\springframework\spring-jcl\5.1.10.RELEASE\spring-jcl-5.1.10.RELEASE.jar;F:\repository\org\springframework\boot\spring-boot-starter-data-mongodb\2.1.9.RELEASE\spring-boot-starter-data-mongodb-2.1.9.RELEASE.jar;F:\repository\org\mongodb\mongodb-driver\3.8.2\mongodb-driver-3.8.2.jar;F:\repository\org\mongodb\bson\3.8.2\bson-3.8.2.jar;F:\repository\org\mongodb\mongodb-driver-core\3.8.2\mongodb-driver-core-3.8.2.jar;F:\repository\org\springframework\data\spring-data-mongodb\2.1.11.RELEASE\spring-data-mongodb-2.1.11.RELEASE.jar;F:\repository\org\springframework\spring-tx\5.1.10.RELEASE\spring-tx-5.1.10.RELEASE.jar;F:\repository\org\springframework\data\spring-data-commons\2.1.11.RELEASE\spring-data-commons-2.1.11.RELEASE.jar;F:\repository\io\netty\netty-all\4.1.6.Final\netty-all-4.1.6.Final.jar;F:\repository\org\springframework\boot\spring-boot-starter-freemarker\2.1.9.RELEASE\spring-boot-starter-freemarker-2.1.9.RELEASE.jar;F:\repository\org\freemarker\freemarker\2.3.29\freemarker-2.3.29.jar;F:\repository\org\springframework\spring-context-support\5.1.10.RELEASE\spring-context-support-5.1.10.RELEASE.jar;F:\repository\commons-codec\commons-codec\1.9\commons-codec-1.9.jar;F:\repository\org\springframework\spring-aop\5.1.10.RELEASE\spring-aop-5.1.10.RELEASE.jar;F:\repository\org\springframework\spring-beans\5.1.10.RELEASE\spring-beans-5.1.10.RELEASE.jar;F:\repository\org\aspectj\aspectjrt\1.9.2\aspectjrt-1.9.2.jar;F:\repository\org\aspectj\aspectjweaver\1.9.2\aspectjweaver-1.9.2.jar;F:\repository\com\google\code\gson\gson\2.8.1\gson-2.8.1.jar;F:\repository\org\apache\logging\log4j\log4j-nosql\2.9.0\log4j-nosql-2.9.0.jar;F:\repository\org\apache\logging\log4j\log4j-core\2.11.2\log4j-core-2.11.2.jar;F:\repository\org\apache\logging\log4j\log4j-api\2.11.2\log4j-api-2.11.2.jar;F:\repository\com\baidu\aip\java-sdk\4.4.0\java-sdk-4.4.0.jar;F:\repository\org\json\json\20160810\json-20160810.jar;F:\repository\log4j\log4j\1.2.17\log4j-1.2.17.jar;E:\Program Files\JetBrains\IntelliJ IDEA 2018.3.6\lib\idea_rt.jar" com.sinjee.faces.Client
Connected to the target VM, address: '127.0.0.1:54796', transport: 'socket'
0 [main] INFO com.baidu.aip.client.BaseClient  - get access_token success. current state: STATE_AIP_AUTH_OK
6 [main] DEBUG com.baidu.aip.client.BaseClient  - current state after check priviledge: STATE_TRUE_AIP_USER
{
    "result": {
        "face_num": 1,
        "face_list": [{
            "gender": {
                "probability": 1,
                "type": "female"
            },
            "beauty": 88.36,
            "liveness": {"livemapscore": 0.93},
            "angle": {
                "roll": 25.81,
                "pitch": 11.99,
                "yaw": -11.92
            },
            "face_token": "cc73d040ac47ae0588fec95540d8c011",
            "location": {
                "top": 75.05,
                "left": 258.31,
                "rotation": 26,
                "width": 171,
                "height": 162
            },
            "face_probability": 1,
            "age": 22
        }]
    },
    "log_id": 744193231417490091,
    "error_msg": "SUCCESS",
    "cached": 0,
    "error_code": 0,
    "timestamp": 1573141749
}
Disconnected from the target VM, address: '127.0.0.1:54796', transport: 'socket'

Process finished with exit code 0

https://ai.baidu.com/docs#/Face-Java-SDK/top


