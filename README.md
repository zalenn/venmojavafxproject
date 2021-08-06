# Venmo Socket Server w/ Java FX

This project was created using JavaFX in Eclipse as a class project. Worked jointly with Jose Balseros (jbalseros@fordham.edu) and supplementary code including form real time clock from Professor Kounavelis (nkounavelis@fordham.edu). 


![Client Screen](https://user-images.githubusercontent.com/51182601/118371849-6d582f00-b57c-11eb-95d3-1b682797c606.png)
![Screen Shot 2021-05-11 at 8 43 47 PM](https://user-images.githubusercontent.com/51182601/118371878-86f97680-b57c-11eb-8189-a35b70397ec4.png)








## Troubleshooting
1. Make sure the JavaFX jmods are downloaded from here:   https://gluonhq.com/products/javafx/
2. Go to Eclipse -> Preferences ->Java -> Build Path - > User Libraries -> New
3. Create a new library called "JAVAFX15" 
4. Click "Add External Jars" 
5. Select all the .jar files from the "lib" folder from the jmods downloaded from the site and put them in the library. 
6. Go to Eclipse -> Preferences-> Run/Debug -> String Substitution -> New
7. Name the variable "PATH_TO_FX" and as the value put the file path to the jmods. (Ex. for me the file path would be /Users/mycomputername/Downloads/javafx-sdk-11.0.2/lib)
8. Go to Run -> Run Configurations -> Java Application -> There should already be a configuration named after your class name (mine was named "Main") -> VM arguments -> Add these " --module-path /path/to/javafx-sdk-15.0.1/lib --add-modules javafx.controls,javafx.fxml
--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml" 
9. While still in Run Configurations, make sure "Use the -XstartOnFirstThread argument while launching with SWT box is unclicked and apply the changes. 







