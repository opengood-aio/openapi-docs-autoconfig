# OpenAPI Docs Auto Configuration

Spring Boot auto-configuration for OpenAPI documentation using
[Spring Doc](https://springdoc.org/)

## Setup

### Add Dependency

#### Gradle

```groovy
implementation("io.opengood.autoconfig:openapi-docs-autoconfig:VERSION")
```

#### Maven

```xml
<dependency>
    <groupId>io.opengood.autoconfig</groupId>
    <artifactId>openapi-docs-autoconfig</artifactId>
    <version>VERSION</version>
</dependency>
```

**Note:** See *Releases* for latest version(s).

## Configuration

*OpenAPI Docs Auto Configuration* simplifies configuration using a
YAML-based configuration approach.

### Properties

#### Main

| Property | Description | Default |
|---|---|---|
| `enabled` | Value indicating if OpenAPI Docs auto configuration is enabled | `true` |
| `path` | List of API resource paths to include in docs | `/**` |
| `title` | API title |  |
| `description` | API description |  |
| `version` | API version |  |
| `terms-of-service` | API terms of service description |  |
| `contact` | API contact details | see *Contact* |
| `license` | API license details | see *License* |
| `security` | API security details | see *Security* |

#### Contact

| Property | Description | Default |
|---|---|---|
| `name` | API contact name |  |
| `url` | API contact URL |  |
| `email` | API contact email address |  |

#### License

| Property | Description | Default |
|---|---|---|
| `name` | API license name |  |
| `url` | API license URL |  |

#### Security

| Property | Description | Default |
|---|---|---|
| `enabled` | Value indicating if OpenAPI Docs security is enabled | `true` |
| `name` | API security name | `default` |
| `description` | API security description |  |
| `scheme` | API security scheme. Supported values (`bearer`, `basic`) | `basic` |
| `type` | API security type. Supported values (`http`, `apikey`) | `http` |
| `bearerFormat` | API security token bearer format. Supported values (`JWT`). Only required when `scheme = bearer`. | `JWT` |
| `oauth2` | API security OAuth2 details | see *OAuth2* |

#### OAuth2

| Property | Description | Default |
|---|---|---|
| `grant-type` | OAuth2 grant type. Supported values (`authorizationCode`, `clientCredentials`) | `authorizationCode` |
| `token-uri` | OAuth2 token URI | `http://localhost/oauth/token` |
| `resource` | OAuth2 resource details | see *OAuth2 Resource* |
| `client` | OAuth2 client details | see *OAuth2 Client* |

#### OAuth2 Resource

| Property | Description | Default |
|---|---|---|
| `authorization-server-uri` | OAuth2 authorization server URI | `http://localhost/oauth/authorize` |

#### OAuth2 Client

| Property | Description | Default |
|---|---|---|
| `scopes` | Map of OAuth2 client scopes |  |

### Example

```yaml
open-api-docs:
  enabled: true
  paths:
    - /greeting/**
  title: test title
  description: test description
  version: test version
  terms-of-service: http://test.tos.url
  contact:
    name: test contact name
    url: http://test.contact.url
    email: test@domain.com
  license:
    name: test license name
    url: http://test.lic.url
  security:
    enabled: true
    name: test security
    description: test security description
    scheme: bearer
    type: http
    bearer-format: jwt
    oauth2:
      grant-type: clientCredentials
      resource:
        authorization-server-uri: http://localhost/oauth2/authorize
      client:
        scopes:
          test-1: test-scope-1
          test-2: test-scope-2
      token-uri: http://localhost/oauth2/token
```