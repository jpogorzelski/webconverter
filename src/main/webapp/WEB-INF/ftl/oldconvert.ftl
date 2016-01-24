<#include "head.ftl" />
<#-- @ftlvariable name="form" type="pl.pogorzelski.webconverter.domain.dto.ConvertForm" -->

<h1>Convert pdf to png:</h1>

<form role="form" name="form" action="" method="post" enctype="multipart/form-data">

    <div class="form-group">

        <input class="form-control" type="file" name="file" id="file" required/>
    </div>
    <div class="form-group">
        <label for="name">Name</label>
        <input class="form-control" type="name" name="name" id="name" required/>
    </div>

    <button type="submit" class="btn btn-default">Convert</button>
</form>

<@spring.bind "form" />
<#if spring.status.error>
<ul>
    <#list spring.status.errorMessages as error>
        <li>${error}</li>
    </#list>
</ul>
</#if>

<#include "footer.ftl"/>