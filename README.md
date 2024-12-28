# FetchProject

This app will call an API and retrieve a list of items to display to the user.  The architecture is described below.

## Overall
My architecture was designed in a way to keep separation of concerns and allow updates with minimal disturbance to the different layers of code.  I've split the code into the Data Layer, the Domain Layer, and the UI Layer.

### Domain Layer
The Domain layer is where I've defined what the app does. I've abstracted it as two usecases: A case to view the list (ViewListUseCase) that returns the list of items, and a case to refresh the list (RefreshListUseCase) to get the latest list from the API and save it. 
The work is done by interfacing with the Data Layer through the Repo Interface. That is defined more below.

### Data Layer
The Data Layer is the source of truth for the app. I created a Repo interface that allows the user to only do two things: get the ListItems and refresh the ListItems by calling the remoteAPI.  The concrete implementaiton of the Repo Interface
contains a RemoteDataSource and a LocalDataSource. These are defined as interfaces as well, with the concrete implementation being Room for the LocalDataSource and RetroFit for the RemoteDataSource.  The details of performing this work are abstracted
away from the Domain Layer, the UseCases in the Domain layer only know to call the Repo Interface and receive their results. The usecases are called by the UI layer which is found below

### UI Layer
The UI layer consists mainly of the ViewListViewModel which will handle interfacing with the UseCases in the Domain layer and show the results to the User.  On init of the ViewModel, we make a call to refresh the list so we have the latest list when the app starts up.
this function will actually call the two usecases sequentially using coroutines.  We first call the RefreshListUseCase to call the API and save the results to the RoomDb.  If successful, we then call the ViewListUseCase to fetch the latest items from the Db.
If an Error happens due to network/db/other issues we return the Error and the ViewModel will show the appropriate error message to the user on the ViewListScreen.  Otherwise the user will see the list of Items returned from the Db.  

The UI itself is implemented with Jetpack Compose.  We subscribe to the state in the viewModel (which exposes a StateFlow) with .collectAsStateWithLifecycle to ensure our work is LifeCycle aware.  We also use LazyColumn to show the list of items to handle efficiently
displaying the list to the user and handling orientation changes appropriately.  Also for fun I implemented Sticky Headers so we can keep track of which ListId we are currently looking at.

### Other Notes
I created a RepoResult class to be the main source of communication throughout the app.  This is a sealed class which contains the states of Success, Error, and Loading.  Error is its own sealed class indicating the possible error states: Network, Database, and EmptyList.
Loading is emitted before we perform work and Success is emitted when the sync is successful, and also emitted with the list of items when the ViewListItemsUseCase is invoked.

Another Note, the filtering of the data set to remove blanks, nulls, and order by ListId then Name is done in Room.  After saving the API result to the database, the ViewListUseCase will pull from the Room Db using a query to have the items ordered in the state that we want already.

