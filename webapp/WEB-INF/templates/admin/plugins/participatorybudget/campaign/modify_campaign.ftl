<#include "manage_campaign_tabs.ftl" />
<@tabs tab="campaign" />

<@rowBoxHeader i18nTitleKey="participatorybudget.modify_campaign.pageTitle">
    <form class="form-horizontal" method="post" name="modify_campaign" action="jsp/admin/plugins/participatorybudget/campaign/ManageCampaign.jsp">
        <@messages errors=errors />
        <input type="hidden" id="id" name="id" value="${campaign.id}"/>
        
        <@fieldInputText i18nLabelKey="participatorybudget.modify_campaign.labelCode" inputName="code" mandatory=true value="${campaign.code}" i18nHelpBlockKey="participatorybudget.modify_campaign.labelCode.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.modify_campaign.labelTitle" inputName="title" mandatory=true value="${campaign.title}" i18nHelpBlockKey="participatorybudget.modify_campaign.labelTitle.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.modify_campaign.labelDescription" inputName="description" mandatory=true value="${campaign.description}" i18nHelpBlockKey="participatorybudget.modify_campaign.labelDescription.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.modify_campaign.labelActive" inputName="active" mandatory=true value="${campaign.active?c}" i18nHelpBlockKey="participatory.modify_campaign.labelActive.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.modify_campaign.labelCodeModerationType" inputName="code_moderation_type" mandatory=true value="${campaign.codeModerationType}" i18nHelpBlockKey="participatorybudget.modify_campaign.labelCodeModerationType.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.modify_campaign.labelModerationDuration" inputName="moderation_duration"  value="${campaign.moderationDuration}" i18nHelpBlockKey="participatorybudget.modify_campaign.labelModerationDuration.help" />
        <@actionButtons button1Name="action_modifyCampaign" button2Name="view_manageCampaigns"/>
    </form>
</@rowBoxHeader>

