# Retrieve and Rank
The IBM Watson [Retrieve and Rank][service_url] service helps users find the most relevant information for their queries by using a combination of search and machine learning algorithms to detect "signals" in the data. You load your data into the service, which is built on top of Apache Solr, and train a machine learning model. Then use the trained model to provide improved results to users.

Give it a try! Click the button below to fork into IBM DevOps Services and deploy your own copy of this application on Bluemix.  
[![Deploy to Bluemix](https://bluemix.net/deploy/button.png)](https://bluemix.net/deploy?repository=https://github.com/watson-developer-cloud/retrieve-and-rank-java)

View a [demo](http://retrieve-and-rank-demo.mybluemix.net/) of this app.

## How it works
This application uses publicly available test data called the [Cranfield collection](http://ir.dcs.gla.ac.uk/resources/test_collections/cran/). The collection contains abstracts of aerodynamics journal articles, a set of questions about aerodynamics, and labels to mark how relevant an article is to a question. Some questions are not used as training data, which means that you can use them to validate the performance of the trained ranker. This subset of questions are are used in the demo.

## Before you begin
Ensure that you have the following prerequisites before you start:

* A Bluemix account. If you don't have one, [sign up][sign_up]
* [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) version 1.7 or later
* [Eclipse IDE for Java EE Developers](https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/marsr)
* [Apache Maven](https://maven.apache.org/download.cgi), version 3.1 or later
* [Git](https://git-scm.com/downloads)
* [Websphere Liberty Profile server](https://developer.ibm.com/wasdev/downloads/liberty-profile-using-non-eclipse-environments/), if you want to run the app in your local environment


## Getting Started

1. Create a Bluemix Account

   [Sign up][sign_up] in Bluemix or use an existing account. Watson Services
   in Beta are free to use.

2. Download and install the [Cloud-foundry CLI][cloud_foundry] tool.

3. Edit the `manifest.yml` file and change the `<application-name>` to something unique.

  ```none
  applications:
  - services:
    - retrieve-and-rank-service
    name: <application-name>
    path: webApp.war
    memory: 512M
  ```

  The name you use determines your initial application URL, e.g.,
  `<application-name>.mybluemix.net`.

4. Connect to Bluemix in the command line tool.

  ```sh
  $ cf api https://api.ng.bluemix.net
  $ cf login -u <your-user-ID>
  ```

5. Create the Retrieve and Rank service in Bluemix.

  ```sh
  $ cf create-service retrieve_and_rank standard retrieve-and-rank-service
  ```

6. Download and install the [maven][maven] compiler.

7. Build the project.

   You need to use the Apache `maven` to build the war file.

  ```sh
  $ maven install
  ```

8. Push it live!

  ```sh
  $ cf push -p target/webApp.war
  ```

9. Train the service to use the Cranfield collection and train a ranker with the Cranfield data. See a tutorial in <a href="http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/retrieve-rank/get_start.shtml" target="_blank"> Getting started with the Retrieve and Rank service</a>. As you complete the tutorial, save this information:
  * Solr cluster ID: The unique identifier of the Apache Solr Cluster that you create.
  * Collection name: The name you give to the Solr collection when you create it.  
  * Ranker ID: The unique identifier of the ranker you create.


10. Use the values from the tutorial to specify environment variables in your app.  

  1. Navigate to the application dashboard in Bluemix.
  2. Click the Retrieve and Rank application you created earlier.
  3. Click **Environment Variables**.
  4. Click **USER-DEFINED**.
  5. Add the following three environment variables with the values that you copied from the tutorial:
      * `CLUSTER_ID`
      * `COLLECTION_NAME`
      * `RANKER_ID`

## Running locally

  The application uses the WebSphere Liberty profile runtime as its server,
  so you need to download and install the profile as part of the steps below.

1. Copy the credentials, `CLUSTER_ID`, `COLLECTION_NAME` and `RANKER_ID` from your `retrieve-and-rank-service` service in Bluemix to `RetrieveAndRankResource.java`.  
   You can use the following command to see the credentials:

    ```sh
    $ cf env <application-name>
    ```

   Example output:

    ```sh
    System-Provided:
    {
    "VCAP_SERVICES": {
      "retrieve-and-rank": [{
          "credentials": {
            "url": "<url>",
            "password": "<password>",
            "username": "<username>"
          },
        "label": "retrieve-and-rank",
        "name": "retrieve-and-rank-service",
        "plan": "standard"
     }]
    }
    }
    User-Provided:
    CLUSTER_ID: xxxxxxxx_ca0e_zzzz_zzzz_95zzz3aa2404
    COLLECTION_NAME: ga
    RANKER_ID: F131F6-rank-10
    ```

	You need to copy the `username`, `password`, and `url`,


2. Create a Liberty profile server in Eclipse.

3. Add the application to the server.

4. Start the server.

5. Go to `http://localhost:9080/webApp` to see the running application.


## Troubleshooting

  To troubleshoot your Bluemix application, the most useful source of
  information is the log files. To see them, run the following command:

  ```sh
  $ cf logs <application-name> --recent
  ```

## License

  This sample code is licensed under Apache 2.0.  
  Full license text is available in [LICENSE](LICENSE).

## Contributing

  See [CONTRIBUTING](CONTRIBUTING.md).


## Reference information
* Retrieve and Rank service [documentation](http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/retrieve-rank/)
* [Configuring](http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/retrieve-rank/configure.shtml) the Retrieve and Rank service
* Retrieve and Rank [API reference](http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/retrieve-and-rank/api/v1/)

## Open Source @ IBM

  Find more open source projects on the
  [IBM Github Page](http://ibm.github.io/).

[sign_up]: https://console.ng.bluemix.net/registration/
[cloud_foundry]: https://github.com/cloudfoundry/cli
[service_url]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/retrieve-and-rank.html
[sign_up]: https://console.ng.bluemix.net/registration/
[liberty]: https://developer.ibm.com/wasdev/downloads/
[liberty_mac]: http://www.stormacq.com/how-to-install-websphere-8-5-liberty-profile-on-mac/
[maven]: https://maven.apache.org/
