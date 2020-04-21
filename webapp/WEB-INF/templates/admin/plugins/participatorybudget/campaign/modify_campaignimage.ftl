<#include "manage_campaign_tabs.ftl" />
<@tabs tab="campaignimage" />

<@rowBoxHeader i18nTitleKey="participatorybudget.modify_campaignimage.pageTitle">
    <form class="form-horizontal" method="post" name="modify_campaignimage" action="jsp/admin/plugins/participatorybudget/campaign/ManageCampaignImages.jsp">
        <@messages errors=errors />
        <input type="hidden" id="id" name="id" value="${campaignimage.id}"/>
        
        <@fieldInputText i18nLabelKey="participatorybudget.modify_campaignimage.labelCodeCampaign" inputName="code_campaign" mandatory=true value="${campaignimage.codeCampaign}" i18nHelpBlockKey="participatorybudget.modify_campaignimage.labelCodeCampaign.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.modify_campaignimage.labelFile" inputName="file" mandatory=true value="${campaignimage.file}" i18nHelpBlockKey="participatorybudget.modify_campaignimage.labelFile.help" />
        <@actionButtons button1Name="action_modifyCampaignImage" button2Name="view_manageCampaignImages"/>
    </form>
</@rowBoxHeader>

