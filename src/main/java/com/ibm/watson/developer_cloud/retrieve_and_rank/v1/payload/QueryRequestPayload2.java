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
package com.ibm.watson.developer_cloud.retrieve_and_rank.v1.payload;

/**
 * A payload object used to describe an incoming query from the client.
 */
public class QueryRequestPayload2 {
  
  private String query;

  public QueryRequestPayload2() {

  }

  /**
   * Returns the query which is to be sent to the WDS service.
   *
   * @return the query
   */
  public String getQuery() {
    return query;
  }

  /**
   * Sets the query.
   *
   * @param query the new query
   */
  public void setQuery(String query) {
    this.query = query;
  }

}
