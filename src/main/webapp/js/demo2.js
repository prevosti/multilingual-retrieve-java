$(document).ready(function() {
  var $searchResults = $('.search--results');

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

    $searchResults.text("search results!!!");
    $searchResults.show();

    var jsonQuery = JSON.parse(queryText);

    var request = $.ajax('api/query', {
      data : JSON.stringify(jsonQuery),
      contentType : 'application/json',
      type : 'POST'
    });  

    request.done(function(response) {
      //var tt = response.numSolrResults
      $searchResults.text("search results: " +JSON.stringify(response));
      $searchResults.show();
    });  

    request.fail(function(jqXHR, textStatus) {
      $searchResults.text("Status: "+textStatus+", Error getting the data: " + jqXHR.responseText);
      $searchResults.show();
    });  

     request.always(function() { });
  }
});

