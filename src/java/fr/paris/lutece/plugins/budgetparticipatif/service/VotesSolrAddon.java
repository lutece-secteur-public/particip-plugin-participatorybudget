package fr.paris.lutece.plugins.budgetparticipatif.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.search.solr.business.SolrSearchResult;
import fr.paris.lutece.plugins.search.solr.service.ISolrSearchAppAddOn;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portal.PortalMenuService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class VotesSolrAddon implements ISolrSearchAppAddOn {

    private static final String MARK_RESULTS_LIST = "results_list";
    private static final String MARK_RESULTS_IDEES_MAP = "results_idee_map";
    private static final String PARAMETER_PREV_ITEMS_PER_PAGE = "prev_items_per_page";
    private static final String MARK_PREV_ITEMS_PER_PAGE = "prev_items_per_page";

    @Override
    public void buildPageAddOn(Map<String, Object> model, HttpServletRequest request) {
        Map<String, Object> mapAdditionalInfos =  new HashMap<String, Object>();
        List<SolrSearchResult> listResults =  (List<SolrSearchResult>) model.get(MARK_RESULTS_LIST);

        IDocumentBodyService documentBodyService = SpringContextService.getBean(DocumentBodyService.BEAN_NAME);

        for (SolrSearchResult solrSearchResult: listResults) {
            String solrDocPortletId = solrSearchResult.getDocPortletId();
            String parsedSolrDocPortletId[] = solrDocPortletId.split("&");
            AppLogService.debug("budgetparticipatif, fetching " +  solrDocPortletId);
            if (parsedSolrDocPortletId.length == 2) {
                try {
                    mapAdditionalInfos.put(solrSearchResult.getId(),
                            documentBodyService.getPage(request,
                                    parsedSolrDocPortletId[0],
                                    parsedSolrDocPortletId[1],
                                    PortalMenuService.MODE_NORMAL));
                } catch (UserNotSignedException e) {
                    AppLogService.error("budgetparticipatif, SolrVoteAddon, got exception " +e, e);
                } catch (SiteMessageException e) {
                    AppLogService.error("budgetparticipatif, SolrVoteAddon, got exception " +e, e);
                }
            } else {
                AppLogService.error("budgetparticipatif, VotesSolrSaddon, Error parsing DocPortletId " + solrDocPortletId);
            }
        }
        model.put(MARK_RESULTS_IDEES_MAP, mapAdditionalInfos);

        model.put(MARK_PREV_ITEMS_PER_PAGE, request.getParameter(PARAMETER_PREV_ITEMS_PER_PAGE));
    }
}
