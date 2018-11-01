# domui-skeleton

This is a skeleton bare-bones DomUI application that can be used as a template to build your own DomUI applications
quickly, without having to learn about everything that is needed to initialize a realistic web application.

The skeleton app consists of three modules:

* app-core is the logic module. It contains non-UI code and business logic.

* app-ui is the DomUI user interface module. It contains all code that is related to the UI, and itself depends on
app-core for its logic

* app-web is the war assembly project. It consists of the maven spells to turn a set of projects in a war, and 
contains the web related resources (like images, static pages and whatnot).

This is just an example structure; is it of course easy to add more modules using the normal Maven module support. 
Ok, not too easy, then ;)

This repository has multiple branches (well, it will have once DomUI 2.0 has been released):

* branches like "2.0" are release branches, and are supposed to work with the DomUI release of the same version. 
These branches can still change though when fixes are made on released versions. These branches include DomUI by adding
it to maven's dependencies. This means DomUI is added as a set of jars.

* the master branch is related to the DomUI master branch and is the tip of new development. As such it is less stable 
but of course is very modern ;). The master branch includes DomUI as a __submodule__: as such it gets cloned together with
this repository. This means one has to learn about submodules - but it makes maintaining DomUI and the skeleton app way easier.

## Using the skeleton app

To use the skeleton app you clone it from the repository, using the branch that relates to the DomUI version you want to use.
After that you can change the name of the modules in the poms and whatever else in the code, starting your own application
by fleshing out the skeleton. You can easily push this new code to a new repository of your own.

Let's try it. Start by cloning the repository (we will take the master branch) in a new directory called "petshop":

    git clone -b master --recursive https://github.com/fjalvingh/domui-skeleton petshop
    
This will mumble some cryptic things to the screen and takes a while. You should now have a new directory "petshop".
cd in there and start a maven build:

mvn clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true

(the skips speed up the build, and running the tests means installing software before they run).
With a bit of luck this will end with BUILD SUCCESS after some 20 seconds.

## Running the webapp with Maven (for the silly ones)

Those who like pain can run the skeleton app from Maven. This is useful for quick tests, but to do real development
you should really use a Java IDE, of course. To run from maven do this:

* Do a full Maven build in the root of the repository (the petshow directory in this example) as described above.

* cd' to the app-web project, and run:

        mvn jetty:run

This should start the Jetty web server with the skeleton app, and open a browser into it.

*warning* Do not forget to do the maven build in the toplevel directory whenever you change something! Maven is
quite the disaster as tools go, and building "too low" or "too little" will give you incomprehensible bugs and
a headache.

# Using IntelliJ with the skeletom app

Jetbrains IntelliJ Idea can natively read Maven's poms and once initialized compiles way faster - and with infinitely 
better error support. To run the skeleton app "out of the box" from IntelliJ you must have the Ultimate edition. You
can still develop with the community edition but you will need to find out how to run a webapp in there.

To import your new app in IntelliJ you must do the following:

* Make sure to compile AT LEAST ONCE with maven, as in the example above! DomUI contains some generated code (grammar
files and such), and IntelliJ is not able (yet) to generate these. The maven build does generate them, and once generated
they will be used by IntelliJ.

* Start up intelliJ, and on the "Welcome" screen select "open".

* Select the directory that you cloned in (petshop in the example), and let IntelliJ finish its work. Wait until you no
longer see a progress bar in the bottom right statusbar area before continuing.

* In the right side gutter of the screen select the vertical "Maven" view, then press the refresh button (first icon
on the toolbar). If the Maven view tells you that there are no pom's loaded then add the pom in the root of the petshop
directory to it; that will automatically find all other modules.

* In the IDE, go to File -> Settings, then go to Build, Execution, Deployment -> Build tools -> Maven -> Importing

* In that screen find "Import maven projects automatically" and check the box. This makes IntelliJ re-import poms
automatically on changes, and is a huge time saver. Leave the screen with OK.

* Do a build (Build -> Rebuild Project). We only do this once; usually a normal Build suffices.

You might have some errors during the above build, depending on your configuration and your version of IntelliJ. 
The most common ones are:

* "No JDK for project": Use File -> Project Structure, then make sure that the correct JDK (8, 10 or 11 depending
on the DomUI release) is selected, and that the correct language level has been selected.

* "Invalid flag: -properties": DomUI uses the Eclipse Batch compiler (ecj) for its compilation, because that compiler
is way faster than the normal Javac compiler, and has a lot of extra options that make building Java software safer.
You need to tell IntelliJ to use that compiler, as follows:

** Open File -> Settings, then go to Build, Execution, Deployment -> Compiler -> Java Compiler

** On top, at "Use Compiler", select "Eclipse".

Once you have fixed the issues the build should succeed, and after that a normal incremental build suffices.


# Running the skeleton app in IntelliJ

To run the app in IntelliJ we need to create a run configuration. One should be present already, but if you want
to make it manually these are the steps:

* Open "Run -> Edit Configurations".

* In the top right of the screen, press the + icon on the toolbar. Then select the Tomcat -> Local template. If you do not see 
it click the "More items" thingy that calls itself irrelevant but is not.

* Fill in a name for the config (like "petshop"), select a Tomcat 8.x installation (or add it using the three ... button, then select).

* Click the Deployment tab, then click the green + at the right side. Pick the "app-web exploded" artifact.

* Under the list of artifacts is the "Application Context" input. Change it from / to /petshop.

* Now go back to the "Server" tab, and change the "On update action" to "Update classes and resources". Then remove the check 
before "show dialog".

Close the window with the OK button, and run/debug the web app as usual from IntelliJ.

