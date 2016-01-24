<#import "/spring.ftl" as spring>
<#--
* textInput
*
* @param path the name of the field to bind to
* @param attributes any additional attributes for the input element (such as class or CSS styles or size)
* @param messageKey key to lookup in resource bundle, defaults to path
* @param type input type: defaults to "text"
-->
<#macro textInput path class="" label="" type="text" required="">
    <@spring.bind path/>
    <#assign error>
        <#if spring.status.errorMessages?has_content>error</#if>
    </#assign>
<div class="control-group ${error}">
    <#assign id="${spring.status.expression?replace('[','')?replace(']','')}">
    <#if label?? && label!="">
        <label class="control-label" for="${id}">
            <@spring.message label/>
        </label>
    </#if>
    <div class="controls">
        <input type="${type}" id="${id}"
               name="${spring.status.expression}"
               value="${spring.stringStatusValue}" class="form-control ${class}" required="${required}">
        <#if error?has_content>
            <span class="help-inline">
                ${spring.status.errorMessages?first!}
            </span>
        </#if>
        <#nested>
    </div>
</div>
</#macro>
