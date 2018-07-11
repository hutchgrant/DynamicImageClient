# DynamicImageClient

This is a sample java REST endpoint for the Google App Engine Images API. This creates and deletes ServingUrls from a cloud storage bucket.

## Prerequisites

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Maven](https://maven.apache.org/download.cgi) (at least 3.5)
- [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud)
- [Google Cloud Tools for Eclipse](https://cloud.google.com/eclipse/docs/quickstart)

See the [Google App Engine standard environment documentation][ae-docs] for more
detailed instructions.

[ae-docs]: https://cloud.google.com/appengine/docs/java/

## Setup

    gcloud init
    gcloud auth application-default login

## Import App Engine Project into Eclipse

```
git clone https://github.com/hutchgrant/DynamicImageClient.git
```

- File -> Import -> General -> Projects from Folder or Archive

- Browse for the directory you cloned. e.g. /home/user/DynamicImages

- Edit Project Properties -> Project Facets then enable the following: - Enable App Engine Java Standard Environment - Enable Java 1.8 - Enable Dynamic Web Module

- Apply & Close

## Usage

Add your bucket name to src/main/java/com/hutchgrant/DynamicImages/Client.java

ln 19:

```java
final String bucket = "your-bucket-name";
```

### GET /

#### Request

| Parameters | Description                                                       |
| ---------- | ----------------------------------------------------------------- |
| name       | filename(with extension) of file on your bucket you want to serve |
| size       | size of the image(in pixels) resolution                           |
| crop       | true/false whether you want to crop the image                     |

e.g. http://your-project.appspot.com/?name=somefile-in-your-bucket.jpg&size=150&crop=true

#### Response

| Parameters | Description                                                        |
| ---------- | ------------------------------------------------------------------ |
| name       | filename(with extension) of file on your bucket you want to serve  |
| size       | size of the image(in pixels) you want it scaled to in the serveUrl |
| crop       | true/false whether you want to crop the image                      |
| url        | Generated ServingURL                                               |
| blobkey    | Unique key identifier for the file. Use this to delete file.       |

### Delete /

#### Request

| Parameters | Description                                      |
| ---------- | ------------------------------------------------ |
| key        | blobkey(string) of file you want to stop serving |

#### Response

| Parameters | Description  |
| ---------- | ------------ |
| status     | success/fail |

## Running locally in Eclipse

- Right Click Project -> Run As -> App Engine

Note: Some parts of the google app engine API won't work locally.

## Deploy from Eclipse to AppEngine

- Right Click Project -> Deploy to App Engine Standard

## Maven

### Running locally

    mvn appengine:devserver

### Deploying

    mvn appengine:update

## Testing

    mvn verify
