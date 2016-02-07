<#include "head.ftl" />
<#-- @ftlvariable name="error" type="java.util.Optional<String>" -->

<@f.insertHeader "login"/>

<#if error.isPresent()>
<p class="alert alert-danger">The email or password you have entered is invalid, try again.</p>
</#if>
<p>You can use: admin@localhost / demo</p>

<form role="form" class="form-signin" action="/login" method="post">
    <label for="email" class="sr-only">Email address</label>
    <input type="email" class="form-control" name="email" id="email" required autofocus placeholder="Email address">

    <label for="password" class="sr-only">Password</label>
    <input class="form-control" type="password" name="password" id="password" required placeholder="Password">

    <label for="remember-me">Remember me</label>
    <input type="checkbox" name="remember-me" id="remember-me"/>

    <button type="submit" class="btn btn-lg btn-primary btn-block">Sign in</button>
</form>



<#include "footer.ftl" />