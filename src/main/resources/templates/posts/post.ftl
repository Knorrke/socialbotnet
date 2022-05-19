<#ftl output_format="XML">
<#macro show post>
    <div id="post-${post.id?c}" class="media colored">
        <div class="media-body">
            <h4 class="media-heading">
                <a href="/user/profile/${post.user.username}">
                <img alt="Profilbild von ${post.user.username}" src="${post.user.image}" width="50" style="vertical-align:middle">
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
			<#assign likesShown=(post.likesCount < 5)?then(post.likesCount, 5)>
			<#if authenticatedUser??>
				<#if postsLikedByUser?map(p->p.getId())?seq_contains(post.getId())>
					<form method="POST" action="/unlike" style="margin-right:10px; display:inline;">
						<input type="hidden" name="post" value="${post.id?c}">
						<button class="button action-button colored" type="submit" title="Gefällt mir nicht mehr">
							<i class="icon-current fas fa-heart"></i>
							<i class="icon-action fas fa-heart-broken"></i>
							<span>${post.likesCount}</span>
						</button>
					</form>
				<#else>
					<form method="POST" action="/like" style="margin-right:10px; display:inline;">
						<input type="hidden" name="post" value="${post.id?c}">
						<button class="button action-button colored" type="submit" title="Gefällt mir">
							<i class="icon-current far fa-heart"></i>
							<i class="icon-action fas fa-heart"></i>
							<span>${post.likesCount}</span>
						</button>
					</form>
				</#if>
			<#else>
				<span style="margin-right: 10px; vertical-align: middle;"><i class="far fa-heart"></i><span>${post.likesCount}</span></span>
			</#if>
			<span class="likes">
				<#if post.likesCount gt 0>
					<#list post.getRecentLikes()[0..(likesShown-1)] as likingUser>
						 <a class="overlapping" data-tooltip="${likingUser.username}" href="/user/profile/${likingUser.username}"><img class="profile-pic small" alt="${likingUser.username} Like" src="${likingUser.image}" style="vertical-align:middle"></a>
					</#list>
				</#if>
				<#if post.likesCount gt likesShown> <span class="profile-pic small overlapping" data-tooltip="${post.likesCount - likesShown} weitere">...</span></#if>
			</span>
        </div>
    </div>
</#macro>
