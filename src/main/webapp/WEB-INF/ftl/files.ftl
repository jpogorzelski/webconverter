<#-- @ftlvariable name="files" type="java.util.List<pl.pogorzelski.webconverter.domain.FileEntry>" -->
<#include "head.ftl" />
<@f.insertHeader "files"/>

<table class="table">
    <thead>
    <tr>
        <th>File name</th>
        <th>Download link</th>
    </tr>
    </thead>
    <tbody>
    <#list files as file>
    <tr>
        <td>${file.name}</td>
        <td><a href="/download/${file.md5Hash}">Download ${file.id}</a></td>
    </tr>
    </#list>
    </tbody>
</table>
<#include "footer.ftl" />