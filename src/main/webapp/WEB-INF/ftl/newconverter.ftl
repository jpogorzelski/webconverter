<#include "head.ftl"/>
<h1>Register new converter</h1>

<form role="form" name="form" action="" method="post">
    <div class="form-group">
        <label for="sourceFormat">Source Format: </label>
        <input class="form-control" type="text" name="sourceFormat" id="sourceFormat"/>
    </div>
    <div class="form-group">
        <label for="targetFormat">Target Format: </label>
        <input class="form-control" type="text" name="targetFormat" id="targetFormat"/>
    </div>
    <div class="form-group">
        <label for="sourceCode">Source code (Groovy): </label>
        <textarea class="form-control" name="sourceCode" id="sourceCode" rows="15" cols="110"></textarea>
    </div>

    <input type="submit" class="btn btn-success"/>
</form>

<@spring.bind "form" />
<#if spring.status.error>
<ul>
    <#list spring.status.errorMessages as error>
        <li>${error}</li>
    </#list>
</ul>
</#if>


<#include "footer.ftl" />