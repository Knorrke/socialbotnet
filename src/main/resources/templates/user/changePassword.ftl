<#import "../layout/layout.ftl" as layout />
<#import "../common/fa-icons.ftl" as fa/>

<@layout.masterTemplate pageTitle="Profil aktualisieren">
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
    <form action="/user/change-password" method="post">
	   <dl>
	     <dt>Aktuelles Passwort:
	     <dd>
		     <span class="text-element">
			 	<input type="password" name="password" size="30">
		     </span>
		  </dd>
	   </dl>
	   <dl>
	     <dt>Neues Passwort:
	     <dd>
		     <span class="text-element">
			 	<input type="password" name="new_password" size="30">
		     </span>
	     </dd>
	     <dt>Neues Passwort wiederholen:
	     <dd>
		     <span class="text-element">
			 	<input type="password" name="new_password2" size="30">
		     </span>
	     </dd>
	   </dl>
	   <div class="actions"><button class="button colored" type="submit"><@fa.icon fa="save" /> Aktualisieren</button></div>
	 </form>
</@layout.masterTemplate>