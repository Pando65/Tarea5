package tarea5;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.net.URL;
                                                        
public class SoundClip { 
                                                       
        private AudioInputStream sample;
        private Clip clip; 
        private boolean looping = false;
        private int repeat = 0;
        private String filename = "";
//El siguiente método es el constructor vacio, crea el objeto SoundClip con el buffer de sonido.

        public SoundClip() { 
                                                       
                try {  
                                                      
                      clip = AudioSystem.getClip();
                }catch (LineUnavailableException e) {  
                                                      
                      System.out.println("Error en " + e.toString());
                }
        }
//Además se usa un constructor con parámetros que lo que hace es, manda llamar al constructor default y carga el archivo de sonido del nombre del archivo de sonido dado como parámetro.

        public SoundClip(String filename) { 
                                                       
                this();
                load(filename);
        }
//Ahora tenemos los métodos modificadores, usados para cambiar los valores de nuestro objeto SoundClip.

        public void setLooping(boolean looping) {

                this.looping = looping; 
        }           
                                             
        public void setRepeat(int repeat) {

                this.repeat = repeat;
        }  
                                                      
        public void setFilename(String filename) {

                this.filename = filename; 
        }
//Los métodos de acceso son usados para obtener los valores del objeto SoundClip.

        public Clip getClip() { 
                                                       
                return clip; 
        }
                                                   
        public boolean getLooping() { 
                                                       
                return looping;
        }
                                                      
        public int getRepeat() {
                                                        
                return repeat; 
        }
                                                     
        public String getFilename() { 
                                                       
                return filename;
        }
                                                    
        private URL getURL(String filename) {
                                                       
                URL url = null;
                try {
                                                     
                      url = this.getClass().getResource(filename);
                }catch (Exception e) {
                                                       
                      System.out.println("Error en " + e.toString());
                }
                return url;
        }
//Para verificar si el archivo esta cargado o no, usamos el método isLoaded.

        public boolean isLoaded() {
                                                        
                return (boolean)(sample != null);
        }
//El método load nos sirve para poder cargar el archivo de audio, recibe como parámetro un String con el nombre del archivo.

        public boolean load(String audiofile) { 
                                                       
                try {

                      setFilename(audiofile);
                      sample = AudioSystem.getAudioInputStream(getURL(filename)); 
                      clip.open(sample); 
                      return true;
                } catch (IOException e) { 
                                                       
                      System.out.println("Error en " + e.toString());
                      return false;
                }catch (UnsupportedAudioFileException e) {
                                                        
                      System.out.println("Error en " + e.toString());
                      return false;
                }catch (LineUnavailableException e) {
                                                      
                      System.out.println("Error en " + e.toString());
                      return false;
                }
        }
//Para reproducir el archivo de sonido utilizamos el método play, que se encarga de verificar si el archivo ha sido cargado o no y además si el archivo debe reproducirse en forma continua.

        public void play() { 
                                                       
                if (!isLoaded()) 
                    return;
                                                        
                clip.setFramePosition(0);
                                                     
                if (looping) 
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                else 
                    clip.loop(repeat);
        }
//El método stop se encarga simplemente de parar la reproducción del sonido.

        public void stop() { 
                                                       
                clip.stop();
        }
}
