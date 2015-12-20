<#include "head.ftl" />
<h1>Create a new user</h1>

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
    <div class="form-group">
        <label for="role">Role</label>
        <select name="role" id="role" required>
            <option <#if form.role == 'USER'>selected</#if>>USER</option>
            <option <#if form.role == 'ADMIN'>selected</#if>>ADMIN</option>
        </select>
    </div>
    <button type="submit" class="btn btn-default">Save</button>
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