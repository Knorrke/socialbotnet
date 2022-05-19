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
	     <dd>
		     <span class="text-element">
		     	<input type="text" name="username" size="30" maxlength="50" value="${authenticatedUser.username!}">
		     </span>
	   </dl>
	   <dl>
	     <dt>Hobbies:
	     <dd>
		     <span class="text-element">
		     	<textarea name="hobbies" cols="30" rows="4">${authenticatedUser.hobbies!}</textarea>
	     	</span>
	     <dt>Über mich:
	     <dd>
		     <span class="text-element">
			     <textarea name="about" cols="30" rows="4">${authenticatedUser.about!}</textarea>
		     </span>
	   </dl>
	   <div class="actions"><button class="button colored" type="submit"><i class="far fa-save"></i> Aktualisieren</button></div>
	 </form>
</@layout.masterTemplate>