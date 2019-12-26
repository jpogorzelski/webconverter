<#include "head.ftl" />
<#-- @ftlvariable name="phoneNo" type="String" -->

<@f.insertHeader "unlock"/>
<form name="unlockForm" action="/unlock<#if step2??>2</#if>" role="form" class="form" method="post" >
    <label for="phoneNo" class="sr-only"><@spring.message "phoneNo"/></label>

    <input type="tel" class="form-control" name="phoneNo" id="phoneNo" placeholder="<@spring.message "phoneNo"/>" value="${phoneNo!}"/>

    <label for="code" class="sr-only"><@spring.message "code"/></label>
    <input type="text" class="form-control" name="code" id="code" placeholder="<@spring.message "code"/>">
    <input type="submit"/>
</form>


<script>

    $(document).ready(function(){
        $("label[for='phoneNo']").show();
        $("#phoneNo").show();
        $("label[for='code']").hide();
        $("#code").hide();

        <#if step2??>
            $("label[for='phoneNo']").hide();
            $("#phoneNo").hide();
            $("label[for='code']").show();
            $("#code").show();
        </#if>
    });
</script>
<#include "footer.ftl" />
