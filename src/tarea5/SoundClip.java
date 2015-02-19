/**
 * SoundClip
 *
 * Clase que sirve para controlar sonido en un JFrame
 *
 * @author Antonio Mejorado
 * @version 1.0
 * @date 17/02/15
 */ 
package tarea5;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.net.URL;
                                                        
public class SoundClip { 
                                    
    private AudioInputStream aisSample;
    private Clip cliClip; 
    private boolean bLooping = false;
    private int iRepeat = 0;
    private String sFilename = "";
        
    /**
     * SoundClip
     * 
     * El siguiente método es el constructor vacio, crea el objeto SoundClip
     * con el buffer de sonido.
     */
    public SoundClip() { 
        try {  
            cliClip = AudioSystem.getClip();
        }catch (LineUnavailableException e) {  
            System.out.println("Error en " + e.toString());
        }
    }
    
    /**
     * SoundClip
     * 
     * Además se usa un constructor con parámetros que lo que hace es, manda
     * llamar al constructor default y carga el archivo de sonido del nombre 
     * del archivo de sonido dado como parámetro.
     * 
     * @param sFilename 
     */
    public SoundClip(String sFilename) { 
        this();
        load(sFilename);
    }

    /**
     * setLooping
     * 
     * Asigna el valor al atributo bLooping
     * 
     * @param bLooping 
     */
    public void setLooping(boolean bLooping) {
        this.bLooping = bLooping; 
    }           

    /**
     * setRepeat
     * 
     * Asigna el valor del atributo iRepeat
     * 
     * @param iRepeat El nuevo valor de iRepeat
     */
    public void setRepeat(int iRepeat) {
        this.iRepeat = iRepeat;
    }  

    /**
     * setFilename
     * 
     * Asigna el nombre del archivo
     * 
     * @param sFilename El nombre del archivo
     */
    public void setFilename(String sFilename) {
        this.sFilename = sFilename; 
    }
    
    /**
     * getClip
     * 
     * Regresa el clip actual
     * 
     * @return 
     */
    public Clip getClip() { 
        return cliClip; 
    }

    /**
     * getLooping
     * 
     * Regresa el estado de bLooping actual
     * 
     * @return 
     */
    public boolean getLooping() { 
        return bLooping;
    }

    /**
     * getRepeat
     * 
     * Regresa el valor de iRepeat actual
     * 
     * @return 
     */
    public int getRepeat() {
        return iRepeat; 
    }

    /**
     * getFilename
     * 
     * Regresa el valor de sFilename actual
     * 
     * @return 
     */
    public String getFilename() { 
        return sFilename;
    }

    /**
     * getURL
     * 
     * Regresa la URL de un nombre de archivo dado
     * 
     * @param sFilename el nombre del archivo
     * @return 
     */
    private URL getURL(String sFilename) {
        URL urlDireccion = null;
        try {
            urlDireccion = this.getClass().getResource(sFilename);
        }catch (Exception e) {
            System.out.println("Error en " + e.toString());
        }
        return urlDireccion;
    }
    
    /**
     * isLoaded
     * 
     * Para verificar si el archivo esta cargado o no, usamos el método
     * isLoaded.
     * 
     * @return regresa un booleano que indica si el archivo está cargado
     */
    public boolean isLoaded() {
        return (boolean)(aisSample != null);
    }
    
    /**
     * load
     * 
     * El método load nos sirve para poder cargar el archivo de audio, 
     * recibe como parámetro un String con el nombre del archivo.
     * 
     * @param audiofile el nombre del archivo
     * @return regresa un booleano para indicar si se cargo el archivo
     */
    public boolean load(String audiofile) { 
        try {
            setFilename(audiofile);
            aisSample = AudioSystem.getAudioInputStream(getURL(sFilename)); 
            cliClip.open(aisSample); 
            return true;
        } catch (IOException IOe) { 
            System.out.println("Error en " + IOe.toString());
            return false;
        }catch (UnsupportedAudioFileException IOe) {
            System.out.println("Error en " + IOe.toString());
            return false;
        }catch (LineUnavailableException IOe) {
            System.out.println("Error en " + IOe.toString());
            return false;
        }
    }
    
    /**
     * play
     * 
     * Para reproducir el archivo de sonido utilizamos el método play, que se 
     * encarga de verificar si el archivo ha sido cargado o no y además si el 
     * archivo debe reproducirse en forma continua.
     * 
     */
    public void play() { 
        if (!isLoaded()) 
            return;
        cliClip.setFramePosition(0);
        cliClip.loop(iRepeat);
    }

    /**
     * stop
     * 
     * El método stop se encarga simplemente de parar la reproducción 
     * del sonido.
     * 
     */
    public void stop() { 
        cliClip.stop();
    }
}
