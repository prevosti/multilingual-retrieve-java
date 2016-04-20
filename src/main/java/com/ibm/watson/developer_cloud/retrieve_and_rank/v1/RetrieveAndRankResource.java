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
package com.ibm.watson.developer_cloud.retrieve_and_rank.v1;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.payload.QueryRequestPayload2;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.payload.QueryResponsePayload2;
import org.apache.commons.codec.binary.Base64;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrResult;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.utils.HttpSolrClientUtils;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.utils.SolrUtils;
import com.ibm.watson.developer_cloud.service.ServiceResponseException;
import com.ibm.watson.developer_cloud.util.CredentialUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 * Sample application for using the IBM Watson Retrieve and Rank V1 service.
 */

@Path("/")
public class RetrieveAndRankResource {

  private static final String clusterId = "YOUR_CLUSTER_ID_HERE";
  private static String username = "YOUR_USERNAME_HERE";
  private static String password = "YOUR_PASSWORD_HERE";

  private static final String BASIC = "Basic ";
  private static final String RETRIEVE_AND_RANK = "retrieve_and_rank";
  private static Logger logger = Logger.getLogger(RetrieveAndRankResource.class.getName());

  private RetrieveAndRank service;
  private SolrUtils solrUtils;

  /**
   * Instantiates a new retrieve and rank resource.
   *
   * @throws FileNotFoundException ground truth file not found
   */
  public RetrieveAndRankResource() {
    // When running locally you need to provide values for the variables below
    // WHen running in Bluemix the values will be provided in the environment variables
    //String clusterId = System.getenv("CLUSTER_ID");

    logger.info("CLUSTER_ID:" + clusterId);

    String endPoint = "https://gateway.watsonplatform.net/retrieve-and-rank/api";

    // Get username and password from the VCAP_SERVICES environment variable in Bluemix
    String apiKey = CredentialUtils.getAPIKey(RETRIEVE_AND_RANK);
    if (apiKey != null) {
      // credentials = username:password
      String[] credentials = new String(Base64.decodeBase64(apiKey.replaceAll(BASIC,""))).split(":");
      username = credentials[0];
      password = credentials[1];
    }

    // Service instance
    this.service = new RetrieveAndRank();
    service.setUsernameAndPassword(username, password);

    // Solr Client
    HttpSolrClient solrClient = new HttpSolrClient(service.getSolrUrl(clusterId),
        HttpSolrClientUtils.createHttpClient(endPoint, username, password));

    solrUtils = new SolrUtils(solrClient);

    if (clusterId == null)
      logger.warning("CLUSTER_ID cannot be null");

    logger.info("RetrieveAndRank service initialized");
  }

  /**
   * Ping.
   *
   * @return the string
   */
  @GET
  @Path("/ping")
  public String ping() {
    return "pong";
  }

  @POST
  @Path("/query")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response query(QueryRequestPayload2 payload) {
    try {
      logger.info("We are in query");
      String query = payload.getQuery();
      String collectionName = payload.getCollectionName();
      QueryResponse response = solrUtils.searchTerms(query, collectionName);

//      String resultJson = "{\"title\":\"some title\", \"body\":\"some body\"}";

      SolrDocumentList results = response.getResults();
      Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
      long numDocs = results.getNumFound();
      QueryResponsePayload2 queryResponse = new QueryResponsePayload2();
      queryResponse.setNumSolrResults((int) numDocs);
      List<SolrResult> solrResults = new ArrayList<>();

      for(SolrDocument solrDocument: results) {
        SolrResult solrResult = new SolrResult();
        String id = (String) solrDocument.getFieldValue("id");
        solrResult.setId(id);
        solrResult.setTitle((String) solrDocument.getFieldValue("title"));
        List<String> bodyContents = highlighting.get(id).get("body");
        if(bodyContents != null && !bodyContents.isEmpty()) {
          String body = highlighting.get(id).get("body").get(0);
          solrResult.setBody(body);
        }
        solrResults.add(solrResult);
        queryResponse.setSolrResults(solrResults);
      }

      logger.info("about to return queryResponse");
      return Response.ok(queryResponse).build();
    } catch (ServiceResponseException e) {
      logger.info("ServiceResponseException:\n" + e);
      return Response.status(e.getStatusCode())
              .entity(createError(e.getStatusCode(),e.getMessage()))
              .build();
     } catch (Exception e) {
      logger.info("Generic Exception:\n" + e);
      return Response.status(500)
              .entity(createError(500, "Internal Server Error. Did you create and index the collection?\n" + e))
              .build();
    }
  }

  /**
   * Creates the JSON error based on the service exception.
   *
   * @param statusCode
   * @param message
   * @return the JSON error as string
     */
  private String createError(int statusCode, String message) {
    JsonObject error = new JsonObject();
    error.addProperty("code", statusCode);
    error.addProperty("error", message);
    return error.toString();
  }

}