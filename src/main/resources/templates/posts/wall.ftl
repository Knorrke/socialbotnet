<#ftl output_format="XML">
<#import "./post.ftl" as postLayout />
<#import "../common/text-input.ftl" as textInput/>

<#assign filters>
	<div id="filters">
		<#if (sortby!"")=="trending">
			<span class="button colored selected"><i class="fas fa-fire"></i> Trending</span>
		<#else>
			<a class="button colored" href="?sortby=trending"><i class="fas fa-fire"></i> Trending</a>
		</#if>
		<#if (sortby!"")!="trending" && (sortby!"")!="likes">
			<span class="button colored selected"><i class="fas fa-history"></i> Neueste</span>
		<#else>
			<a class="button colored" href="?sortby=time"><i class="fas fa-history"></i> Neueste</a>
		</#if>
		<#if (sortby!"")=="likes">
			<span class="button colored selected"><i class="fas fa-crown"></i> Meiste Likes</span>
		<#else>
			<a class="button colored" href="?sortby=likes"><i class="fas fa-crown"></i> Meiste Likes</a>
		</#if>
	</div>
</#assign>
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
	<div class="media colored media-info">
        <div class="media-heading">
        	<#if user?? && user.username != authenticatedUser.username>
		    	<h3 class="media-title">Was m&ouml;chtest du ${user.username} erz&auml;hlen, ${authenticatedUser.username}?</h3>
        		<#assign action="/post/${user.username}">
        	<#else>    
		    	<h3 class="media-title">Was denkst du gerade, ${authenticatedUser.username}?</h3>
        		<#assign action="/post">
		    </#if>
        </div>
        <div class="media-body">
            <form class="form-horizontal" action="${action}" method="post">
                <div class="input-group">
                	<@textInput.show/>
                </div>
            </form>
        </div>
    </div>
</#if>
<div id="media-list" class="row">
	<#if !(trending?? & posts??)>
		${filters}
	</#if>
	<#if trending??>
		<h2><i class="fas fa-fire"></i> Top 3 Trends</h2>
		<#list trending as post>
			<@postLayout.show post=post />
		<#else>
			<hr/>
			<div class="well">
				Hier gibt es noch keine Posts in den Trends.
			</div>
		</#list>
	</#if>
	<#if trending?? & posts??>
		<hr/>
		${filters}
	</#if>
	<#if mostliked??>
		<h2><i class="fas fa-crown"></i> Meiste Likes</h2>
		<#list mostliked as post>
			<@postLayout.show post=post/>
		<#else>
			<hr/>
			<div class="well">
				Hier gibt es noch keine Posts mit Likes.
			</div>
		</#list>
	</#if>
    <#if posts??>
    	<h2><i class="fas fa-history"></i> Neueste Posts</h2>
        <#list posts as post>
        	<@postLayout.show post=post/>
        <#else>
            <hr/>
            <div class="well">
                Hier gibt es noch keine Posts.
            </div>
        </#list>
    </#if>
    <#if !(mostliked??) & !(posts??)>
    	<hr/>
        <div class="well">
			Hier gibt es noch keine Posts.
        </div>
    </#if>
</div>