package tarea5;

/**
 * 1er Examen - Proyecto de Desarrollo de Videojuegos
 *
 * Juego donde Juanito debe atrapar los Chimpys y huir de los Diddys
 *
 * @author Omar Manjarrez & Jose Manuel Gonzalez 
 * @matriculas A008XXXXX & A01280106
 * @version 1.0
 * @date 11/02/15
 */ 
 
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import static java.awt.Color.yellow;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author AntonioM
 */
public class Juego extends JFrame implements Runnable, KeyListener {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basPrincipal;         // Objeto principal
    //private Base basMalo;         // Objeto malo
    private LinkedList<Base> lklChimpys; //coleccion de chimpys;
    private LinkedList<Base> lklDiddys; //coleccion de Diddys
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image imaImagenApplet;   // Imagen a proyectar en Applet	
    private Image imaGameover; //imagen a mostrar cuando se pierde
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private SoundClip socSonidoChimpy;   // Objeto sonido de Chimpy
    private SoundClip socSonidoDiddy; //objeto sonido de Diddy
    private URL urlImagenChimpy;
    private URL urlImagenDiddy;
    private URL urlImagenPrincipal;
    
    private int iDir; //Direccion del objeto principal
    private int iAceleracion; //Aceleracion de los enemigos
    private int iScore; //puntaje del jugador
    private int iVidas; //vidas del jugador
    private boolean bTecla; //Bandera que indica si se presiono una flecha
    private boolean bPausa; //indica si el juego está en pausa
    private boolean bGameover; //indica si ya se perdio el juego o no
    
    public Juego() {
        init();
        start();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
    public void grabaArchivoVidasScore() throws IOException {
         PrintWriter prwFileOut = new PrintWriter
                (new FileWriter("VidasScore.txt"));
         prwFileOut.println(Integer.toString(iVidas));
         prwFileOut.println(Integer.toString(iScore));
         prwFileOut.println(Integer.toString(iAceleracion));
         prwFileOut.println(Boolean.toString(bPausa));
         prwFileOut.close();
    }
    
    public void grabaArchivoPosiciones() throws IOException {
         PrintWriter prwFileOut = new PrintWriter
                (new FileWriter("Posiciones.txt"));
         prwFileOut.println(Integer.toString(lklChimpys.size()));
         for (int iI = 0; iI < lklChimpys.size(); iI ++) {
             prwFileOut.println(Integer.toString(lklChimpys.get(iI).getX()));
             prwFileOut.println(Integer.toString(lklChimpys.get(iI).getY()));
         }
         prwFileOut.println(Integer.toString(lklDiddys.size()));
         for (int iI = 0; iI < lklDiddys.size(); iI ++) {
             prwFileOut.println(Integer.toString(lklDiddys.get(iI).getX()));
             prwFileOut.println(Integer.toString(lklDiddys.get(iI).getY()));
         }
         prwFileOut.println(Integer.toString(basPrincipal.getX()));
         prwFileOut.println(Integer.toString(basPrincipal.getY()));
         
         prwFileOut.close();
    }
    
    public void leeArchivoVidasScore() throws IOException {

        BufferedReader fileIn;
        try {
                fileIn = new BufferedReader(new FileReader("VidasScore.txt"));
        } catch (FileNotFoundException e){
                File filVidasScore = new File("VidasScore.txt");
                PrintWriter prwFileOut = new PrintWriter(filVidasScore);
                prwFileOut.println("4");
                prwFileOut.println("0");
                prwFileOut.println("1");
                prwFileOut.close();
                fileIn = new BufferedReader(new FileReader("VidasScore.txt"));
        }
        String sVidas = fileIn.readLine();
        String sScore = fileIn.readLine();
        String sAceleracion = fileIn.readLine();
        String sPausa = fileIn.readLine();
        iVidas = Integer.parseInt(sVidas);
        iScore = Integer.parseInt(sScore);
        iAceleracion = Integer.parseInt(sAceleracion);
        bPausa = Boolean.parseBoolean(sPausa);
        fileIn.close();
    }

    public void leePosiciones() throws IOException {

        BufferedReader fileIn;
        try {
                fileIn = new BufferedReader(new FileReader("Posiciones.txt"));
        } catch (FileNotFoundException e){
                File filVidasScore = new File("Posiciones.txt");
                PrintWriter prwFileOut = new PrintWriter(filVidasScore);
                prwFileOut.println("0");
                prwFileOut.println("0");
                prwFileOut.println("300");
                prwFileOut.println("300");
                prwFileOut.close();
                fileIn = new BufferedReader(new FileReader("Posiciones.txt"));
        }
        String sLimite = fileIn.readLine();
        lklChimpys.clear();
        int iPosicionX, iPosicionY;
        for(int iI = 0; iI < Integer.parseInt(sLimite); iI ++) {
            iPosicionX = Integer.parseInt(fileIn.readLine());
            iPosicionY = Integer.parseInt(fileIn.readLine());
            Base basChimpy = new Base(iPosicionX,iPosicionY, getWidth() 
                    / iMAXANCHO, getHeight() / iMAXALTO,
                    Toolkit.getDefaultToolkit().getImage(urlImagenChimpy));
            lklChimpys.add(basChimpy);
        }
        sLimite = fileIn.readLine();
        lklDiddys.clear();
        for(int iI = 0; iI < Integer.parseInt(sLimite); iI ++) {
            iPosicionX = Integer.parseInt(fileIn.readLine());
            iPosicionY = Integer.parseInt(fileIn.readLine());
            Base basDiddy = new Base(iPosicionX,iPosicionY, getWidth() 
                    / iMAXANCHO, getHeight() / iMAXALTO,
                    Toolkit.getDefaultToolkit().getImage(urlImagenDiddy));
            lklDiddys.add(basDiddy);
        }
        iPosicionX = Integer.parseInt(fileIn.readLine());
        iPosicionY = Integer.parseInt(fileIn.readLine());
        basPrincipal = new Base(iPosicionX, iPosicionY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));
        
        fileIn.close();
    }
    
    
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
        // hago el applet de un tamaño 500,500
        setSize(800,500);
             
	urlImagenPrincipal = this.getClass().getResource("juanito.gif");
                
        // se crea el objeto para principal 
	basPrincipal = new Base(0, 0, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        // se posiciona a principal  en la esquina superior izquierda del Applet 
        basPrincipal.setX(getWidth() / 2);
        basPrincipal.setY(getHeight() / 2);
        
        // defino imagenes
	urlImagenChimpy = this.getClass().getResource("chimpy.gif");
        urlImagenDiddy = this.getClass().getResource("diddy.gif");
        URL urlImagenGameover = this.getClass().getResource("gameover.png");
        imaGameover = Toolkit.getDefaultToolkit().getImage(urlImagenGameover);

        //Instancio las linked list
        lklChimpys = new LinkedList();
        lklDiddys = new LinkedList();
        
        //creo variable para generar numeros random
        Random ranAleatorio = new Random();
        
        //creo cantidades de personajes
        int iChimpys = ranAleatorio.nextInt(5) + 3;
        int iDiddys = ranAleatorio.nextInt(5) + 3;
        
        //pongo valores iniciales de mis variables
        iAceleracion = 1;
        iScore = 0;
        bPausa = false;
        bGameover = false;
        
        //creo los iChimpys
        for(int iI = 0; iI < iChimpys; iI ++) {
            int iPosX = ranAleatorio.nextInt(getWidth()) + getWidth();
            int iPosY = ranAleatorio.nextInt(iMAXALTO) * getHeight() / iMAXALTO;
            Base basChimpy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenChimpy));
            lklChimpys.add(basChimpy);
        }
        
        //creo los iDiddys
        for(int iI = 0; iI < iDiddys; iI ++) {
            int iPosX = ranAleatorio.nextInt(getWidth()) - getWidth();
            int iPosY = ranAleatorio.nextInt(iMAXALTO) * getHeight() / iMAXALTO;
            Base basDiddy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenDiddy));
            lklDiddys.add(basDiddy);
        }        
        
        //Coloco cantidad inicial de vidas
        iVidas = ranAleatorio.nextInt(2) + 4;
    
        socSonidoChimpy = new SoundClip("monkey1.wav");
        socSonidoDiddy = new SoundClip("monkey2.wav");
        
        socSonidoChimpy.setLooping(true);
        socSonidoDiddy.setLooping(true);
        
        //habilito para escuchar teclado
        addKeyListener(this);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (!bGameover) {
            if(!bPausa) {
                actualiza();
                checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
        repaint();
        
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){
        //pregunto si se presiono una tecla de dirección
        if(bTecla) {
            int iX = basPrincipal.getX();
            int iY = basPrincipal.getY();
            if(iDir == 1) {
                iY -= getHeight() / iMAXALTO;
            }
            if(iDir == 2) {
                iX += getWidth() / iMAXANCHO;
            }
            if(iDir == 3) {
                iY += getHeight() / iMAXALTO;
            }
            if(iDir == 4) {
                iX -= getWidth() / iMAXANCHO;
            }
            //limpio la bandera
            bTecla = false;
            
            //asigno posiciones en caso de no salirme
            if(iX >= 0 && iY >= 0 && iX + basPrincipal.getAncho() <= getWidth()
                    && iY + basPrincipal.getAlto() <= getHeight()) {
                basPrincipal.setX(iX);
                basPrincipal.setY(iY);
            }       
        }
        
        //Muevo los chimpys
        for(Base basChimpy : lklChimpys) {
            basChimpy.setX(basChimpy.getX() - iAceleracion);
        }
        
        //Muevo los diddys
        for(Base basDiddy : lklDiddys) {
            basDiddy.setX(basDiddy.getX() + iAceleracion);
        }

    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        //checo si hubo colision con Chimpy
        for(Base basChimpy : lklChimpys) {
            //con el principal
            if(basChimpy.intersecta(basPrincipal)) {
                //aumeto score
                iScore += 10;
                
                //genero nueva posicion del Chimpy y la asigno
                Random ranAleatorio = new Random();
                int iPosX = ranAleatorio.nextInt(getWidth()) + getWidth();
                int iPosY = ranAleatorio.nextInt(iMAXALTO) * getHeight()
                        / iMAXALTO;                
                basChimpy.setX(iPosX);
                basChimpy.setY(iPosY);
                
                //reproduzco sonido
                socSonidoChimpy.play();
                
            }
            
            //con la pared
            if(basChimpy.getX() <= 0) {
                //genero nueva posicion del Chimpy y la asigno
                Random ranAleatorio = new Random();
                int iPosX = ranAleatorio.nextInt(getWidth()) + getWidth();
                int iPosY = ranAleatorio.nextInt(iMAXALTO) * getHeight()
                        / iMAXALTO;                
                basChimpy.setX(iPosX);
                basChimpy.setY(iPosY);                
            }
        }
        
        //checo si hubo colision con diddy
        for(Base basDiddy : lklDiddys) {
            //con el principal
            if(basDiddy.intersecta(basPrincipal)) {
                //reproduzco sonido
                socSonidoDiddy.play();
                
                //quito una vida y aumento aceleracion
                iVidas --;
                iAceleracion ++;
                
                //si ya no tengo vidas activo la bandera de gameover
                if(iVidas <= 0) {
                    bGameover = true;
                }
                
                //genero nueva posicion del Diddy y la asigno
                Random ranAleatorio = new Random();
                int iPosX = ranAleatorio.nextInt(getWidth()) - getWidth();
                int iPosY = ranAleatorio.nextInt(iMAXALTO) * getHeight()
                        / iMAXALTO;                
                basDiddy.setX(iPosX);
                basDiddy.setY(iPosY);
            }
            
            //con pared
            if(basDiddy.getX() + basDiddy.getAncho() >= getWidth()) {
                //genero nueva posicion del Diddy y la asigno
                Random ranAleatorio = new Random();
                int iPosX = ranAleatorio.nextInt(getWidth()) - getWidth();
                int iPosY = ranAleatorio.nextInt(iMAXALTO) * getHeight()
                        / iMAXALTO;                
                basDiddy.setX(iPosX);
                basDiddy.setY(iPosY);                
            }
        }

    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint1(Graphics graDibujo) {
        // si la imagen ya se cargo
        if (basPrincipal != null) {
                //Dibuja la imagen de principal en el Applet
                basPrincipal.paint(graDibujo, this);
                
                //Dibujo los chimpys
                for(Base basChimpy : lklChimpys) {
                    basChimpy.paint(graDibujo, this);
                }

                //Dibujo los diddys
                for(Base basDiddy : lklDiddys) {
                    basDiddy.paint(graDibujo, this);
                }
                
                //Dibujo vidas y score
                graDibujo.setColor(yellow);
                graDibujo.drawString("Score: " + iScore, 5, getHeight() - 10);
                graDibujo.drawString("Vidas: " + iVidas, 5, getHeight() - 25);
                
                //en caso de estár en pausa dibujo letrero
                if(bPausa) {
                    graDibujo.drawString("P A U S A", getWidth() / 2 - 40, 
                            getHeight() / 2);
                }
                
                //en caso de perder despliego el game over
                if(bGameover) {
                    graDibujo.drawImage(imaGameover, getWidth() / 2 - 
                            imaGameover.getWidth(this) / 2, getHeight() / 2 -
                            imaGameover.getHeight(this) / 2, this);
                }
                
                
                
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
    }

    /**
     * keyTyped
     * 
     * Método sobreescrito de la interface keyListener. Se invoca despues de
     * presionar y soltar una tecla
     * 
     * @param keyEvent 
     */
    @Override
    public void keyTyped(KeyEvent keyEvent) {
        //Sin uso en ésta aplicación
    }

    /**
     * keyPressed
     * 
     * Método sobreescrito de la interface keyPressed. Se invoca despues de
     * presionar una tecla
     * 
     * @param keyEvent 
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        //Sin uso en ésta aplicación
    }

    /**
     * keyReleased
     * 
     * Método sobreescrito de la interface keyReleased. Se invoca despues de 
     * soltar una tecla
     * 
     * @param keyEvent 
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {        
        //pregunto cual tecla fue presionada
        if(keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            bTecla = true;
            iDir = 1;
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            bTecla = true;
            iDir = 3;
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            bTecla = true;
            iDir = 2;
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            bTecla = true;
            iDir = 4;
        }
        
        //pregunto si el jugador puso o quito pausa del juego
        if(keyEvent.getKeyCode() == 'P' && !bGameover) {
            bPausa = !bPausa;
        }
        
        //pregunto si el jugador quiso salir
        if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            bGameover = true;
        }
        
        //Preungto si el usuario quiere guardar los datos de la partida
        if(keyEvent.getKeyCode() == 'G') {
            // Si hay un error se detecta y se atrapa 
            try {
                grabaArchivoVidasScore();
                grabaArchivoPosiciones();
            } catch (IOException ex) {
                Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(keyEvent.getKeyCode() == 'C') {
            try {
                leeArchivoVidasScore();
                leePosiciones();
            } catch (IOException ex) {
                Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
}