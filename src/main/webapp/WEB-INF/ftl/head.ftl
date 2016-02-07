<#import "/spring.ftl" as spring>
<#import "macros.ftl" as f>
<#-- @ftlvariable name="currentUser" type="pl.pogorzelski.webconverter.domain.dto.CurrentUser" -->
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Best Converter</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/style.css"/>

    <script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
    <script src="https://code.jquery.com/ui/1.10.4/jquery-ui.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>

<nav class="navbar navbar-fixed-top navbar-default" role="navigation">

    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" rel="home" href="/" title="Converter ">Converter</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">

            <#if currentUser?? >
                <#if currentUser.role == "ADMIN">
                    <li><a href="/user/create">Create a new user</a></li>
                    <li><a href="/users">View all users</a></li>
                    <li><a href="/newconverter">Add Converter</a></li>
                </#if>
                <li><a href="/user/${currentUser.id}">Profile</a></li>
                <li><a href="/convert">Convert</a></li>
                <li><a href="/tasks">My Conversions</a></li>
                <li><a href="/files">Files</a></li>
            </#if>
            </ul>
            <ul class="nav navbar-nav"
            <div class="pull-right">
            <#if !currentUser??>
                <li><a href="/login">Log in</a></li>
                <li><a href="/register">Register</a></li>
            </#if>
            <#if currentUser??>
                <li><a href="/logout">Log out</a></li>
            </#if>
            </div>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <br><br><br><br>