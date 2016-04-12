/**
 * Copyright 2015 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.watson.developer_cloud.retrieve_and_rank.v1.utils;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Ranker;

/**
 * Utility class to search in for documents Solr using or not a {@link Ranker}
 */
public class SolrUtils {

  private static Logger logger = Logger.getLogger(SolrUtils.class.getName());
  private HttpSolrClient solrClient;

  public SolrUtils(HttpSolrClient solrClient) {
    this.solrClient = solrClient;
  }

  public QueryResponse searchTerm(String term, String collectionName) throws IOException {

    String query = "body:\"" + term + "\"";

    logger.info("Searching for [" + query + "]");
    final SolrQuery solrQuery = new SolrQuery(query);
    try {
      solrQuery.addHighlightField("body");
      solrQuery.add("hl.fragsize", "300");
      solrQuery.add("fl", "id,title");
      final QueryResponse response = solrClient.query(collectionName, solrQuery);
      logger.info("Found " + response.getResults().size() + " documents!");
      logger.info(response.getResults().toString());
      logger.info(response.getHighlighting().toString());
      logger.info(response.toString());
      return response;
    } catch (final SolrServerException e) {
      throw new RuntimeException("Failed to search!", e);
    }
  }

}
