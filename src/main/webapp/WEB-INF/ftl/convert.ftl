<#include "head.ftl" />
<#-- @ftlvariable name="currentUser" type="pl.pogorzelski.webconverter.domain.dto.CurrentUser" -->
<#assign findSourceFormatsURL="/sources" />
<#assign findTargetFormatsURL="/targets" />

<#if message??>
<div class="${message.type.cssClass}">${message.text}</div>
</#if>

<@f.insertHeader "convert"/>
<#if currentUser.user.currentConversionCount gte currentUser.user.conversionCountLimit>
You have exceeded limit of free conversions.
<#else>
    <@spring.bind "selectConverterForm" />
    <#if spring.status.error>
    <div class="alert alert-danger">
        <#list spring.status.errorMessages as error>
            <p>${error}</p>
        </#list>
    </div>
    </#if>
<form name="selectConverterForm" action="/convert" method="post" role="form" enctype="multipart/form-data"
      class="form-inline">
    <fieldset>
        <div class="form-group">
        <#--<select class="form-control" name="sourceFormat" id="sourceFormat" > </select>-->
            <@spring.formSingleSelect path="selectConverterForm.sourceFormat" attributes='class="form-control has-error"' options={"":"Source Format"}/>
            <@spring.formSingleSelect path="selectConverterForm.targetFormat" attributes='class="form-control"' options={"":"Target Format"}/>
            <#--<select class="form-control" name="targetFormat" id="targetFormat"> </select>-->
        </div>
        <div class="form-group">
        <#--<input class="form-control" type="file" name="file" id="file" required/>-->
            <@f.textInput path="selectConverterForm.file" type="file"/>
        </div>
    </fieldset>
    <br>
    <p>
        <button type="submit" class="btn btn-success">Convert</button>
    </p>
</form>


<div id="output"></div>


<script type="text/javascript">
    $(document).ready(
            function () {
                $.getJSON('${findSourceFormatsURL}', {
                    ajax: 'true'
                }, function (data) {
                    console.log(data);
                    var html = '<option value="">Source Format</option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i] + '">'
                                + data[i] + '</option>';
                    }
                    html += '</option>';
                    $('#sourceFormat').html(html);
                });
            });
</script>


<script type="text/javascript">
    $(document).ready(function () {
        $('#sourceFormat').change(
                function () {
                    $.getJSON('${findTargetFormatsURL}', {
                        sourceFormat: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = '<option value="">Target Format</option>';
                        var len = data.length;
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i] + '">'
                                    + data[i] + '</option>';
                        }
                        html += '</option>';
                        $('#targetFormat').html(html);
                    });
                });
    });
</script>
<script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="https://code.jquery.com/ui/1.10.4/jquery-ui.min.js"></script>
</#if>
<#include "footer.ftl" />