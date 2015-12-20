<#-- @ftlvariable name="user" type="pl.pogorzelski.webconverter.domain.User" -->
<#include "head.ftl" />
<h1>User details</h1>

<p>E-mail: ${user.email}</p>

<p>Role: ${user.role}</p>
<#include "footer.ftl" />