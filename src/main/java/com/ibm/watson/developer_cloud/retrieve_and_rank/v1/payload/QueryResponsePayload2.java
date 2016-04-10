package com.ibm.watson.developer_cloud.retrieve_and_rank.v1.payload;


import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.RankResult;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrResult;

import java.util.List;


/**
 * The query response that contains:<br>
 * <ul>
 * <li>The original query sent from the client</li>
 * <li>The number of results which the Solr portion of the service returned</li>
 * <li>The list of results as returned by Solr</li>
 * <li>The list of results from Solr re-ranked using the ranker portion of the service.</li>
 * </ul>
 */
public class QueryResponsePayload2 {

  private int num_solr_results;

  private List<SolrResult> solr_results;

  public QueryResponsePayload2() {

  }


  /**
   * Returns the number of results returned by the Solr search
   */
  public int getNumSolrResults() {
    return num_solr_results;
  }

  /**
   * Returns an ordered list of results as they were returned by the Solr search.
   */
  public List<SolrResult> getSolrResults() {
    return solr_results;

  }

  /**
   * Set the number of results returned by Solr
   * 
   * @param numSolrResults
   */
  public void setNumSolrResults(int numSolrResults) {
    this.num_solr_results = numSolrResults;

  }

  /**
   * Sets an ordered list of results as they were returned by Solr
   * 
   * @param solrResults
   */
  public void setSolrResults(List<SolrResult> solrResults) {
    this.solr_results = solrResults;
  }
}
