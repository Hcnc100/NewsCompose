# NewsCompose
Simple app that shows a series of news, obtained from an api and saved in an internal database


### Instruccions

1. SigUp in https://newsapi.org/register
2. Get yout api key in https://newsapi.org/account
3. Put yout Api key in local.properties (this file no is tracking for git), remeber this name.

  apiKeyNews="yout_api_key_xxx_Xxx"

4.Set this code in your *build.gradle* in defaultConfig 

```kotlin
def localProperties = new Properties()
        localProperties.load(new FileInputStream(rootProject.file("local.properties")))
        buildConfigField "String", "API_KEY_NEWS",localProperties['apiKeyNews']

```
where "API_KEY_NEWS" is the name with your reference in inner code, and *apiKeyNews* is the name in the step 3
your can use this api key in yout code as 

```kotlin
BuildConfig.API_KEY_NEWS
```

### Change source

You can change country code for request in

```
newsRepo.concatenateNews("mx", numberPager)
```

in NewsViewModel. "mx" is from mexico.

You cant see another code contry in https://newsapi.org/docs/endpoints/top-headlines in *country*


### Screenshots
