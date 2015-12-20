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


<#--<script type="text/javascript">
    $(document).ready(function () {
        $("#targetFormat").change(onSelectChange);
    });
    function onSelectChange() {
        var selected = $("#targetFormat option:selected");
        var output = "";
        if (selected.val() != 0) {
            output = "Target format:  " + selected.text();
        }
        $("#output").html(output);
    }
</script>-->


<#if message??>
<div class="${message.type.cssClass}">${message.text}</div>
</#if>

<#assign convertUrl="/convertNew" />
<h2>Convert</h2>
<form name="selectConverterForm" action="${convertUrl}" method="post" role="form" class="form-inline"
      modelAttribute="selectConverterForm">


    <fieldset>
        <div class="form-group">
            <select class="form-control" id="sourceFormat" path="sourceFormat"> </select>
            <select class="form-control" id="targetFormat" path="targetFormat">
                <option value="">Target Format</option>
            </select>
        </div>
        <div class="form-group">
            <input class="form-control" type="file" name="file" id="file" required/>
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