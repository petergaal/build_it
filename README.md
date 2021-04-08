It's a simple Java project with Maven and some test unit.
I didn't build it for command line, so the best to try everything 
inside IntelliJ Idea environment.
I used two classes, one is a parser (where I made also an interface), and the other one is a sitemap builder.
Mostly I chose this, so it's easy to test with unit tests / mocks.

To fully parse the page the limit (`GET_PAGE_TIMEOUT` in `HtmlParser`) needs to be raised (or removed), 
currently it's doing up to 10 requests.
When I did a full run it was able to finish the page parsing,
but it took some time.

It's not handling correctly the links which are relative, and it will put into external links (which are not requested).
That could be improved with more time (in 2 hours time you can't do everything).
Static links are only parsed from images (img src), there are probably others as well on the web page which can be parsed).
The read timeout is currently 10 seconds, network errors are not handled, there is not retry if it fails, etc.
Also it's not able to make a difference between the same website if one link is starting with `"http://"` and other is with `"https://"`.

It should run fine inside IntelliJ IDEA (community edition), I used JDK 1.8.0.261 (and tested also on 11.0.7), should run on Java 8 and higher.
On older version probably not as I used Java streams at least in the test.

This project is not set up to run an application from maven command line.

