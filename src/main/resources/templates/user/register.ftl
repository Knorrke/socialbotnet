<#import "../layout/layout.ftl" as layout />

<@layout.masterTemplate pageTitle="Registrieren">
    <#if success??>
    	<div class="success">
    		${success}
    	</div>
    </#if>
    <#if error??>
    	<div class="error">
    		<strong>Error:</strong> ${error}
    	</div>
    </#if>
    <form action="/registrieren" method="post">
	   <dl>
	     <dt>Nutzername:
	     <dd><input type="text" name="username" size="30" maxlength="50" value="${username!}">
	     <dt>Passwort:
	     <dd><input type="password" name="password" size="30">
	     <dt>Passwort wiederholen:
	     <dd><input type="password" name="password2" size="30">
	   </dl>
	   <div class="actions"><input type="submit" value="Registrieren"></div>
	 </form>
</@layout.masterTemplate>