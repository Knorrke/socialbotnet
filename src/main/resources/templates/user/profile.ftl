<#ftl output_format="XML">
<#import "../layout/layout.ftl" as layout />
<#assign pageTitle>
<div><img class="profile-pic" src="${user.image}" alt="Profilbild von ${user.username}"></div>
<div>${user.username}s Profil</div>
</#assign>

<@layout.masterTemplate title=user.username+"s Profil" pageTitle=pageTitle colored=false>
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
    <div id="side-content">
    <div id="media-list" class="row">
    	<div class="media colored">
            <div class="media-body">
                <h3 class="media-heading">
                    Über mich
                </h3>
                <#if user.about!="">
                	${user.about}
                <#else>
                	Leider hat ${user.username} noch keine Informationen über sich angegeben.
                </#if>
            </div>
        </div>
    	<div class="media colored">
            <div class="media-body">
                <h3 class="media-heading">
                    Hobbies
                </h3>
                <#if user.hobbies != "">
                	${user.hobbies}
                <#else>
					Leider hat ${user.username} noch keine Hobbies angegeben
                </#if>
            </div>
        </div>
        <#if authenticatedUser?? && authenticatedUser.id == user.id>
        	<div class="media colored">
        		<a href="/user/update">Profil bearbeiten</a>
        	</div>
        </#if>
    </div>
    </div>
    <div class="has-side-content">    
 	   <#include "../posts/wall.ftl">
    </div>
</@layout.masterTemplate>