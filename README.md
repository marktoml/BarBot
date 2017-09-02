# BarBot
AutoMatic Bartender Source
This was originally based on a project by Yu Jiang Tham
http://yujiangtham.com/2014/05/25/build-your-very-own-drink-mixing-robot-part-1/

We took his idea and used the same electrical and mechanical ideas for the pumps to build a board that would interface
with a Raspberry Pi w/touchscreen. The code to drive the bartender is then written for the Pi in Java
The ultimate goal is a db with all the recipies and configuration so that it may be easily and dynamically 
reconfigured. 

Currently the Java GUI reads the properties file and displays the drinks. When one is chosen a thread is spawned for each ingrediant
to run that motor for the appropriate interval. 
