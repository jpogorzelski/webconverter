<#import "/spring.ftl" as spring>
<#-- @ftlvariable name="currentUser" type="pl.pogorzelski.webconverter.domain.dto.CurrentUser" -->
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Best Converter</title>
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <nav class="navbar navbar-fixed-top navbar-default" role="navigation">
        <ul class="nav navbar-nav ">

        <#if currentUser?? >
            <#if currentUser.role == "ADMIN">
                <li><a href="/user/create">Create a new user</a></li>
                <li><a href="/users">View all users</a></li>
            </#if>
            <li><a href="/user/${currentUser.id}">Profile</a></li>
            <li><a href="/convert">Convert</a></li>
            <li><a href="/newconverter">New Converter</a></li>
            <li><a href="/test">Test</a></li>
        </#if>
        <#-- </ul>
         <ul class="nav navbar-nav"-->
        <#--<div class="pull-right">-->
        <#if !currentUser??>
            <li><a href="/login">Log in</a></li>
        </#if>
        <#if currentUser??>
            <li><a href="/logout">Log out</a></li>
        </#if>
        <#--</div>-->
        </ul>
    </nav>
    <br><br><br><br>