<div class="row">
	<div class="col-xs-12 col-sm-12">
		 <div class="box box-danger">
      		<div class="box-header">
	      		<@messages infos=infos />
	       		<@messages errors=errors />
	        	<h1 class="box-title">Statistiques</h1>
		        <br><br>
		        
		        <h2>Exports demandés</h2>
		        
		        <div class="label label-warning" style="font-size:100%!important;">Les exports ne sont consultables que durant la journée courante. Ils sont automatiquement purgés le lendemain.</div>

	        	<form method="post" name="doDownloadCsv" action="jsp/admin/plugins/participatorybudget/bizstat/BizStat.jsp" >

		       		<#if files??>
		       			<table class="table table-hover table-condensed hover" id="">
		       				<tbody>
								<#-- Header -->
		       					<tr>
		       						<th                  >                </th>
		       						<th class="hidden-xs">Id              </th>
		       						<th class="hidden-xs">Statut          </th>
		       						<th                  >Nom             </th>
		       						<th class="hidden-xs">Description     </th>
		       						<th class="hidden-xs">Date de création</th>
		       						<th class="hidden-xs">Erreur          </th>
		       						<th class="hidden-xs">Taille          </th>
		       					</tr>
	
								<#-- Records -->
				       			<#list files as file>
									<#if file.status != "purged">
									
										<#assign style = ''>
										<#switch file.status>
											<#case 'available'>           <#assign style = 'color:#0B0;font-weight:bold'><#break>
											<#case '(being processed...)'><#assign style = 'color:blue' ><#break>
											<#case 'error'>               <#assign style = 'color:red'  ><#break>
										</#switch>
									
				       					<tr>
				       						<td                  ><#if file.status = 'available'><input type="radio" name="export_id" value="${file.idBizStatFile!'-1'}"></input></#if></th>
				       						<td class="hidden-xs">                         ${file.idBizStatFile!'?'}                </td>
				       						<td class="hidden-xs" style="${style!''}">     ${file.status!'?'}                       </td>
				       						<td>                                           ${file.fileName!'?'}                     </td>
				       						<td>                                           ${file.description!'?'}                  </td>
				       						<td class="hidden-xs">                         ${file.creationDate!'?'  }               </td>
				       						<td class="hidden-xs">                         <span title="${file.error!''}"><#if file.error?has_content>MSG (${(file.error[0..9])!''}…)</#if></span></td>
				       						<td class="hidden-xs" style="text-align:right">${file.contentSize?string(",##0")!''}    </td>
				       					</tr>
									</#if>
				       			</#list>			       			
		       				</tbody>
			       		</table>

						<div class="form-group">
							 <button class="btn btn-primary btn-small" type="submit" name="action_doDownloadCsv" >
							     <i class="fa fa-gear"></i>&nbsp;Récupérer le fichier sélectionné
							 </button>
						</div>

		       		</#if>

				</form>
				
				<br>

		        <h2>Demander un nouvel export</h2>
			        
	        	<form method="post" name="doExportCsv" action="jsp/admin/plugins/participatorybudget/bizstat/BizStat.jsp" >

		       		<#if methods?? && methods?size gt 0 >
		        	
						<div class="form-group">
					        <#list methods as method >
					        	<#if method.name?starts_with("export_") >
					        	
					        		<#assign timeConsuming = false>
					        		
					        		<#list method.annotations as annotation>
					        		
					        			<#if annotation.annotationType().getSimpleName() = 'TimeConsuming'>
							        		<#assign timeConsuming = true>
					        		
					        			<#elseif annotation.annotationType().getSimpleName() = 'BizStatDescription'>
							        		<#assign description = annotation.value()>
	
					        			</#if>
					        		</#list>
	
									<#if timeConsuming> 
										<input type="radio" name="method_name" value="${method.name!'?'}"><span style="color:red">${description!'?'} - /!\ Exécution lourde pour le site !</span></input>
							        <#else>             
							        	<input type="radio" name="method_name" value="${method.name!'?'}">${description!'?'}</input>
							        </#if>
							        <br/>
					        		
					        	</#if>
					        </#list>
						</div>
	
						<div class="form-group">
							<label for="reason">Raison * : </label>
							<input class="form-control" id="reason" name="reason" type="text" maxlength="255" />
						</div>
	
						<div class="form-group">
							 <button class="btn btn-primary btn-small" type="submit" name="action_doExportCsv" >
							     <i class="fa fa-gear"></i>&nbsp;Demander l'export
							 </button>
						</div>
	
					</#if>

				</form>

		    </div>
		</div>     
	</div>
</div>