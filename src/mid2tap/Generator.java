package mid2tap;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Arrays;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JOptionPane;
import net.coobird.thumbnailator.Thumbnails;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

/**
 *
 * Contains all functions for each part of the conversion process.
 * 
 * @author Kevin
 * 
 */
public class Generator {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    
    /**
     * 
     * Creates a new info.plist in the "artist - song.track" folder.
     * Calculates the audio duration that is needed in the info.plist anyways.
     * Writes what is mostly static data, adding in variables where needed.
     * 
     * @param artist The artist specified by the user.
     * @param song The song specified by the user.
     * @param theme The theme specified by the user.
     * @param outputFolder The output folder specified by the user.
     * @param audioFile The audio specified by the user.
     * @return The audio duration if plist creation was successful,
     * -1 if plist creation was unsuccessful.
     * @exception IOException caught if the file cannot be written to.
     * 
     */
    public static int generatePlist(String artist, String song, String theme,
            File outputFolder, File audioFile) {
        
        try {
            File plist = new File(outputFolder.getCanonicalPath() + "\\info.plist");
            int audioDuration = audioLength(audioFile);
            
            FileWriter writer = new FileWriter(plist);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
            writer.write("<plist version=\"1.0\">\n");
            writer.write("<dict>\n");
            writer.write("\t<key>audioFileDuration</key>\n");
            writer.write("\t<real>" + audioDuration + "</real>\n");
            writer.write("\t<key>availableDifficultyLevels</key>\n");
            writer.write("\t<array>\n");
            writer.write("\t\t<integer>3</integer>\n");
            writer.write("\t</array>\n");
            writer.write("\t<key>bossLevels</key>\n");
            writer.write("\t<array/>\n");
            writer.write("\t<key>kTTRBandLink</key>\n");
            writer.write("\t<string>http://www.taptaprevenge.com/</string>\n");
            writer.write("\t<key>kTTRDownloadableItemCommandsKey</key>\n");
            writer.write("\t<array>\n");
            writer.write("\t\t<dict>\n");
            writer.write("\t\t\t<key>kTTRDownloadableItemPreferredFilenameKey</key>\n");
            writer.write("\t\t\t<string>audio.m4a</string>\n");
            writer.write("\t\t\t<key>kTTRDownloadableItemSourceURLKey</key>\n");
            writer.write("\t\t\t<string>null</string>\n");
            writer.write("\t\t</dict>\n");
            writer.write("\t\t<dict>\n");
            writer.write("\t\t\t<key>kTTRDownloadableItemPreferredFilenameKey</key>\n");
            writer.write("\t\t\t<string>artwork.png</string>\n");
            writer.write("\t\t\t<key>kTTRDownloadableItemSourceURLKey</key>\n");
            writer.write("\t\t\t<string>null</string>\n");
            writer.write("\t\t</dict>\n");
            writer.write("\t\t<dict>\n");
            writer.write("\t\t\t<key>kTTRDownloadableItemPreferredFilenameKey</key>\n");
            writer.write("\t\t\t<string>taptrack.ttr2_track</string>\n");
            writer.write("\t\t\t<key>kTTRDownloadableItemSourceURLKey</key>\n");
            writer.write("\t\t\t<string>null</string>\n");
            writer.write("\t\t</dict>\n");
            writer.write("\t\t<dict>\n");
            writer.write("\t\t\t<key>kTTRDownloadableItemDependencyKey</key>\n");
            writer.write("\t\t\t<string>Themes/TTRDJ.ttrTheme</string>\n");
            writer.write("\t\t\t<key>kTTRDownloadableItemSourceURLKey</key>\n");
            writer.write("\t\t\t<string>null</string>\n");
            writer.write("\t\t\t<key>kTTRDownloadableItemVersionKey</key>\n");
            writer.write("\t\t\t<integer>0</integer>\n");
            writer.write("\t\t</dict>\n");
            writer.write("\t</array>\n");
            writer.write("\t<key>kTTRDownloadableItemSubtitleKey</key>\n");
            writer.write("\t<string>" + artist + "</string>\n");
            writer.write("\t<key>kTTRDownloadableItemTitleKey</key>\n");
            writer.write("\t<string>" + song + "</string>\n");
            writer.write("\t<key>kTTRDownloadableItemTypeKey</key>\n");
            writer.write("\t<string>track</string>\n");
            writer.write("\t<key>kTTRDownloadableItemUniqueIdentifierKey</key>\n");
            writer.write("\t<string>MLC-" + artist + ": " + song + "</string>\n");
            writer.write("\t<key>kTTRDownloadableItemVersionKey</key>\n");
            writer.write("\t<integer>0</integer>\n");
            writer.write("\t<key>kTTRGenre</key>\n");
            writer.write("\t<string>MetalAF</string>\n");
            writer.write("\t<key>kTTRiTunesLink</key>\n");
            writer.write("\t<string>null</string>\n");
            writer.write("\t<key>levelThemes</key>\n");
            writer.write("\t<dict>\n");
            writer.write("\t\t<key>3</key>\n");
            writer.write("\t\t<string>" + theme + "</string>\n");
            writer.write("\t</dict>\n");
            writer.write("\t<key>lockedLevels</key>\n");
            writer.write("\t<array/>\n");
            writer.write("<key>themeName</key>\n");
            writer.write("<string>" + theme + "</string>\n");
            writer.write("</dict>\n");
            writer.write("</plist>\n");
                    
            writer.flush();
            writer.close();
            
            return audioDuration;
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Error writing plist!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return -1;
    }
    
    /**
     * 
     * Calculates the audio duration by reading the audio file header.
     * 
     * @param audioFile The audio the user specified.
     * @return The audio duration if successful, -1 if unsuccessful.
     * @exception CannotReadException Caught if the file cannot be read.
     * @exception TagException Caught if the audio tags are invalid.
     * @exception ReadOnlyFileException Caught if the file is read-only.
     * @exception InvalidAudioFrameException Caught if a frame in the audio 
     * is invalid
     * @exception IOException Caught if the file cannot be read.
     * @exception NullPointerException Caught if the file path is invalid.
     * 
     */
    public static int audioLength(File audioFile) {
        try {
            AudioFile aFile = AudioFileIO.read(audioFile);
            AudioHeader header = aFile.getAudioHeader();
            return header.getTrackLength();
    
        } catch (CannotReadException ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Audio format not supported!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        } catch (TagException ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Tag Exception!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        } catch(ReadOnlyFileException ex) {
                        JOptionPane.showMessageDialog(Loading.loadingFrame, "Read Only Exception!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        } catch(InvalidAudioFrameException ex) {
                        JOptionPane.showMessageDialog(Loading.loadingFrame, "Invalid Audio Frame Exception!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Unable to read audio file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        } catch(NullPointerException ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Audio file not found!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    
    /**
     * 
     * Takes the artwork the user specified, resizes it to 64x64 and 128x128, and
     * copies it to the "artist - song.track" folder.
     * 
     * @param artworkFile The artwork file specified by the user.
     * @param outputFolder The output folder specified by the user.
     * @exception IOException Caught if the artwork file cannot be read or written
     * 
     */
    public static void generateArtwork (File artworkFile, File outputFolder) {
        try {
            if(artworkFile.exists()) {
                BufferedImage artworkImage = ImageIO.read(artworkFile);
                BufferedImage artworkImage2x = artworkImage;

                artworkImage2x = Thumbnails.of(artworkImage2x).forceSize(128, 128).asBufferedImage();
                artworkImage = Thumbnails.of(artworkImage2x).forceSize(64, 64).asBufferedImage();

                File outputImage2x = new File(outputFolder.getCanonicalPath() + "\\artwork@2x.png");
                File outputImage = new File(outputFolder.getCanonicalPath() + "\\artwork.png");            

                ImageIO.write(artworkImage2x, "png", outputImage2x);
                ImageIO.write(artworkImage, "png", outputImage);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Unable to read image file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException ex) {
        }
    }
    
    /**
     * 
     * Copies the audio file and renames it to "audio.m4a". Tap Tap Revenge
     * still reads MP3 files renamed to an M4A extension.
     * 
     * @param audioFile The audio file specified by the user.
     * @param outputFolder  The output folder specified by the user.
     * @exception IOException Caught if the file cannot be read or written.
     * 
     */
    public static void generateAudio (File audioFile, File outputFolder) {
        try {
            File outputAudio = new File(outputFolder.getCanonicalPath() + "\\audio.m4a");
            Files.copy(audioFile.toPath(), outputAudio.toPath(), REPLACE_EXISTING);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Unable to move audio file!",
                    "Error", JOptionPane.ERROR_MESSAGE);;
        }
    }
    
    /**
     * 
     * Parses the MIDI file by recording every track and the note values on them.
     * Ignores any data in the tracks that does not contain a note.
     * 
     * @param eofFile The MIDI file specified by the user.
     * @return A linked list of the notes.
     * @exception IOException Caught if the MIDI cannot be read.
     */
    public static LinkedList<Note> parseMidi(File eofFile) {
        LinkedList<Note> notes = new LinkedList<>();
        
        try {
            Sequence sequence = MidiSystem.getSequence(eofFile);
            // Used in the conversion from MIDI tick time to real time
            double timePerTick = getTimePerTick(eofFile, sequence);
            
            // Throw exception if the tick was not calculated correctly
            if(timePerTick == -1)
                throw new Exception();
            
            // Run for every track
            for (Track track :  sequence.getTracks()) {
                // Check every part of the track
                for (int i=0; i < track.size(); i++) { 
                    MidiEvent event = track.get(i);                            
                    MidiMessage message = event.getMessage();
                    
                    if (message instanceof ShortMessage) {
                        Note note = new Note();
                        
                        // Calculate real time using ((MIDI tick) * (timePerTick))
                        note.setRealTime((event.getTick() * timePerTick));
                        // Convert tick time to that of 120 BPM
                        note.setQuarterTime(note.getRealTime() * 2);
                        
                        ShortMessage sm = (ShortMessage) message;
                        
                        if (sm.getCommand() == NOTE_ON) {
                            int key = sm.getData1();
                            note.setStartEnd('0');
                            
                            // Validation check to make sure that the note
                            // was previously closed before a new one is opened.
                            // This is a work around for a rare bug.
                            if(notes.size() > 0) {
                                for(int j = (notes.size() - 1); j >= 0; j--) {
                                    if(notes.get(j).getNoteValue() == key) {
                                        if(notes.get(j).getStartEnd() == '0') {
                                            note.setStartEnd('1');
                                            break;
                                        }
                                        
                                        else {
                                            break;
                                        }
                                    }
                                }
                            }
                            
                            note.setNoteValue(key);
                            
                        } else if (sm.getCommand() == NOTE_OFF) {
                            int key = sm.getData1();
                            note.setStartEnd('1');
                            note.setNoteValue(key);
                        }
                        
                        notes.add(note);
                    }
                }
            }
            
            // Put the first note in a linked list
            LinkedList<Integer> noteTracker = new LinkedList<>();
            noteTracker.add(notes.get(0).getNoteValue());
            
            // Add any unique note values into the linked list
            for(int i = 1; i < notes.size(); i++) {
                Note tempNote = notes.get(i);
                if(!noteTracker.contains(tempNote.getNoteValue()))
                    noteTracker.add(tempNote.getNoteValue());
            }
            
            // If there are 3 unique note values
            if(noteTracker.size() == 3) {
                int[] noteConverter = new int[3];
                noteConverter[0] = (int) noteTracker.get(0);
                noteConverter[1] = (int) noteTracker.get(1);
                noteConverter[2] = (int) noteTracker.get(2);
                
                Arrays.sort(noteConverter);
                
                // Convert all given note values in the notes linked list.
                // Lowest = 60 (left note)
                // Middle = 62 (middle note)
                // Highest = 64 (right note)
                for(int i = 0; i < notes.size(); i++) {
                    Note tempNote = notes.get(i);
                    
                    if(tempNote.getNoteValue() == noteConverter[0]){
                        tempNote.setNoteValue(60);
                        notes.set(i, tempNote);
                    }
                    
                    else if(tempNote.getNoteValue() == noteConverter[1]) {
                        tempNote.setNoteValue(62);
                        notes.set(i, tempNote);
                    }
                    
                    else {
                        tempNote.setNoteValue(64);
                        notes.set(i, tempNote);
                    }
                }
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Error parsing MIDI file!",
                    "Error", JOptionPane.ERROR_MESSAGE);;
        }
        
        return notes;
    }
    
    /**
     * 
     * Creates the taptrack file by first parsing the MIDI file and then
     * writing both constant and variable data.
     * 
     * @param audioDuration The duration of the song.
     * @param eofFile The MIDI file specified by the user.
     * @param outputFolder The output folder specified by the user.
     * @param artist The artist specified by the user.
     * @param song The song specified by the user.
     * 
     */
    public static void generateTTR2(int audioDuration, File eofFile, File outputFolder,
            String artist, String song) {
        try {
            File ttr2File = new File(outputFolder.getCanonicalPath() + "\\taptrack.ttr2_track");
            LinkedList<Note> notes = parseMidi(eofFile);
            Sequence sequence = MidiSystem.getSequence(eofFile);
            FileWriter writer = new FileWriter(ttr2File);
            
            // Begin writing data. Most of it here is constant.
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
            writer.write("<plist version=\"1.0\">\n");
            writer.write("<dict>\n");
            writer.write("\t<key>$archiver</key>\n");
            writer.write("\t<string>NSKeyedArchiver</string>\n");
            writer.write("\t<key>$objects</key>\n");
            writer.write("\t<array>\n");
            writer.write("\t\t<string>$null</string>\n");
            writer.write("\t\t<dict>\n");
            writer.write("\t\t\t<key>$class</key>\n");
            writer.write("\t\t\t<dict>\n");
            writer.write("\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t<integer>" + (notes.size() + 25) + "</integer>\n");
            writer.write("\t\t\t</dict>\n");
            writer.write("\t\t\t<key>NS.keys</key>\n");
            writer.write("\t\t\t<array>\n");
            writer.write("\t\t\t\t<dict>\n");
            writer.write("\t\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t\t<integer>2</integer>\n");
            writer.write("\t\t\t\t</dict>\n");
            writer.write("\t\t\t</array>\n");
            writer.write("\t\t\t<key>NS.objects</key>\n");
            writer.write("\t\t\t<array>\n");
            writer.write("\t\t\t\t<dict>\n");
            writer.write("\t\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t\t<integer>3</integer>\n");
            writer.write("\t\t\t\t</dict>\n");
            writer.write("\t\t\t</array>\n");
            writer.write("\t\t</dict>\n");
            writer.write("\t\t<integer>4</integer>\n");
            writer.write("\t\t<dict>\n");
            writer.write("\t\t\t<key>$class</key>\n");
            writer.write("\t\t\t<dict>\n");
            writer.write("\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t<integer>" + (notes.size() + 24) + "</integer>\n");
            writer.write("\t\t\t</dict>\n");
            writer.write("\t\t\t<key>copyrightInfo</key>\n");
            writer.write("\t\t\t<dict>\n");
            writer.write("\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t<integer>5</integer>\n");
            writer.write("\t\t\t</dict>\n");
            writer.write("\t\t\t<key>exactTempo</key>\n");
            writer.write("\t\t\t<real>120</real>\n");
            writer.write("\t\t\t<key>rawData</key>\n");
            writer.write("\t\t\t<dict>\n");
            writer.write("\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t<integer>" + (notes.size() + 20) + "</integer>\n");
            writer.write("\t\t\t</dict>\n");
            writer.write("\t\t\t<key>tempoChanges</key>\n");
            writer.write("\t\t\t<dict>\n");
            writer.write("\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t<integer>" + (notes.size() + 21) + "</integer>\n");
            writer.write("\t\t\t</dict>\n");
            writer.write("\t\t\t<key>timeDivision</key>\n");
            writer.write("\t\t\t<real>" + sequence.getResolution() + "</real>\n");
            writer.write("\t\t\t<key>title</key>\n");
            writer.write("\t\t\t<dict>\n");
            writer.write("\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t<integer>4</integer>\n");
            writer.write("\t\t\t</dict>\n");
            writer.write("\t\t\t<key>trackID</key>\n");
            writer.write("\t\t\t<integer>9592001</integer>\n");
            writer.write("\t\t\t<key>tracks</key>\n");
            writer.write("\t\t\t<dict>\n");
            writer.write("\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t<integer>6</integer>\n");
            writer.write("\t\t\t</dict>\n");
            writer.write("\t\t</dict>\n");
            writer.write("\t\t<string>" + song + "</string>\n");
            writer.write("\t\t<string>" + artist + "</string>\n");
            writer.write("\t\t<dict>\n");
            writer.write("\t\t\t<key>$class</key>\n");
            writer.write("\t\t\t<dict>\n");
            writer.write("\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t<integer>16</integer>\n");
            writer.write("\t\t\t</dict>\n");
            writer.write("\t\t\t<key>NS.objects</key>\n");
            writer.write("\t\t\t<array>\n");
            writer.write("\t\t\t\t<dict>\n");
            writer.write("\t\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t\t<integer>7</integer>\n");
            writer.write("\t\t\t\t</dict>\n");
            writer.write("\t\t\t\t<dict>\n");
            writer.write("\t\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t\t<integer>18</integer>\n");
            writer.write("\t\t\t\t</dict>\n");
            writer.write("\t\t\t</array>\n");
            writer.write("\t\t</dict>\n");
            writer.write("\t\t<dict>\n");
            writer.write("\t\t\t<key>$class</key>\n");
            writer.write("\t\t\t<dict>\n");
            writer.write("\t\t\t\t<key>CF$UID</key>\n");
            writer.write("\t\t\t\t<integer>17</integer>\n");
            writer.write("\t\t\t</dict>\n");
            writer.write("\t\t\t<key>events</key>\n");
            writer.write("\t\t\t<dict>\n");
            
            // I finally gave up writing one write method per line. Here is just
            // huge chunks of data per write method.
            writer.write("\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>8</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$class</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>16</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>NS.objects</key>\n" +
                "\t\t\t<array>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t\t<integer>9</integer>\n" +
                "\t\t\t\t</dict>\n" +
                "\t\t\t\t<dict>\n" +
                "\t\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t\t<integer>12</integer>\n" +
                "\t\t\t\t</dict>\n" +
                "\t\t\t\t<dict>\n" +
                "\t\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t\t<integer>14</integer>\n" +
                "\t\t\t\t</dict>\n" +
                "\t\t\t</array>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$class</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>11</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>channel</key>\n" +
                "\t\t\t<integer>0</integer>\n" +
                "\t\t\t<key>note</key>\n" +
                "\t\t\t<integer>0</integer>\n" +
                "\t\t\t<key>text</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>10</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>time</key>\n" +
                "\t\t\t<real>0.0</real>\n" +
                "\t\t\t<key>timeInQuarterNotes</key>\n" +
                "\t\t\t<real>0.0</real>\n" +
                "\t\t\t<key>type</key>\n" +
                "\t\t\t<integer>2</integer>\n" +
                "\t\t\t<key>velocity</key>\n" +
                "\t\t\t<integer>0</integer>\n" +
                "\t\t</dict>\n" +
                "\t\t<string>TITLE:" + song + "</string>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$classes</key>\n" +
                "\t\t\t<array>\n" +
                "\t\t\t\t<string>KBMidiEvent</string>\n" +
                "\t\t\t\t<string>NSObject</string>\n" +
                "\t\t\t</array>\n" +
                "\t\t\t<key>$classname</key>\n" +
                "\t\t\t<string>KBMidiEvent</string>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$class</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>11</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>channel</key>\n" +
                "\t\t\t<integer>0</integer>\n" +
                "\t\t\t<key>note</key>\n" +
                "\t\t\t<integer>0</integer>\n" +
                "\t\t\t<key>text</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>13</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>time</key>\n" +
                "\t\t\t<real>" + (audioDuration / 1.5) / 2 + "</real>\n" +
                "\t\t\t<key>timeInQuarterNotes</key>\n" +
                "\t\t\t<real>" + audioDuration / 2 +"</real>\n" +
                "\t\t\t<key>type</key>\n" +
                "\t\t\t<integer>2</integer>\n" +
                "\t\t\t<key>velocity</key>\n" +
                "\t\t\t<integer>0</integer>\n" +
                "\t\t</dict>\n" +
                "\t\t<string>ARTIST:" + artist + "</string>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$class</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>11</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>channel</key>\n" +
                "\t\t\t<integer>0</integer>\n" +
                "\t\t\t<key>note</key>\n" +
                "\t\t\t<integer>0</integer>\n" +
                "\t\t\t<key>text</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>15</integer>\n" +
                "\t\t\t</dict>" + 
                "\t\t\t<key>time</key>\n" +
                "\t\t\t<real>" + audioDuration / 1.5 + "</real>\n" +
                "\t\t\t<key>timeInQuarterNotes</key>\n" +
                "\t\t\t<real>" + audioDuration + "</real>\n" +
                "\t\t\t<key>type</key>\n" +
                "\t\t\t<integer>2</integer>\n" +
                "\t\t\t<key>velocity</key>\n" +
                "\t\t\t<integer>0</integer>\n" +
                "\t\t</dict>\n" +
                "\t\t<string>ID: 9592 1</string>\n" + 
                "\t\t<dict>\n" +
                "\t\t\t<key>$classes</key>\n" +
                "\t\t\t<array>\n" +
                "\t\t\t\t<string>NSMutableArray</string>\n" +
                "\t\t\t\t<string>NSArray</string>\n" +
                "\t\t\t\t<string>NSObject</string>\n" +
                "\t\t\t</array>\n" +
                "\t\t\t<key>$classname</key>\n" +
                "\t\t\t<string>NSMutableArray</string>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$classes</key>\n" +
                "\t\t\t<array>\n" +
                "\t\t\t\t<string>KBMidiTrack</string>\n" +
                "\t\t\t\t<string>NSObject</string>\n" +
                "\t\t\t</array>\n" +
                "\t\t\t<key>$classname</key>\n" +
                "\t\t\t<string>KBMidiTrack</string>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$class</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>17</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>events</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>19</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$class</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>16</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>NS.objects</key>\n" +
                "\t\t\t<array>\n");
            
            // Declare a variable for each note, starting at 20 and going to
            // ((note count) + 19).
            for(int i = 20; i <= (notes.size() + 19); i++) {
                writer.write("\t\t\t\t<dict>\n");
                writer.write("\t\t\t\t\t<key>CF$UID</key>\n");
                writer.write("\t\t\t\t\t<integer>" + i + "</integer>\n");
                writer.write("\t\t\t\t</dict>\n");
            }
            
            writer.write("\t\t\t</array>\n");
            writer.write("\t\t\t</dict>\n");
            
            // Write a chunk of data for each note
            for(int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                writer.write("\t\t<dict>\n" +
                    "\t\t\t<key>$class</key>\n" +
                    "\t\t\t<dict>\n" +
                    "\t\t\t\t<key>CF$UID</key>\n" +
                    "\t\t\t\t<integer>11</integer>\n" +
                    "\t\t\t</dict>\n" +
                    "\t\t\t<key>channel</key>\n" +
                    "\t\t\t<integer>1</integer>\n" +
                    "\t\t\t<key>note</key>\n" +
                    // The value of the note
                    "\t\t\t<integer>" + note.getNoteValue() + "</integer>\n" +
                    "\t\t\t<key>text</key>\n" +
                    "\t\t\t<dict>\n" +
                    "\t\t\t\t<key>CF$UID</key>\n" +
                    "\t\t\t\t<integer>0</integer>\n" +
                    "\t\t\t</dict>\n" +
                    "\t\t\t<key>time</key>\n" +
                    // The real time the note happens
                    "\t\t\t<real>" + note.getRealTime() + "</real>\n" +
                    "\t\t\t<key>timeInQuarterNotes</key>\n" +
                    // The MIDI tick time the note happens (120 BPM)
                    "\t\t\t<real>" + note.getQuarterTime() + "</real>\n" +
                    "\t\t\t<key>type</key>\n" +
                    // Write whether the note starts or ends
                    "\t\t\t<integer>" + note.getStartEnd() + "</integer>\n" +
                    "\t\t\t<key>velocity</key>\n");
                
                // A velocity value of 100 is set if the note started, and 0 if
                // the note ended. According to MIDI specification, this should
                // make the track actually output a MIDI sound, but I have not
                // observed this behavior in any track.
                if(note.getStartEnd() == '0')
                    writer.write("\t\t\t<integer>100</integer>\n");
                else
                    writer.write("\t\t\t<integer>0</integer>\n");
                    
                    writer.write("\t\t</dict>\n");
            }
            
            // Finish up the file
            writer.write("\t\t<data>\n" +
                "\t\t</data>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$class</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>16</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>NS.objects</key>\n" +
                "\t\t\t<array>\n" +
                "\t\t\t\t<dict>\n" +
                "\t\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t\t<integer>" + (notes.size() + 22) + "</integer>\n" +
                "\t\t\t\t</dict>\n" +
                "\t\t\t</array>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$class</key>\n" +
                "\t\t\t<dict>\n" +
                "\t\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t\t<integer>" + (notes.size() + 23) + "</integer>\n" +
                "\t\t\t</dict>\n" +
                "\t\t\t<key>startTime</key>\n" +
                "\t\t\t<real>0.0</real>\n" +
                "\t\t\t<key>tempo</key>\n" +
                "\t\t\t<real>120</real>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$classes</key>\n" +
                "\t\t\t<array>\n" +
                "\t\t\t\t<string>KBMidiTempoMap</string>\n" +
                "\t\t\t\t<string>NSObject</string>\n" +
                "\t\t\t</array>\n" +
                "\t\t\t<key>$classname</key>\n" +
                "\t\t\t<string>KBMidiTempoMap</string>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$classes</key>\n" +
                "\t\t\t<array>\n" +
                "\t\t\t\t<string>KBMidiFile</string>\n" +
                "\t\t\t\t<string>NSObject</string>\n" +
                "\t\t\t</array>\n" +
                "\t\t\t<key>$classname</key>\n" +
                "\t\t\t<string>KBMidiFile</string>\n" +
                "\t\t</dict>\n" +
                "\t\t<dict>\n" +
                "\t\t\t<key>$classes</key>\n" +
                "\t\t\t<array>\n" +
                "\t\t\t\t<string>NSMutableDictionary</string>\n" +
                "\t\t\t\t<string>NSDictionary</string>\n" +
                "\t\t\t\t<string>NSObject</string>\n" +
                "\t\t\t</array>\n" +
                "\t\t\t<key>$classname</key>\n" +
                "\t\t\t<string>NSMutableDictionary</string>\n" +
                "\t\t</dict>\n" +
                "\t</array>\n" +
                "\t<key>$top</key>\n" +
                "\t<dict>\n" +
                "\t<key>root</key>\n" +
                "\t<dict>\n" +
                "\t\t\t<key>CF$UID</key>\n" +
                "\t\t\t<integer>1</integer>\n" +
                "\t\t</dict>\n" +
                "\t</dict>\n" +
                "\t<key>$version</key>\n" +
                "\t<integer>100000</integer>\n" +
                "</dict>\n" +
                "</plist>");
            
            writer.flush();
            writer.close();
            
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Error creating taptrack file!",
                    "Error", JOptionPane.ERROR_MESSAGE);;
        }
    }
    
    /**
     * 
     * Calculate time per tick. This is used to convert from MIDI ticks to real time
     * If this method is expanded, it is possible to check for varying BPM
     * and calculate the real time according to BPM change.
     * 
     * @param eofFile The MIDI file specified by the user.
     * @param sequence The MIDI sequence for which the calculations are being made.
     * @return The real time per MIDI tick.
     * @exception IOException Caught if the file cannot be read.
     * 
     */   
    public static double getTimePerTick (File eofFile, Sequence sequence) {
        double BPM;
        int tempo;
        
        try {
            // Read the first 256 bytes of the MIDI. This should contain
            // the first BPM value.
            byte[] b = new byte[256];
            String[] byteStrings = new String [256];
            FileInputStream reader = new FileInputStream(eofFile);
            reader.read(b, 0, 256);
            for(int i = 0; i < 256; i++) {
                byteStrings[i] = Integer.toHexString(b[i]);
                if(byteStrings[i].contains("ffffff")) {
                    byteStrings[i] = byteStrings[i].substring(6, 8);
                }
                
                // Correct hex values per byte (2 hex per byte)
                if(byteStrings[i].length() == 1)
                    byteStrings[i] = "0" + byteStrings[i];               
            }
            
            // Read the byte array. Find the byte sequence FF 51 03. This data
            // is preceded by a byte that specified delta time. The delta time
            // is when this BPM change occurs, meaning it is possible to calculate
            // BPM changes later on. FF 51 signifies the MIDI is setting a tempo.
            // 03 says that the tempo value is in the next 3 bytes.
            for(int i = 0; i < 256; i++) {
                if(byteStrings[i].compareTo("ff") == 0) {
                    if(byteStrings[i+1].compareTo("51") == 0 && byteStrings[i+2].compareTo("03") == 0) {
                        String tempoStr = byteStrings[i+3] + byteStrings[i+4] + byteStrings[i+5];
                        // Convert the hex value into an integer
                        tempo = Integer.parseInt(tempoStr, 16);
                        BPM = 60000000.0 / tempo;
                        // The time per tick
                        return 60 / (BPM * sequence.getResolution());
                    }
                }
            }
            
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(Loading.loadingFrame, "Error calculating time per tick!",
                    "Error", JOptionPane.ERROR_MESSAGE);;
        }
        
        return -1;
    }
}
