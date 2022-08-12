<#import "../layout/layout.ftl" as layout />
<#import "../common/fa-icons.ftl" as fa/>

<@layout.masterTemplate pageTitle="Anmelden">
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
    <form action="/login" method="post">
	   <dl>
	     <dt>Nutzername:
	     <dd>
		     <span class="text-element">
			     <input type="text" name="username" size="30" maxlength="50" value="${username!}">
			 </span>
	     <dt>Passwort:
	     <dd>
		     <span class="text-element">
			 	<input type="password" name="password" size="30">
			 </span>
	   </dl>
	   <div class="actions"><button class="button colored" type="submit"><@fa.icon fa="user" /> Anmelden</button></div>
	 </form>
</@layout.masterTemplate>