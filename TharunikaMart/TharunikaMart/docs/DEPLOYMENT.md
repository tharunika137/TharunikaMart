# Public deployment with GitHub + Azure

This project is a Java Servlet/Tomcat application, so GitHub Pages alone cannot host it. The correct GitHub-based flow is:

- GitHub stores the source code
- GitHub Actions builds the WAR file
- Azure App Service deploys the WAR to a public URL

## Required GitHub secrets

Add these repository secrets in GitHub:

- `AZURE_WEBAPP_NAME`
- `AZURE_WEBAPP_PUBLISH_PROFILE`

## Create the Azure App Service

1. Create a Linux or Windows App Service in Azure.
2. Set the runtime to Java 17 and Tomcat.
3. Copy the publish profile from Azure.
4. Add it as the `AZURE_WEBAPP_PUBLISH_PROFILE` secret.
5. Add the app name as `AZURE_WEBAPP_NAME`.

## Push to deploy

Once the repository is pushed to GitHub and the secrets are added, any push to `master` triggers the workflow automatically.

## Build command used

```bash
mvn -B clean package -DskipTests
```

The generated artifact is:

```text
target/tharunikamart.war
```

## Notes

This app currently uses embedded H2 storage, which is okay for local and demo use but not ideal for a production public deployment. For a production public site, replace H2 with PostgreSQL or MySQL.
