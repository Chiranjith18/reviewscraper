Restaurant Reviews Scraper
Project Overview

This is a Java Selenium scraper that collects restaurant reviews from Zomato for multiple cities and saves them into a CSV file. It uses OpenCSV for writing CSVs and WebDriverManager to handle ChromeDriver automatically.

Directory Structure

demo/src/main/java/com/example/App.java — Main scraper program
Optional: Utils.java — Helper functions
config.properties — Configuration for URLs, output paths, and settings

Viewing the Code

1.Navigate to the demo folder

2.Open src → main → java → com → example

3.Open App.java to see the main working code

4.Optional: Utils.java for helpers, config.properties for settings

Running the Project

1.Ensure Maven is installed and all dependencies are available (pom.xml should include Selenium, OpenCSV, WebDriverManager)

2.Compile and run App.java

3.Output CSV will be generated in the project root
