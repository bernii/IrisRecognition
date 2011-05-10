============
iris-recognition
============

This repository hosts the Iris Recognition open source Java software code.

Iris recognition is considered as the most reliable biometric identification system. Majority of commercial biometric systems use patented algorithms. This software is competly free and was based on work of Libor Masek presented in his thesis ( http://www.csse.uwa.edu.au/~pk/studentprojects/libor/LiborMasekThesis.pdf ).

.. image:: https://github.com/bernii/iris-recognition/raw/master/screenshot.jpg

How it works:
============

As stated in Libor thesis, system consists of a segamatation system based on the Hough transform. It is able to localise iris and pupil region, excluding eyelids, eyelashes and reflecions.

Iris region is then normalised and filtered by 1D Log-Gabor. Phase data is extracted and quantised to four levels creating an unique pattern of the iris.

The Minkowski distance is used for classification and comparison of patterns.

For testing purposes we used a database of eye images that is available from the Chinese Academy of Sciences. ( http://www.sinobiometrics.com/resources.htm )

How to use:
============

* As application uses SWT library that comes with Eclipse you have to add it to your classpath.
* For MS Windows users: you can use start.bat to start the application. ( it adds required libraries to class path and starts the app ) 
* It also uses jhlabs.com image processing library for polar coordinates transform ( all libs included in libs subdirectory )

Prereqs:
============
* jhlabs.com image processing library ( http://www.jhlabs.com/ie/ImageEditor.jar )
* The Standard Widget Toolkit (comes with Eclipse) ( swt.jar )
* Bio-medical Imaging ( http://bij.isi.uu.nl/downloads/bij_plugin.zip )
* ImageJ ( http://imagej.nih.gov/ij/download/zips/ij144.zip ) ( ij.jar )
* all above included in libs subdirectory

:Authors:
    Bernard Kobos,
    Piotr Zaborowski.
	( Warsaw University of Technology )




