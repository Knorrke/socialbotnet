<#ftl output_format="XML">
<#macro show post>
    <div id="post-${post.id?c}" class="media colored">
        <div class="media-body">
            <h4 class="media-heading">
                <a href="/user/profile/${post.user.username}">
                <img src="${post.user.image}" width="50" style="vertical-align:middle">
                ${post.user.username}
                </a>
                <#if post.wall.username != post.user.username>
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
					<a href="/unlike/${post.id?c}" style="margin-right:10px">Gef&auml;llt mir nicht mehr</a>
				<#else>
					<a href="/like/${post.id?c}" style="margin-right:10px">Gef&auml;llt mir</a>
				</#if>
			</#if>
			${likes}
        </div>
    </div>
</#macro>
