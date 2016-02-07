<#-- @ftlvariable name="users" type="java.util.List<pl.pogorzelski.webconverter.domain.User>" -->
<#include "head.ftl" />
<@f.insertHeader "users"/>

<table class="table">
    <thead>
    <tr>
        <th>E-mail</th>
        <th>Role</th>
        <th>Conversion limit</th>
        <th>Current conversion count</th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td><a href="/user/${user.id}">${user.email}</a></td>
        <td>${user.role}</td>
        <td>${user.conversionCountLimit}</td>
        <td>${user.currentConversionCount}</td>
    </tr>
    </#list>
    </tbody>
</table>
<#include "footer.ftl" />