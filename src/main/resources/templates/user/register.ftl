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
	     <dd>
 		    <span class="text-element">
	     		<input type="text" name="username" size="30" maxlength="50" value="${username!}">
	     	</span>
	     <dt>Passwort:
	     <dd>
		     <span class="text-element">
		     	<input type="password" name="password" size="30">
		     </span>
	     <dt>Passwort wiederholen:
	     <dd>
		     <span class="text-element">
			     <input type="password" name="password2" size="30">
		     </span>
	   </dl>
	   <div class="actions"><button class="button colored" type="submit"><i class="far fa-user"></i> Registrieren</button></div>
	 </form>
</@layout.masterTemplate>