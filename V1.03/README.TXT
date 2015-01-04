##############################################################################

Welcome to Mid2Tap by MLCheese

##############################################################################

Requirements
-You need Java, preferably the latest version
-Windows, Mac, and Linux OS should all work

##############################################################################

These fields are required for the conversion to be successful.
-Song name and artist
-MIDI file
-Audio file, either MP3 or M4A.
-Output folder that doesn't require administrator privileges (unless you ran the program with administrator privileges)

##############################################################################

MIDI Support

The program should handle any MIDI file with one track and only three different note values! The code should allow for multiple tracks, but that is untested.

It does not matter where the notes are! Whether your values are at 96, 97, 98 or your values are at 3, 45, 92, as long as they are valid MIDI note values. This means you can chart the MIDI in whatever three values you want. The program only checks that there are 3 different types of notes throughout (fails otherwise), and takes the lowest value as the left note, middle value as middle note, and highest value as right note.

Keep a constant BPM! Set the initial value of the BPM to whatever you would like, but do not vary it throughout the track. Only one BPM value should ever be set per track.

The constant BPM value does not matter! The program makes use of the algorithm that MIDI uses to calculate time. It will generate the time values using that algorithm. When creating the taptrack file, it will convert the times to the standard 120 BPM, since that is easier to do and read.

##############################################################################