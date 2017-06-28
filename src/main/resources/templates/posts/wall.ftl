<#import "../layout/layout.ftl" as layout />

<@layout.masterTemplate title="Pinnwand">
    <#if user??>
    	<h2>${user.username}s Pinnwand</h2>
    <#else>    
    	<h2>&Ouml;ffentliche Pinnwand</h2>
    </#if>
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
	<#if authenticatedUser??>
    	<div class="panel panel-info">
	        <div class="panel-heading">
	        	<#if user?? && user.username != authenticatedUser.username>
			    	<h3 class="panel-title">Was möchtest du ${user.username} erzählen, ${authenticatedUser.username}?</h3>
            		<#assign action="/post/${user.username}">
	        	<#else>    
			    	<h3 class="panel-title">Was denkst du gerade, ${authenticatedUser.username}?</h3>
            		<#assign action="/post">
			    </#if>
	        </div>
            <div class="panel-body">
                <form class="form-horizontal" action="${action}" method="post">
                    <div class="input-group">
                        <input type="text" name="message" class="form-control" required/>
                        <span class="input-group-btn">
                            <button class="btn btn-primary" type="submit"> Teilen </button>
                        </span>
                    </div>
                </form>
            </div>
        </div>
	</#if>
	<div id="media-list" class="row">
        <#if posts??>
            <#list posts as post>
                <hr/>
                <div class="media">
                    <div class="media-body">
                        <h4 class="media-heading">
                            <a href="/pinnwand/${post.user.username}">
                            ${post.user.username}
                            </a>
                            <#if !user?? && post.wall.username != post.user.username>
	                            an <a href="/pinnwand/${post.wall.username}">
                            	${post.wall.username}
                            	</a> 
                            </#if>
                        </h4>
                    	${post.message} <br/>
						<small>&mdash; ${post.publishingDate}</small>
                    </div>
                </div>
            <#else>
                <hr/>
                <div class="well">
                    Hier gibt es noch keine Posts.
                </div>
            </#list>
        <#else>
            <hr/>
            <div class="well">
				Hier gibt es noch keine Posts.
            </div>
        </#if>
	</div>
</@layout.masterTemplate>