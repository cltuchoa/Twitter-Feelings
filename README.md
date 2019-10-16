# Twitter Fellings App

This app uses the twitter api (https://developer.twitter.com/en/docs) to show the last tweets from specific user. 
Furthermore, we use the Cloud Natural Language API from google to analyze the sentiment from each tweet based on return from this API.
This API return a score of the sentiment with ranges between -1.0 (negative) and 1.0 (positive) that corresponds to the overall emotional leaning of the text and the magnitude indicating the overall strength of emotion (both positive and negative) within the given text, between 0.0 and +inf (https://cloud.google.com/natural-language/docs/basics). We are using only score in our Sentiment Analyzer module, and we use this values to classify the sentiment in the sentence:

[-1.0 .. -0.7] - Negative
[-0.8 ..  0.3] - Neutral
[ 4.0 ..  1.0] - Positive

# Features

We divide the app in two main fragment:

1. Home Tweet Fragment: Responsible to call the twitter API to get data from specific user name.
2. List Tweet Fragment: Responsible to display the tweets from an user. In this fragment you can:
- See the user banner image profile
- See the last teets from an user;
- Click in all links from the tweet to see the content.
- See the sentiment based from each tweet by clicking in the image profile
- Pull to refresh data

# Technical solution

- Androidx library
- Navigation component fragment 
- MVVM architecture
- Retrofit to consume API
- Observer patter using RxKotlin
- Shared WiewModel patter
- Glide to download images
