<#import "../layout/layout.ftl" as layout />

<@layout.masterTemplate title="Timeline">
    <#if user??>
    	<h2>${user.username}s Timeline</h2>
    <#else>    
    	<h2>&Ouml;ffentliche Timeline</h2>
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
	            <h3 class="panel-title">Was denkst du gerade, ${authenticatedUser.username}?</h3>
	        </div>
            <div class="panel-body">
                <form class="form-horizontal" action="/post" method="post">
                    <div class="input-group">
                        <input type="text" name="text" class="form-control" required/>
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
                            <a href="/timeline/${post.user.username}">
                            ${post.user.username}
                            </a>
                        </h4>
                    ${post.message} <br/>

                        <small>&mdash; ${post.publishingDate}</small>
                    </div>
                </div>
            <#else>
                <hr/>
                <div class="well">
                    There're no posts so far.
                </div>
            </#list>
        <#else>
            <hr/>
            <div class="well">
                There're no posts so far.
            </div>
        </#if>
	</div>
</@layout.masterTemplate>