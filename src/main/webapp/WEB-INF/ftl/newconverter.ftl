<#include "head.ftl"/>
<h1>Register new converter</h1>

<form role="form" name="form" action="" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <label for="sourceFormat">Source Format: </label>
        <@spring.formInput attributes='class="form-control"' fieldType="text" path="form.sourceFormat" />
        <@spring.showErrors "<br>" "err" />
    </div>
    <div class="form-group">
        <label for="targetFormat">Target Format: </label>
        <@spring.formInput attributes='class="form-control"' fieldType="text" path="form.targetFormat" />
        <@spring.showErrors "<br>" "err" />
    </div>
    <div class="form-group">
        <label for="sourceCode">Source code (Groovy): </label>

        <@spring.formTextarea path="form.sourceCode" attributes='class="form-control" rows="15" cols="110"' />
        <@spring.showErrors "<br>" "err" />

    </div>

    <input type="submit" class="btn btn-success"/>
</form>

<#--<@spring.bind "form" />
<#if spring.status.error>
<ul>
    <#list spring.status.errorMessages as error>
        <li>${error}</li>
    </#list>
</ul>
</#if>-->


<#include "footer.ftl" />