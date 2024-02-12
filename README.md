# Weatherlady
## Weather Data Retrieval And Storing Application

### Short description 

In the wake of natural disasters like earthquakes or volcanic eruptions- meteorologists, 
researchers, and analysts play a crucial role in documenting these events along with associated 
city, country, and weather data. This comprehensive information serves as a vital resource for 
future analysis and disaster preparedness efforts. With the help of this application, professionals 
can efficiently collect and store pertinent weather data alongside event details. This application might
be useful to predict and document future disasters.

### Overview

The Weather Data Retrieval Application is a Java-based tool designed to fetch, process, 
and store weather data from various sources. It allows users to retrieve real-time weather 
information for specific locations and save it to a database for further analysis.

Users can insert a new disaster by providing the following information:

- The name of the city where the disaster occurred
- Name of the specific disaster
- An optional comment, such as the magnitude of an earthquake

Users can also search for disasters that have occurred by:

- The date on which it happened
- The name of the disaster
- The name of the city where it happened
- The name of the country where it happened

### Features

- Data Retrieval: Fetch weather data from multiple external APIs- WeatherStack, AccuWeather, OpenWeather.
- Data Processing: Parse JSON responses to extract relevant weather parameters such as temperature, 
humidity, wind speed, and direction.
- Data Storage: Store retrieved weather data in a relational database using Hibernate ORM.

## Installation

#### Clone the repository to your local machine:

To clone using HTTPS, run the following command: ``` git clone https://github.com/Ellucy/Weatherlady.git ```

To clone using SSH, run the following command: ```git clone git@github.com:Ellucy/Weatherlady.git ```

#### Set up your development environment by ensuring you have JDK, Maven, and a supported IDE installed and navigate to the project directory.

#### Configure environment variables for API keys:

- For WeatherStack: WS_API_KEY
- For AccuWeather: AW_API_KEY
- For OpenWeather: OW_API_KEY

#### Under hibernate configuration file update username and password. Create empty database called "weather".