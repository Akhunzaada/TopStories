Top Stories by New York Times App
===========================================================

The app displays list of top stories by New York Times. A story can bookmarked to read it again later.

This app loads Top Stories from New York Times' JSON API at

https://developer.nytimes.com/docs/top-stories-product/1/overview.

The app is based on Google's MVVM Architecture

https://github.com/android/architecture-components-samples/tree/main/GithubBrowserSample.

### UI/UX
Following resources are used for design inspirations:
https://dribbble.com/shots/6616292-News-app

https://dribbble.com/shots/6688580-News-App

### Testing
The project uses both instrumentation tests that run on the device
and local unit tests that run on your computer.
To run both of them and generate a coverage report, you can run:

`./gradlew fullCoverageReport` (requires a connected device or an emulator)

#### Device Tests
##### UI Tests
The projects uses Espresso for UI testing. Since each fragment
is limited to a ViewModel, each test mocks related ViewModel to
run the tests.
##### Database Tests
The project creates an in memory database for each database test but still
runs them on the device.

#### Local Unit Tests
##### ViewModel Tests
Each ViewModel is tested using local unit tests with mock Repository
implementations.
##### Repository Tests
Each Repository is tested using local unit tests with mock web service and
mock database.
##### Webservice Tests
The project uses [MockWebServer][mockwebserver] project to test REST api interactions.


### Libraries
* [Android Support Library][support-lib]
* [Android Architecture Components][arch]
* [Android Data Binding][data-binding]
* [Dagger 2][dagger2] for dependency injection
* [Retrofit][retrofit] for REST api communication
* [Glide][glide] for image loading
* [Timber][timber] for logging
* [espresso][espresso] for UI tests
* [mockito][mockito] for mocking in tests


[mockwebserver]: https://github.com/square/okhttp/tree/master/mockwebserver
[support-lib]: https://developer.android.com/topic/libraries/support-library/index.html
[arch]: https://developer.android.com/arch
[data-binding]: https://developer.android.com/topic/libraries/data-binding/index.html
[espresso]: https://google.github.io/android-testing-support-library/docs/espresso/
[dagger2]: https://google.github.io/dagger
[retrofit]: http://square.github.io/retrofit
[glide]: https://github.com/bumptech/glide
[timber]: https://github.com/JakeWharton/timber
[mockito]: http://site.mockito.org
