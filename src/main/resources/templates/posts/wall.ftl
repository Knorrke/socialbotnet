<#ftl output_format="XML">
<#import "./post.ftl" as postLayout />
<#import "../common/text-input.ftl" as textInput/>
<#import "../common/fa-icons.ftl" as fa/>

<#assign filters>
	<div id="filters">
		<#if (sortby!"")=="trending">
			<span class="button colored selected"><@fa.icon fa="fire" /> Trending</span>
		<#else>
			<a class="button colored" href="?sortby=trending"><@fa.icon fa="fire" /> Trending</a>
		</#if>
		<#if (sortby!"")!="trending" && (sortby!"")!="likes">
			<span class="button colored selected"><@fa.icon fa="history" /> Neueste</span>
		<#else>
			<a class="button colored" href="?sortby=time"><@fa.icon fa="history" /> Neueste</a>
		</#if>
		<#if (sortby!"")=="likes">
			<span class="button colored selected"><@fa.icon fa="crown" /> Meiste Likes</span>
		<#else>
			<a class="button colored" href="?sortby=likes"><@fa.icon fa="crown" /> Meiste Likes</a>
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
		    	<h3 class="media-title">Was möchtest du ${user.username} erzählen, ${authenticatedUser.username}?</h3>
        		<#assign action="/post/${user.username?url}">
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
	<#if !(trending?? & recent??)>
		${filters}
	</#if>
	<#if trending??>
		<h2><@fa.icon fa="fire" /> Top 3 Trends</h2>
		<#list trending as post>
			<@postLayout.show post=post />
		<#else>
			<hr/>
			<div class="well">
				Hier gibt es noch keine Posts in den Trends.
			</div>
		</#list>
	</#if>
	<#if trending?? & recent??>
		<hr/>
		${filters}
	</#if>
	<#if mostliked??>
		<h2><@fa.icon fa="crown" /> Meiste Likes</h2>
		<#list mostliked as post>
			<@postLayout.show post=post/>
		<#else>
			<hr/>
			<div class="well">
				Hier gibt es noch keine Posts mit Likes.
			</div>
		</#list>
	</#if>
    <#if recent??>
    	<h2><@fa.icon fa="history" /> Neueste Posts</h2>
        <#list recent as post>
        	<@postLayout.show post=post/>
        <#else>
            <hr/>
            <div class="well">
                Hier gibt es noch keine Posts.
            </div>
        </#list>
    </#if>
    <#if !(mostliked??) & !(recent??)>
    	<hr/>
        <div class="well">
			Hier gibt es noch keine Posts.
        </div>
    </#if>
</div>