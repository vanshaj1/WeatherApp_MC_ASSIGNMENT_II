# The Weather App
# Component Structure
   * **Main Activity**
     * [**GetWeatherInfoComponent**](#GetWeatherInfoComponent)


# GetWeatherInfoComponent
 - This having :- 
    - Mutable States 
      * [states](#states) 
    - Composables
      * [**Date Picker**](#Date Picker)
      * [**Button**](#Button)
 - This have arranged all components inside in **Column** composable

# Data Classes
 - These are the following data classes in the project:-
    * WeatherInfo
    * WeatherDataX
       * Daily
       * DailyUnits

# Api used  
   * open-meteo
     - Forecast :- https://api.open-meteo.com/v1/forecast
     - Archive  :- https://archive-api.open-meteo.com/v1/archive

# Dependency used for making Api call is :-
  *  Retrofit
    - gson retrofit converter:- 
        For parsing json data returned by Api and converting data into corresponding Data class object such as WeatherDataX 
    - **coroutines** were used to make Api calls by using retrofit library such as Global scope coroutine
    - its object is defined in file RetrofitInt.kt just need to change base Url and retrofit object wil work for api calls according to thsi base url

# Dependency used for making Database :-
  * Room 
    - for using it we created 
        - Dataclass i.e WeatherInfo
        - Dao interface i.e weatherDao
        - Database class i.e weatherDatabase 
     * *WeatherInfo* represents the table WeatherInfo inside the database so its object is used to insert to and fetch from the database
        - it is having attributes such as 
            * date: of type String and It is primary key
            * temperatureMin: of type String
            * temperatureMax: of type String
     * It is having three fields that are 
        - Date of type string
        - temperatureMin of type string
        - temperatureMax of type string
     * *weatherDao* is used for defining functions that we want to provide with database to access it
        * It is having two functions
            - insertWeatherInfo :- It is used to insert data of type WeatherInfo into database
            - getWeatherInfoByDate :- It is used to get data of type WeatherInfo from the database based on date as filter
     * *weatherDatabase* is used for linking weatherDao with WeatherInfo and create a database. It also provides a singleton object for accessing the database  
   
   * The database created by this room is used in replacement of api calls , So whenever api calls made the date retured by it saved into the database and when internet is not available then this database will be used to give user data according to the query asked previously by user when internet connectiviity is there.
 
# States
- On change of these states recomposition happens and composable functions which are dependent on these states will get _**re-composed / re-rendered**_.

 * **state**
   This is the state of the date picker that changes when the user select new date. It is having property date to get the value of date given by user. Its Unit for date is millis but we need to convert that into format yyyy-MM-dd by using formater class by defining corresponding format like yyyy-MM-dd
 * **minimumTemperature**
    This value is used to display minimum temperature according to the date value in state.
 * **maximumTemperature**
    This value is used to display the maximum temperature according ti the date in state.
 * **makeVisible**
    This is of boolean type. Its value define whether to show the datepicker or not.
 * **animationVisibility**
    This is of boolean type. Its value is used for showing text message which provide information based on minimumTemperature and maximumTemperature states about the temperature status of current date.
 * **WarningAnimationVisibility**
    This is also of boolean type. It is used for showing text that represents error message. if any error occured

# For checking internet connectivity 
  * Function checkNetworkConnectivity is defined for checking connectivity
    - In this from ConnectionManager CONNECTIVITY_SERVICE is used for checking internet connectivity such as      
      * TRANSPORT_ETHERNET
      * TRANSPORT_WIFI
      * TRANSPORT_CELLULAR
      Accoridng to the boolean values from all these values function returns true of false.
      When either of these value is true then function returns true else false.

# Flow of the program according to user action
   * Program starts when user clicks on the datePicker and select date from the datepicker then the state changes and program get recomposed
   * Now for getting information about temperature user clicks on the get temperature status button then on click a lambda functions is called that checks following things and give output accordingly
    - if the date chosed by user is after next year dates then it will give alert that sorry we cannot provide information for these future dates
    - if internet connectivity is not there then using database object given by weatherDatabase, data is fetched from the database and present to the user by changing values of states such as **minimumTemperature** , **maximumTemperature** and **animationVisibility**
    - if internet connectivity is there then 
          * if date is of past then data corresponding to that date is fetched using Archive endpoint of open-meteo api and present it to the user by changing value of states such as **minimumTemperature** , **maximumTemperature** and **animationVisibility** according to data recieved from the api.
          * if date is of the future then past 10 years minimumTemperature and maximumTemperature data of same day and same month is fetched using *Archive* endpoint of the *open-meteo* and averaged out and presented to the user by changing value of states such as **minimumTemperature** , **maximumTemperature** and **animationVisibility** 
          * if the date given by user is of next year then we checks that is day and month given by user is future of the current year or past of the current year. if it is future of the current year then we show an alert to the user that we cannot show the data of this future date because this date didnt appeared yet in the past year else we will average out past 10 years data for this day and month and present it to the user by changing value of states such as **minimumTemperature** , **maximumTemperature** and **animationVisibility** according
          * While changing values of the **minimumTemperature** , **maximumTemperature** , we save these values in the database with date chosed by user as primary key . So, when the internet connectivity is not there then we can use database to the results. 
          * when internet connectivity is not there and also the database is not having result of the query we toggle the value of **WarningAnimationVisibility** to the error message to the user that *Data is not available offline* 

# Output

<img height="600" src="TheWeatherAppUi.jpg" width="300"/>