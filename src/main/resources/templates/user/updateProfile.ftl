<#import "../layout/layout.ftl" as layout />

<@layout.masterTemplate pageTitle="Profil aktualisieren">
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
    <form action="/user/update" method="post">
	   <dl>
	     <dt>Nutzername:
	     <dd><input type="text" name="username" size="30" maxlength="50" value="<#if authenticatedUser??>${authenticatedUser.username!}</#if>">
	   </dl>
	   <dl>
	     <dt>Hobbies:
	     <dd><textarea name="hobbies" cols="30"></textarea>
	     <dt>&Uuml;ber mich:
	     <dd><textarea name="about" cols="30"></textarea>
	   </dl>
	   <div class="actions"><input type="submit" value="Aktualisieren"></div>
	 </form>
</@layout.masterTemplate>