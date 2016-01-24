<#include "head.ftl" />
<script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="https://code.jquery.com/ui/1.10.4/jquery-ui.min.js"></script>

<#assign findSourceFormatsURL="/sources" />
<#assign findTargetFormatsURL="/targets" />

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



<#if message??>
<div class="${message.type.cssClass}">${message.text}</div>
</#if>


<h2>Convert</h2>
<form name="selectConverterForm" action="/convert" method="post" role="form"
      enctype="multipart/form-data" >


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
    <p>
        <button type="submit">Convert</button>
    </p>
</form>


<div id="output"></div>

<@spring.bind "selectConverterForm" />
<#if spring.status.error>
<ul>
    <#list spring.status.errorMessages as error>
        <li>${error}</li>
    </#list>
</ul>
</#if>

<#include "footer.ftl" />