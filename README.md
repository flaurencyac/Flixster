Flixster readme
# Project 2 - *Flixster*

**Flixster** shows the latest movies currently playing in theaters. The app utilizes the Movie Database API to display images and basic information about these movies to the user.

Time spent: **18** hours spent in total over 3 days

## User Stories

The following **required** functionality is completed:

* [x] User can **scroll through current movies** from the Movie Database API
* [x] Display a nice default [placeholder graphic](https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library#advanced-usage) for each image during loading
* [x] For each movie displayed, user can see the following details:
  * [x] Title, Poster Image, Overview (Portrait mode)
  * [x] Title, Backdrop Image, Overview (Landscape mode)
* [x] Allow user to view details of the movie including ratings within a separate activity

The following **stretch** features are implemented:

* [x] Improved the user interface by experimenting with styling and coloring.
* [x] Apply rounded corners for the poster or background images using [Glide transformations](https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library#transformations)
* [x] Apply the popular [View Binding annotation library](http://guides.codepath.org/android/Reducing-View-Boilerplate-with-ViewBinding) to reduce boilerplate code.

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!
* [x] Overlay of play vector on top of poster image in Movie Details screen to indicate click-to-play
* [x] Enabled landscape viewing of Movie Details page and YouTubePlayerView
* [x] Allow video trailers to be played in full-screen using the YouTubePlayerView from the details screen.
* [x] Implemented fragments, ViewPager, and Fragment Adapter to make clickable tabs that show fragments for Movide Details and Recommended Movies (using the Get Similar Movies API)
* [x] Used Similar Movies endpoint to show a Recycler View of similar movies within the Movie Details Activity
* [x] Used Get Details endpoint to show runtime and genres of movies
* [x] Displayed movie language, release date, vote count, runtime, and genres 

## Video Walkthrough

Here's a walkthrough of implemented user stories:
https://www.internalfb.com/intern/px/p/1KXDf 
<img src='https://github.com/flaurencyac/Flixster/blob/main/flixster.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [Kap](https://getkap.co/).

## Notes
I submitted 25 minutes late because GitHub overwrote my local files, so I reached out to my manager for help understanding where my final code had gone. She helped me figure out that it was in a detached head. After about 2 hours of troubleshooting we couldn't figure out how to import to GitHub without having it revert to my oldest commit (without the work I did on Day #3), so I created this new repo and drag and dropped everything but the Build folder/file.

Additionally, the video -> gif converter made a really bad quality gif! If you'd like to see the high quality walkthrough video please visit PixelCloud: https://www.internalfb.com/intern/px/p/1KXDf 

Difficulties:
• Learning fragment adapters, fragments, and tab layouts (week 3 content)
• understanding how to inflate a layout on the same activity

Some Bugs I ran into:
• Bug: I figured out that the reason why my YouTube videos were not playing was because the key in the JSON for the video ID was "key" not "id," figured that out by looking through the resulting Json Array in the debugging console (with a breakpoint)
• Bug: In MoviesDetailsActivity.java I initially defined context as super.getBaseContext(), which provided me with a context that had nothing to do with the activity I was working in. So, when I did context.startActivity I ran into a bug where the context did not exist for startActivity. A TA explained to me that I should have defined context = this, because I was already using a context to get from MainActivity to MovieDetailsActivity, and therefore should reference that context to get from MovieDetailsActivity to MovieTrailerActivity.
• bug: placeholder image doesn't load for each poster image, only a singular one in the center
- tried to resolve with centercrop(), which made it worse
- checked whether or not the layout width and heights were all okay, they were
- resolution: had to use Pixel 4 emulator and enable auto-rotate in settings
• bug: kept reverting to portrait layout despite flipping phone orientation
- resolution: had to use Pixel 4 emulator and enable auto-rotate in settings

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
