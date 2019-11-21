function loadVotes ( )
{
	$.ajax({
		url : baseUrl + "jsp/site/plugins/participatorybudget/LoadVotes.jsp",
		type: 'GET',
		dataType: "json",
		data: {},
		async: false,
		cache:false,
	    success:function(data) {
	    	if(data == "closed")
    		{
	    		alert("Phase de vote close");
    		}
	    	else
	    	{
				var html = data.result.split("||");
	      		
	      		$("#votes_tout_paris").html(html[0]);
	      		$("#votes_user_arr").html(html[1]);
				//#valid_vote no longer exists
	      		
	      		// modalChooseArr not used
	      		
	      		var nbProjectParis = $("#votes_tout_paris .vote_project").size();
	      		var nbProjectArr = $("#votes_user_arr .vote_project").size();
	      		var index = 0;
				var square;
				
	      		for (i = 1; i <= 4; i++) { 
					index = i - 1;
					square = $("#progress_paris").find("div:eq(" + index + ")");
	      		    if(i <= nbProjectParis){
						square.css("background-color","#FFFFFF");
	      		    }else{
	      		    	square.css("background-color","#65C85F");
	      		    }
					square = $("#progress_arr").find("div:eq(" + index + ")");
	      		    if(i <= nbProjectArr){
	      		    	square.css("background-color","#FFFFFF");
	      		    }else{
	      		    	square.css("background-color","#65C85F");
	      		    }
	      		}
	    	}
	    },
		error: function(jqXHR, textStatus, errorThrown) {
			/*alert("Echec");*/
		}
	});
}

/* Phase vote modal - Displayed only once a day !*/
$(document).ready(function() {
	/* To debug, add "$debugModal" on URL : http://localhost:8080/bp2016/?debugModal */
	loadVotes();
	if( ! $('#mesinfos-modal').hasClass('in') ) {		
		if ( ( ( Cookies.get('vote_popin') != 'shown') && Cookies.get('cookie-cnil-parisfr') == 'true' ) || (window.location.href.indexOf("debugModal") >= 0) ) {
			$('#modalPhaseVote').modal('show');
			Cookies.set('vote_popin', 'shown', { expires: 1 });
		}
	}
});

$('#modalPhaseVote').on('hidden.bs.modal', function () {
	/* set expiration to 1 years which should be enough */
    if($('#modalVoteNoDisplay').prop('checked')) {
		Cookies.set('vote_popin', 'shown', { expires: 365 });
	}
});
