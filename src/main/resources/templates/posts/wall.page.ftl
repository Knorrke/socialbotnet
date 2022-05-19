<#ftl output_format="XML">
<#import "../layout/layout.ftl" as layout />
<#if user??>
	<#assign pageTitle>${user.username}s Pinnwand</#assign>
<#else>
	<#assign pageTitle>Öffentliche Pinnwand</#assign>
</#if>

<@layout.masterTemplate title="Pinnwand" colored=false pageTitle=pageTitle>
    <#include "./wall.ftl">
</@layout.masterTemplate>