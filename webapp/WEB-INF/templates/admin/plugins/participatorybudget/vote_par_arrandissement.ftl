<div class="row">
	<div class="col-xs-12 col-sm-12">
		 <div class="box box-danger">
      		<div class="box-header">
      		<@messages infos=infos />
       		<@messages errors=errors />
        	<h1 class="box-title">Votes par Arrandissement </h1>
	        <br><br>
       		<#if listVotes?? && listVotes?size gt 0 >
        	<form method="post"  action="jsp/admin/plugins/participatorybudget/VoteParArrandissement.jsp" >
		        <#list listVotes as vote >
			        <div class="form-group col-xs-9 col-sm-9 col-md-9">
					<label class="col-xs-9 col-sm-3 col-md-3" for="urlProjet">${vote.locationArd}</label>
					<div class="col-xs-9 col-sm-6 col-md-6">
						<input class="form-control" id="urlProjet" name="nb_${vote.id}" type="text" value="${vote.nbVotes}" maxlength="6" />
						<p class="help-block">${vote.locationArd} (help text)</p>
					</div>
					</div>
				</#list>
				<div class="form-group col-xs-9 col-sm-9 col-md-9">
					 <button class="btn btn-primary btn-small" type="submit" name="action_update" >
					     <i class="fa fa-gear"></i>&nbsp;Modifier
					 </button>
					 <button class="btn btn-secondary btn-small" type="button" onClick="javascript:history.go(-1);" >
					     Annuler
					</button>
				</div>
			</form>
			</#if>
		    </div>
		</div>     
	</div>
</div>