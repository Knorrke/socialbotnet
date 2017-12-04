<#import "../layout/layout.ftl" as layout />

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
    <form action="/user/update" method="post">
	   <dl>
	     <dt>Nutzername:
	     <dd><input type="text" name="username" size="30" maxlength="50" value="${authenticatedUser.username!}">
	   </dl>
	   <dl>
	     <dt>Hobbies:
	     <dd><textarea name="hobbies" cols="30">${authenticatedUser.hobbies!}</textarea>
	     <dt>&Uuml;ber mich:
	     <dd><textarea name="about" cols="30">${authenticatedUser.about!}</textarea>
	   </dl>
	   <div class="actions"><input type="submit" value="Aktualisieren"></div>
	 </form>
</@layout.masterTemplate>