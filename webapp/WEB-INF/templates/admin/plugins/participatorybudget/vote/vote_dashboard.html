<#-- Random function, used for chart colors -->
<#assign rnd="0.${turnoverstr(.now?long?string)}"?number />

<#function random >
	<#local h = "0.${turnoverstr(.now?long?string)}" />
	<#local r = (h?number + rnd) />
	<#if (r gte 1)>
		<#local r = (r-1) />
	</#if>
	<#assign rnd=r />
	<#return r/>
</#function>

<#function turnoverstr str >
	<#local l = str?length />
	<#local r = ""/>
	<#list 1..l as i>
		<#local r = r+str?substring(l-i,l-i+1)/>
	</#list>
	<#return r/>
</#function>

<br/>

<div class="container">

	<@boxHeader title='' boxTools=true>
		<@tform method='post' action='jsp/admin/plugins/participatorybudget/vote/VoteDashboard.jsp?action=confirmGenerateRandomVoteData'>
			<@button type='submit' buttonIcon='plus' title='#i18n{participatorybudget.vote_dashboard.global.buttonGenerateRandomVoteData}' />
		</@tform>
	</@boxHeader>


	<@messages infos=infos warnings=warnings errors=errors />
		
	<#-- ********************************************************************************************* -->
	<#-- * GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBA * -->
	<#-- * GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBAL GLOBA * -->
	<#-- ********************************************************************************************* -->

	<@rowBoxHeader i18nTitleKey="participatorybudget.vote_dashboard.global.title">
		
		<div class="row">

			<div class="col-md-6">
				<canvas id="nbVotes_allCmp" aria-label="Number of votes of each campaign" role="img">
					<p>Number of votes of each campaign</p>
				</canvas>
			</div>
			
			<div class="col-md-6">
				<canvas id="nbVotesByDay_allCmp" aria-label="Number of votes by day for current campaign" role="img">
					<p>Number of votes by day for current campaign</p>
				</canvas>
			</div>
		
		</div>
	
	</@rowBoxHeader>
		
	<#-- ********************************************************************************************* -->
	<#-- * BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CA * -->
	<#-- * BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CA * -->
	<#-- ********************************************************************************************* -->
	
	<@rowBoxHeader i18nTitleKey="participatorybudget.vote_dashboard.by_campaign.title">

		<div class="row">
		
			<@tform name='select_campaign' action='jsp/admin/plugins/participatorybudget/vote/VoteDashboard.jsp?action=changeCampaign'>

				<div class="col-md-6">
					<@formGroup labelKey='#i18n{participatorybudget.vote_dashboard.select_campaign.labelTitle}' helpKey='#i18n{participatorybudget.vote_dashboard.select_campaign.labelTitle.help}' mandatory=true>
						<@select name='campaign_id' items=campaignCodeList default_value='${campaignId!}' sort=true />
					</@formGroup>
				</div>
			
				<div class="col-md-6">
					<@formGroup label>
						<@button type='submit' buttonIcon='check' title='#i18n{participatorybudget.vote_dashboard.select_campaign.buttonModify}'  size='' />
					</@formGroup>
				</div>
			                 
			</@tform>
		
		</div>
		
	</@rowBoxHeader>
	
	<@rowBoxHeader i18nTitleKey="participatorybudget.vote_dashboard.by_campaign.title">

		<div class="row">

			<div class="col-md-6">
				<canvas id="nbVotesByTheme_curCmp" aria-label="Number of votes by theme for choosen campaign" role="img">
					<p>Number of votes by theme for choosen campaign</p>
				</canvas>
			</div>
		
			<div class="col-md-6">
				<canvas id="nbVotesByLocation_curCmp" aria-label="Number of votes by location for choosen campaign" role="img">
					<p>Number of votes by location for choosen campaign</p>
				</canvas>
			</div>

		</div>
		
	</@rowBoxHeader>
	
	<@rowBoxHeader i18nTitleKey="participatorybudget.vote_dashboard.by_campaign.title">
	
		<div class="row">

			<div class="col-md-12">
			
		        <@table condensed=false hover=false striped=true>
		            
		            <@tr><@th>Project</@th><@th>Title</@th><@th>Nb votes</@th></@tr>
		            
		            <@tableHeadBodySeparator />
		            
					<#assign list = nbVotesByProjectList>
					<#list list as values>
						<@tr>
							<@td>${values["ref"]}</@td>
							<@td>${values["title"]}</@td>
							<@td>${values["nb_votes"]}</@td>
						</@tr>
					</#list>
		        </@table>
					
				
			</div>
		</div>
	
	</@rowBoxHeader>
	
</div>
		
<#-- ********************************************************************************************* -->
<#-- * JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS * -->
<#-- * JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS JS * -->
<#-- ********************************************************************************************* -->

<script src="js/plugins/participatorybudget/Chart.bundle.min.js"></script>

<script>

	<#-- +-------------------------------------------------------------------------------------------+ -->
	<#-- | nbVotes_allCmp nbVotes_allCmp nbVotes_allCmp nbVotes_allCmp nbVotes_allCmp nbVotes_allCmp | -->
	<#-- +-------------------------------------------------------------------------------------------+ -->

	<#assign map = nbVotesByCampaignCodeMap>
	
	var ctx = document.getElementById('nbVotes_allCmp').getContext('2d');
	var myChart = new Chart(ctx, {
	    type: 'bar',
	    data: {
	        labels: ['${map?keys?join("', '")}'],
	        datasets: [{
	            label: 'Nb of votes',
	            data: [
	            	<#assign str = ''>
	            	<#list map?keys as key>
	            		<#assign str = str + "'" + map[key] + "',">
	            	</#list>
	            	${str?keep_before_last(",")}
	            ],
            	<#assign color = "rgba(" + (random()*255)?round + ", " + (random()*255)?round + ", " + (random()*255)?round + ",">
	            backgroundColor: "${color} 0.5)",
				borderColor: "${color} 1)",
	            borderWidth: 3
	        }]
	    },
	    options: {
	        scales: {
	            yAxes: [{
	                ticks: {
	                    beginAtZero: true
	                }
	            }]
	        },
	        title: {
				display: true,
				text: 'Number of votes by campaign'
			}
	    }
	});
	
	<#-- +-------------------------------------------------------------------------------------------+ -->
	<#-- | nbVotesByDay_allCmp nbVotesByDay_allCmp nbVotesByDay_allCmp nbVotesByDay_allCmp nbVotesBy | -->
	<#-- +-------------------------------------------------------------------------------------------+ -->

	<#assign map = nbVotesByDateAllCampaignsMap>
	
	<#-- Calculate size of longer campaign -->
	<#assign nbDays = 0>
	<#list map?keys as key>
		<#if map[key]?size gt nbDays>
			<#assign nbDays = map[key]?size>
		</#if>
	</#list>
	
	var ctx = document.getElementById('nbVotesByDay_allCmp').getContext('2d');
	var myChart = new Chart(ctx, {
	    type: 'line',
	    data: {
	        labels: ['${(1..nbDays)?join("', '")}'],
	        datasets: [
            	<#assign str = ''>
		        <#list map?keys?sort as keyCmp>
			        <#assign mapCmp = map[keyCmp]>
		        	<#assign strData = ''>
			        <#list mapCmp?keys?sort as keyDate>
			        	<#assign strData = strData + "'" + mapCmp[keyDate?string] + "',">
	            	</#list>
	            	<#assign color = "rgba(" + (random()*255)?round + ", " + (random()*255)?round + ", " + (random()*255)?round + ", 1)">
	            	<#assign str = str + "{">
	            	<#assign str = str + "label: 'Campaign " + keyCmp + "',">
	            	<#assign str = str + "borderColor: \"" + color + "\",">
	            	<#assign str = str + "backgroundColor: \"" + color + "\",">
	            	<#assign str = str + "fill: false,">
	            	<#assign str = str + "data: [" + strData?keep_before_last(",") + "]">
	            	<#assign str = str + "},">
	            	
            	</#list>
            	${str?keep_before_last(",")}
            ]
	    },
	    options: {
	        scales: {
	            yAxes: [{
	                ticks: {
	                    beginAtZero: true
	                }
	            }]
	        },
	        title: {
				display: true,
				text: 'Number of votes by day number and by campaign'
			}
        }
	});
	
	<#-- +-------------------------------------------------------------------------------------------+ -->
	<#-- | nbVotesByTheme_curCmp nbVotesByTheme_curCmp nbVotesByTheme_curCmp nbVotesByTheme_curCmp n | -->
	<#-- +-------------------------------------------------------------------------------------------+ -->

	<#assign map = nbVotesByThemeMap>
	
	var ctx = document.getElementById('nbVotesByTheme_curCmp').getContext('2d');
	var myChart = new Chart(ctx, {
	    type: 'horizontalBar',
	    data: {
	        labels: ['${map?keys?join("', '")}'],
	        datasets: [{
	            label: 'Nb of votes',
	            data: [
	            	<#assign str = ''>
	            	<#list map?keys as key>
	            		<#assign str = str + "'" + map[key] + "',">
	            	</#list>
	            	${str?keep_before_last(",")}
	            ],
            	<#assign color = "rgba(" + (random()*255)?round + ", " + (random()*255)?round + ", " + (random()*255)?round + ",">
	            backgroundColor: "${color} 0.5)",
				borderColor: "${color} 1)",
	            borderWidth: 3
	        }]
	    },
	    options: {
	        scales: {
	            xAxes: [{
	                ticks: {
	                    beginAtZero: true
	                }
	            }]
	        },
	        title: {
				display: true,
				text: 'Number of votes by theme'
			}
	    }
	});
	
	<#-- +-------------------------------------------------------------------------------------------+ -->
	<#-- | nbVotesByLocation_curCmp nbVotesByLocation_curCmp nbVotesByLocation_curCmp nbVotesByLocat | -->
	<#-- +-------------------------------------------------------------------------------------------+ -->

	<#assign map = nbVotesByLocationMap>
	
	var ctx = document.getElementById('nbVotesByLocation_curCmp').getContext('2d');
	var myChart = new Chart(ctx, {
	    type: 'horizontalBar',
	    data: {
	        labels: ['${map?keys?join("', '")}'],
	        datasets: [{
	            label: 'Nb of votes',
	            data: [
	            	<#assign str = ''>
	            	<#list map?keys as key>
	            		<#assign str = str + "'" + map[key] + "',">
	            	</#list>
	            	${str?keep_before_last(",")}
	            ],
            	<#assign color = "rgba(" + (random()*255)?round + ", " + (random()*255)?round + ", " + (random()*255)?round + ",">
	            backgroundColor: "${color} 0.5)",
				borderColor: "${color} 1)",
	            borderWidth: 3
	        }]
	    },
	    options: {
	        scales: {
	            xAxes: [{
	                ticks: {
	                    beginAtZero: true
	                }
	            }]
	        },
	        title: {
				display: true,
				text: 'Number of votes by location'
			}
	    }
	});
	
	
</script>
		