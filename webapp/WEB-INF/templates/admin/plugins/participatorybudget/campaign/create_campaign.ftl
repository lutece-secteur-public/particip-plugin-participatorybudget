<#include "manage_campaign_tabs.ftl" />
<@tabs tab="campaign" />

<@rowBoxHeader i18nTitleKey="participatorybudget.create_campaign.pageTitle">
    <form class="form-horizontal" method="post" name="create_campaign" action="jsp/admin/plugins/participatorybudget/campaign/ManageCampaign.jsp">
        <@messages errors=errors />
        <input type="hidden" id="id" name="id"/>
        
        <@fieldInputText i18nLabelKey="participatorybudget.create_campaign.labelCode" inputName="code" mandatory=true value="${campaign.code!''}" i18nHelpBlockKey="participatorybudget.create_campaign.labelCode.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.create_campaign.labelTitle" inputName="title" mandatory=true value="${campaign.title!''}" i18nHelpBlockKey="participatorybudget.create_campaign.labelTitle.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.create_campaign.labelDescription" inputName="description" mandatory=true value="${campaign.description!''}" i18nHelpBlockKey="participatorybudget.create_campaign.labelDescription.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.create_campaign.labelActive" inputName="active" mandatory=true value="${campaign.active?c!''}" i18nHelpBlockKey="participatorybudget.create_campaign.labelActive.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.create_campaign.labelCodeModerationType" inputName="code_moderation_type" mandatory=true value="${campaign.codeModerationType!''}" i18nHelpBlockKey="participatorybudget.create_campaign.labelCodeModerationType.help" />
        <@fieldInputText i18nLabelKey="participatorybudget.create_campaign.labelModerationDuration" inputName="moderation_duration"  value="${campaign.moderationDuration!''}" i18nHelpBlockKey="participatorybudget.create_campaign.labelModerationDuration.help" />
        <@actionButtons button1Name="action_createCampaign" button2Name="view_manageCampaigns"/>
    </form>
</@rowBoxHeader>

