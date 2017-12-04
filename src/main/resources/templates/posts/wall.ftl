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
            <div class="media colored">
                <div class="media-body">
                    <h4 class="media-heading">
                        <img src="${post.user.imageAsBase64}" width="30px" style="vertical-align:middle">
                        <a href="/user/profile/${post.user.username}">
                        ${post.user.username}
                        </a>
                        <#if !user?? && post.wall.username != post.user.username>
                            an <a href="/user/profile/${post.wall.username}">
                        	${post.wall.username}
                        	</a> 
                        </#if>
                    </h4>
                	${post.message} <br/>
					<small>&mdash; ${post.publishingDate}</small>
					<br/>
					<br/>
					<#assign likedByAuthenticatedUser=false>
					<#assign likes>
						<#assign likesShown=3>
						<#list post.likedBy as likingUser>
							<#if likingUser?index lt likesShown>
								<#if likingUser?index ==0>Gef&auml;llt: <#else>,</#if>
								 <a href="/user/profile/${likingUser.username}">${likingUser.username}</a>
							</#if>
							<#assign likedByAuthenticatedUser = likedByAuthenticatedUser || authenticatedUser?? && likingUser.id == authenticatedUser.id>
						</#list>
						<#if post.likedBy?size gt likesShown> und ${post.likedBy?size - likesShown} weiteren.</#if>
					</#assign>
					<#if authenticatedUser??>
						<#if likedByAuthenticatedUser>
							<a href="/unlike/${post.id}" style="margin-right:10px">Gef&auml;llt mir nicht mehr</a>
						<#else>
							<a href="/like/${post.id}" style="margin-right:10px">Gef&auml;llt mir</a>
						</#if>
					</#if>
					${likes}
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