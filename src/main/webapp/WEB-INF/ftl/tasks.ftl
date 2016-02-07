<#include "head.ftl" />
<#-- @ftlvariable name="waitingTasks" type="java.util.ArrayList<pl.pogorzelski.webconverter.queue.ConvertTask>" -->
<#-- @ftlvariable name="inProgressOrFinishedTasks" type="java.util.ArrayList<pl.pogorzelski.webconverter.queue.ConvertTask>" -->

<@f.insertHeader "tasks"/>
<@f.insertTasks waitingTasks "waiting"/>
<@f.insertTasks inProgressOrFinishedTasks "finished" />

<#include "footer.ftl" />
