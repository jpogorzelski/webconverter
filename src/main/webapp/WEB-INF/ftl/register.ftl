<#include "head.ftl" />
<#-- @ftlvariable name="form" type="pl.pogorzelski.webconverter.domain.dto.RegisterForm" -->
<@f.insertHeader "register"/>

<@spring.bind "form" />
<#if spring.status.error>
<div class="alert alert-danger">
    <#list spring.status.errorMessages as error>
        <p>${error}</p>
    </#list>
</div>
</#if>

<form role="form" name="form" action="" method="post">

    <div class="form-group">
        <label for="email">Email address</label>
        <input class="form-control" type="email" name="email" id="email" value="${form.email}" required autofocus/>
    </div>
    <div class="form-group">
        <label for="password">Password</label>
        <input class="form-control" type="password" name="password" id="password" required/>
    </div>
    <div class="form-group">
        <label for="passwordRepeated">Repeat</label>
        <input class="form-control" type="password" name="passwordRepeated" id="passwordRepeated" required/>
    </div>

    <button type="submit" class="btn btn-">Save</button>
</form>



<#include "footer.ftl" />