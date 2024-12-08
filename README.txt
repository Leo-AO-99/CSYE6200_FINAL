1. clone the repo

git clone https://github.com/Leo-AO-99/CSYE6200_FINAL.git

2. download the javaFX SDK

https://gluonhq.com/products/javafx/

setup the JAVA_HOME to the javafx-sdk-22 based on your operating system

3. open in the Eclipse IDE

open Eclipse IDE -> File -> Open Projects from File System -> Select the folder of the cloned repo -> Finish

right click on the project -> Build Path -> Configure Build Path -> Libraries -> select "classPath" -> click the "Add Library" button -> User Library -> JavaFX -> OK -> Finish

4. run config

right click on src.edu.northeastern.csye6200.Main -> Run As -> Run Configurations

4.1 Arguments tab

click on the Arguments tab, and add the following arguments in the VM arguments:

--module-path <YOUR_PATH>/javafx-sdk-22.0.2/lib --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web

one more thing, uncheck all the checkboxes in below the VM arguments section

4.2 Dependencies tab

click on the Dependencies tab  -> select the "Moduldepath Entries" -> click the Advanced button on the right -> select the "Add Library: -> OK button -> User Library -> JavaFX -> OK -> Finish

4.3 Run

do not forget to click the Apply button to save the configuration

5. Software operation

just use your mouse and keyboard to interact with the software

you can find the tab buttons on the top left corner of the window, which is the way to switch between different pages