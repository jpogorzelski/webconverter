<#-- @ftlvariable name="user" type="pl.pogorzelski.webconverter.domain.User" -->
<#include "head.ftl" />
<@f.insertHeader "profile"/>

<p>E-mail: ${user.email}</p>
<p>Role: ${user.role}</p>
<p>Conversion limit: ${user.conversionCountLimit}</p>
<p>Current conversion count: ${user.currentConversionCount}</p>
<#include "footer.ftl" />