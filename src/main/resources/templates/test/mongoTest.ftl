<html>
<#include "../common/header.ftl">

<body>
<div id="wrapper" class="toggled">

    <#--边栏sidebar-->
    <#include "../common/nav.ftl">

    <#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-hover table-condensed">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>姓名</th>
                            <th>年龄</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list MongoTestDtoList as mongoTestDto>
                            <tr>
                                <td>${mongoTestDto.getId()}</td>
                                <td>${mongoTestDto.getUserName()}</td>
                                <td>${mongoTestDto.getAge()}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <#--分页-->
                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                        <#if currentPage lte 1>
                            <li class="disabled"><a href="#">上一页</a></li>
                        <#else>
                            <li><a href="/myFinance/MongoTest/list?page=${currentPage - 1}&size=${size}">上一页</a></li>
                        </#if>

                        <#list 1..totalPage as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                            <#else>
                                <li><a href="/myFinance/MongoTest/list?page=${index}&size=${size}">${index}</a></li>
                            </#if>
                        </#list>

                        <#if currentPage gte totalPage>
                            <li class="disabled"><a href="#">下一页</a></li>
                        <#else>
                            <li><a href="/myFinance/MongoTest/list?page=${currentPage + 1}&size=${size}">下一页</a></li>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
</html>