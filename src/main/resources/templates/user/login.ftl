<#import "../layout/layout.ftl" as layout />

<@layout.masterTemplate title="Anmelden">
    <h2>Anmelden</h2>
    <#if message??>
    	<div class="success">
    		${message}
    	</div>
    </#if>
    <#if error??>
    	<div class="error">
    		<strong>Error:</strong> ${error}
    	</div>
    </#if>
    <form action="/login" method="post">
	   <dl>
	     <dt>Nutzername:
	     <dd><input type="text" name="username" size="30" maxlength="50" value="${username!}">
	     <dt>Passwort:
	     <dd><input type="password" name="password" size="30">
	   </dl>
	   <div class="actions"><input type="submit" value="Sign In"></div>
	 </form>
</@layout.masterTemplate>