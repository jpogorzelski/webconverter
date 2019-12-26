<#include "head.ftl"/>
<@f.insertHeader "newconverter"/>

<script language="Javascript" type="text/javascript" src="/js/editarea_0_8_2/edit_area/edit_area_full.js"></script>
<script language="Javascript" type="text/javascript">
    // initialisation
    editAreaLoader.init({
        id: "sourceCode"	// id of the textarea to transform
        ,start_highlight: true	// if start with highlight
        ,allow_resize: "both"
        ,allow_toggle: true
        ,word_wrap: true
        ,language: "en"
        ,syntax: "java"
    });

</script>






<@spring.bind "form" />
<#if spring.status.error>
<div class="alert alert-danger">
    <#list spring.status.errorMessages as error>
        <p>${error}</p>
    </#list>
</div>
</#if>

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
        <label for="sourceCode">Source code (Java) <a id="helpSourceCode" href="#"><strong>HELP</strong></a>: </label>
        <div id="help" style="display: none">
            Paste here content of class file that implements <a href="/BaseConverter.java">BaseConverter.java</a>, e.g.
            <a href="/ExamplePDFtoPNGConverter.java">ExamplePDFtoPNGConverter.java</a>
        </div>

        <@spring.formTextarea path="form.sourceCode" attributes='class="form-control" rows="15" cols="110"' />
        <@spring.showErrors "<br>" "err" />

    </div>

    <input type="submit" class="btn btn-success"/>
</form>


<script>
    $("#helpSourceCode").click(function () {
        $("#help").toggle();
    });
</script>

<#include "footer.ftl" />