<#include "head.ftl" />
<#-- @ftlvariable name="error" type="java.util.Optional<String>" -->

<h1>Log in</h1>

<p>You can use: demo / demo</p>

<form role="form" action="/login" method="post">

    <div class="form-group">
        <label for="email">Email address</label>
        <input class="form-control" name="email" id="email" required autofocus/>
    </div>
    <div class="form-group">
        <label for="password">Password</label>
        <input class="form-control" type="password" name="password" id="password" required/>
    </div>
    <div class="form-group">
        <label for="remember-me">Remember me</label>
        <input type="checkbox" name="remember-me" id="remember-me"/>
    </div>
    <button type="submit" class="btn btn-default">Sign in</button>
</form>

<#if error.isPresent()>
<p>The email or password you have entered is invalid, try again.</p>
</#if>
<#include "footer.ftl" />