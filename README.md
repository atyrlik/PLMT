# PLMT

## Time invested
I spent around 6 hours on this project.
Most of the time was spent familiarizing myself with the MPAndroidChart library.

## Design Choices
I kept the design as simple as possible.
The main screen is composed of two parts:
* A header displaying the live values sent by Flow.
The values are highlighted in green when the aqi is below 50, and in red otherwise.
* The main chart displaying the historical values.
I used a ScatterChart, because there is some blank in the data (when Flow is not active), and this graph made the more sense to me.
A BarChart would have worked as well.

There is a switch for every value, which allow the user to filter which measurements are plotted.

## Architecture
I followed a MVVM approach for this project. The three main parts are:
* ui/main: Contains the main fragment and the associated view-model to display the measures from Flow.
This is simply suscribing to the repository, and plots the data.
* repository: Connect to the blutooth library, listen to the measures, and store them in database (Room).
It then expose this data directly from the database to an observer.
* entities: Models the data we get from Flow, and is directly usable by Room and viewmodels.

There is only one screen, but I usually create one package for every screen (fragment + view-model).

I did not create uses cases here, because there is not enough logic to make them worthwile. In a actual project, I would prefer to use them.

## Technical choices
I choose to use Coroutines for background tasks, and Flow to observe them.
I have more experience with Rx, but I wanted to get more pratical experience with Flow.
It worked pretty well for this project, but I had issues to test the repository.
If I had to start again, I would probably stick with Rx until Flow become more stable.

I used Koin for Dependency Injection. I usually use Dagger, but I really like how easy it is to set up Koin.
So far, I didn't have any issue with it.

I used MPAndroidChart to plot the data, since it was advised in the assignement.
It seems like a powerful library, but I found the documentation a bit lacking.
For example, I originally wanted to have a vertical LineChart, but I didn't find a proper way to do that.
I therefore used a ScatterChart, which worked well enough.

Finally, I used Room as a database.
It is well supported and easily allow to interface with Flow and coroutines.

## Testing
As said above, I had issues to test the repository due to Flow.
I therefore decided to drop the test for this class, and I then did a few tests for MainViewModel.
There was not much logic in this view-model, that's why there is not much testing going on.

## Issues and improvements
I tried to not spend too much time on the UI, so it's now really basic.
With more time, I would polish the graph, and maybe add more data to the screen (like the Flow battery percentage).

Performance wise, the app is currently redrawing the whole chart everytime a new value is available.
This is definitely not scalable with a long history. I would probably paginate the data to fix this issue.
