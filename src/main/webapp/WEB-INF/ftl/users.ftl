<#-- @ftlvariable name="users" type="java.util.List<pl.pogorzelski.webconverter.domain.User>" -->
<#include "head.ftl" />
<h1>List of Users</h1>

<table class="table">
    <thead>
    <tr>
        <th>E-mail</th>
        <th>Role</th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td><a href="/user/${user.id}">${user.email}</a></td>
        <td>${user.role}</td>
    </tr>
    </#list>
    </tbody>
</table>
<#include "footer.ftl" />