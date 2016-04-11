$(document).ready(function() {
  var $searchResultsHeader = $('.search--results--header');

  $('.input--search-button').click(function() {
    runQuery();
  });

  $("#user-query").keyup(function (e) {
    if (e.keyCode == 13) {
        runQuery();
    }
  });

  function runQuery() { 
    console.log( "the search button was pressed!" );

    var queryText = "{\"query\":\"" + $('#user-query').val() + "\"}";
    console.log( "this is query that will be executed: " + queryText );

    var jsonQuery = JSON.parse(queryText);

    var request = $.ajax('api/query', {
      data : JSON.stringify(jsonQuery),
      contentType : 'application/json',
      type : 'POST'
    });

    request.done(function(response) {
      //var json = '{"numSolrResults":2,"solrResults":[{"body":"body","id":"0.8573195405934366","title":"On-fairy-tales.txt"},{"body":"body","id":"0.2811797388754165","title":"hamlet.txt"}]}';
      var responseJsonObject = JSON.parse(JSON.stringify(response));
      //$searchResultsHeader.text(" search results: " + JSON.stringify(response));

      var numDocs = responseJsonObject.numSolrResults
      $searchResultsHeader.text(numDocs + " search results found");
      $searchResultsHeader.append("<p/>");

      for (var i = 1; i <= responseJsonObject.solrResults.length; i++) {
          var result = responseJsonObject.solrResults[i-1];
          $searchResultsHeader.append("<p/>");
          $searchResultsHeader.append("<b><div class=\"search--result--title\">" + i + ". " + result.title + "</div></b>");
          $searchResultsHeader.append("<div class=\"search--result--body\">" + result.body + "</div>");
          $searchResultsHeader.append("<p/>");
      }

      $searchResultsHeader.show();
    });

    request.fail(function(jqXHR, textStatus) {
      $searchResultsHeader.text("Status: " + textStatus + ", Error getting the search results: " + jqXHR.responseText);
      $searchResultsHeader.show();
    });

     request.always(function() {} );
  }
});

