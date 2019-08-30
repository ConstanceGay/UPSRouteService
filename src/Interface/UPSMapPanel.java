package Interface;

import Application.*;
import UPSRouteService.*;
import t2s.son.LecteurTexte;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import marytts.signalproc.effects.JetPilotEffect;
import marytts.signalproc.effects.LpcWhisperiserEffect;
import marytts.signalproc.effects.RobotiserEffect;
import marytts.signalproc.effects.StadiumEffect;
import marytts.signalproc.effects.VocalTractLinearScalerEffect;
import marytts.signalproc.effects.VolumeEffect;


public class UPSMapPanel extends JPanel implements MouseListener{

    private String imagePath = "/ups-map-9x6.png";
    private List<GPSPoint> coordinates;                                     //all the points of the path
    private UPSRouteService upsRouteService = new UPSRouteService();

    //Points to draw on the map
    private GPSPoint mobileStartPointToDraw;                                //Start of the path point
    private GPSPoint mobileEndPointToDraw;                                  //End of path point
    private GPSPoint explorationPoint;                                      //Exploration point
    private GPSPoint navigationPoint;                                       //point of navigation on the path

    private List<Integer> wayPointsToDraw = new LinkedList<>();             //points of the path selected by user
    private Path steps;                                                     //Navigation instructions

    //GPS coordinates of the corners of the map
    private GPSPoint gpsDownLeft;
    private GPSPoint gpsDownRight;
    private GPSPoint gpsUpLeft;

    private Coordinate mouseCoordinate = new Coordinate(0,0);         //position of the mouse on the map
    private String mouseBuilding = "";                                      //building selected by the mouse click
    private String explorationBuilding = "";                                //building selected by topcode

    private InstructionWindow instructionWindow;                            //Window to display route information
    private int distance;
    private int duration;

    private int scaleWidth;
    private int xOffset;

    //Variables for navigation mode with camera
    private boolean routeConfirmed = false;
    private int instructionNumber = 1;


    UPSMapPanel(Location start, Location end) {

        addMouseListener(this);
        loadGpsConfig();

        instructionWindow = new InstructionWindow(0,0,new Path());
        drawRoute(start, end);
        this.setBackground(Color.BLACK);
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        mouseCoordinate.setX(evt.getPoint().x);
        mouseCoordinate.setY(evt.getPoint().y);
        Vecteur2D mouse_coordinates = new Vecteur2D(mouseCoordinate.getX(),mouseCoordinate.getY(),gpsDownLeft,gpsDownRight,gpsUpLeft);
        Coordinate mouse_GPS = new Coordinate (mouse_coordinates.vue2gps().getX(),mouse_coordinates.vue2gps().getY());
        mouseBuilding = upsRouteService.getBuilding(mouse_GPS.getX(),mouse_GPS.getY());

        TextToSpeech tts = new TextToSpeech();
        tts.setVoice("upmc-pierre-hsmm");
        tts.speak(convertBuildingName(mouseBuilding), 2.0f, false, true);

        repaint();
    }

    void setProfile(Profile profile) {
        upsRouteService.setProfile(profile);
    }

    //Draws the route for a path selected with the application
    void drawRoute(Location start, Location end) {
        instructionWindow.setVisible(false);
        mobileStartPointToDraw = null;
        mobileEndPointToDraw = null;
        UPSRoute upsRoute = upsRouteService.getRoute(start, end);

        if (upsRoute != null) {
            steps = upsRoute.getSteps();
            coordinates = upsRoute.getCoordinates();
            distance = upsRoute.getDistance();
            duration = upsRoute.getDuration();
        }
        else
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver un itinéraire avec ces paramètres de navigation.",
                    "Impossible de trouver le chemin",
                    JOptionPane.ERROR_MESSAGE);
    }

    //Draws the route for a path selected using the camera and topcodes
    private void drawRoute(GPSPoint start, GPSPoint end) {
        UPSRoute upsRoute = upsRouteService.getRoute(start, end);

        if (upsRoute != null) {
            String startBuilding = upsRouteService.getBuilding(start.getLongitude(),start.getLatitude());
            String endBuilding = upsRouteService.getBuilding(end.getLongitude(),end.getLatitude());
            instructionWindow.refresh(upsRoute.getDistance(),upsRoute.getDuration(),upsRoute.getSteps(),startBuilding,endBuilding);
            instructionWindow.setVisible(true);
            steps = upsRoute.getSteps();
            coordinates = upsRoute.getCoordinates();
            distance = upsRoute.getDistance();
            duration = upsRoute.getDuration();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        try {
            BufferedImage image = ImageIO.read(getClass().getResource(imagePath));

            double scaleFactor = Math.min(1d, getScaleFactorToFit(new Dimension(image.getWidth(), image.getHeight()), getSize()));
            scaleWidth = (int) Math.round(image.getWidth() * scaleFactor);
            int scaleHeight = (int) Math.round(image.getHeight() * scaleFactor);

            Image scaled = image.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH);

            int width = getWidth() - 1;
            int height = getHeight() - 1;

            xOffset = (width - scaled.getWidth(this)) / 2;
            int y = (height - scaled.getHeight(this)) / 2;

            g.drawImage(scaled, xOffset, y, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mouseBuilding != null) {
            g.drawString(mouseBuilding, (int) mouseCoordinate.getX(), (int) mouseCoordinate.getY());
        }

        //Draws the first point of the path in BLUE
        g2.setStroke(new BasicStroke(2));
        g.setColor(Color.BLUE);
        g.drawOval(coordinates.get(0).getGraphicsPoint().getCol() - 2 + xOffset, coordinates.get(0).getGraphicsPoint().getRow() - 2, 4, 4);

        for (int i = 0; i < coordinates.size() - 1; i++) {
            if (wayPointsToDraw.contains(i))
                //if this part of the path is selected it'll be in orange
                g2.setColor(Color.ORANGE);
            else
                g2.setColor(Color.RED);

            g2.drawLine(coordinates.get(i).getGraphicsPoint().getCol() + xOffset, coordinates.get(i).getGraphicsPoint().getRow(),
                    coordinates.get(i + 1).getGraphicsPoint().getCol() + xOffset, coordinates.get(i + 1).getGraphicsPoint().getRow());
        }

        //NEXT STEP
        if (routeConfirmed){
            g2.setStroke(new BasicStroke(2));
            g.setColor(Color.BLACK);
            GPSPoint nextPoint = coordinates.get(steps.getInstructions().get(instructionNumber).getWayPoints().get(0));
            g.drawOval(nextPoint.getGraphicsPoint().getCol() - 2 + xOffset, nextPoint.getGraphicsPoint().getRow() - 2, 4, 4);
        }

        //draws the end point of the path in BLUE
        g.setColor(Color.BLUE);
        g.drawOval(coordinates.get(coordinates.size() - 1).getGraphicsPoint().getCol() - 2 + xOffset, coordinates.get(coordinates.size() - 1).getGraphicsPoint().getRow() - 2, 4, 4);

        //draws the topcode starting point in GREEN
        if (mobileStartPointToDraw != null) {
            g.setColor(Color.GREEN);
            g.fillOval(mobileStartPointToDraw.getGraphicsPoint().getCol() + xOffset - 5, mobileStartPointToDraw.getGraphicsPoint().getRow() - 5, 10, 10);
        }

        if (navigationPoint != null){
            g.setColor(Color.BLACK);
            g.fillOval(navigationPoint.getGraphicsPoint().getCol() + xOffset - 5, navigationPoint.getGraphicsPoint().getRow() - 5, 10, 10);
        }

        //draws the exploration point in YELLOW
        if (explorationPoint != null){
            g.setColor(Color.YELLOW);
            g.fillOval(explorationPoint.getGraphicsPoint().getCol() + xOffset - 5, explorationPoint.getGraphicsPoint().getRow() - 5, 10, 10);
            if (explorationBuilding != null){
                g.drawString(explorationBuilding, explorationPoint.getGraphicsPoint().getCol() + xOffset - 5, explorationPoint.getGraphicsPoint().getRow() - 5);
            }
        }

        //Draws the topcode endpoint in red
        if (mobileEndPointToDraw != null) {
            g.setColor(Color.RED);
            g.fillOval(mobileEndPointToDraw.getGraphicsPoint().getCol() + xOffset - 5, mobileEndPointToDraw.getGraphicsPoint().getRow() - 5, 10, 10);
        }
    }

    //Gets the GPS configuration from the files
    private void loadGpsConfig() {
        try {

            //FileInputStream fileInputStream = new FileInputStream("res/gpsUpLeft.ser");
            InputStream inputStream = getClass().getResourceAsStream("/gpsUpLeft.ser");

            ObjectInputStream in = new ObjectInputStream(inputStream);
            gpsUpLeft = (GPSPoint)in.readObject();

            //fileInputStream = new FileInputStream("res/gpsDownLeft.ser");
            inputStream = getClass().getResourceAsStream("/gpsDownLeft.ser");

            in = new ObjectInputStream(inputStream);
            gpsDownLeft = (GPSPoint)in.readObject();

            //fileInputStream = new FileInputStream("res/gpsDownRight.ser");
            inputStream = getClass().getResourceAsStream("/gpsDownRight.ser");

            in = new ObjectInputStream(inputStream);
            gpsDownRight = (GPSPoint) in.readObject();

            in.close();
            inputStream.close();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            gpsUpLeft = new GPSPoint(1.468209, 43.566174);
            gpsDownLeft = new GPSPoint(1.461643, 43.562777);
            gpsDownRight = new GPSPoint(1.468724, 43.555375);
            e.printStackTrace();
        }

    }

    //FUNCTIONS TO SET THE DIFFERENT POINTS

    void setMobileStartPointToDraw(GPSPoint gpsPoint) {
        //If we are in navigation mode and the route has been set
        if (routeConfirmed){
            if(navigationPoint != null){
                Boolean isDif = (gpsPoint.getLongitude() != navigationPoint.getLongitude()) || (gpsPoint.getLatitude() != navigationPoint.getLatitude());
                if (isDif) {
                    navigationPoint = gpsPoint;
                    int closestPoint = closestPathPoint(gpsPoint);
                    String instruction = instructionToSay(gpsPoint, closestPoint);
                    if (!instruction.equals("")){
                        TextToSpeech tts = new TextToSpeech();
                        tts.setVoice("upmc-pierre-hsmm");
                        tts.speak(instruction, 2.0f, false, true);
                        System.out.println(instruction);
                    }
                }
            } else{
                navigationPoint = gpsPoint;
            }

        } else if (mobileStartPointToDraw != null) {
            Boolean isDif = (gpsPoint.getLongitude() != mobileStartPointToDraw.getLongitude()) || (gpsPoint.getLatitude() != mobileStartPointToDraw.getLatitude());
            if (isDif) {
                mobileStartPointToDraw = gpsPoint;
                if (mobileEndPointToDraw != null) {
                    drawRoute(mobileStartPointToDraw, mobileEndPointToDraw);
                }
            }
        } else{
            mobileStartPointToDraw = gpsPoint;
        }
    }

    void setMobileEndPointToDraw(GPSPoint gpsPoint) {
        if(mobileEndPointToDraw != null && !routeConfirmed) {
            Boolean isDif = (gpsPoint.getLongitude() != mobileEndPointToDraw.getLongitude()) || (gpsPoint.getLatitude() != mobileEndPointToDraw.getLatitude());
            if (isDif) {
                mobileEndPointToDraw = gpsPoint;
                if (mobileStartPointToDraw != null) {
                    drawRoute(mobileStartPointToDraw, mobileEndPointToDraw);
                }
            }
        } else{
            mobileEndPointToDraw = gpsPoint;
        }
    }

    //Draws the exploration point on the map and gets the name of the building
    void setExplorationPoint (GPSPoint gpsPoint){
        explorationPoint = gpsPoint;
        String new_building = upsRouteService.getBuilding(explorationPoint.getLongitude(),explorationPoint.getLatitude());
        if (!explorationBuilding.equals(new_building)){
            explorationBuilding = new_building;
            //Reads building name out loud
            TextToSpeech tts = new TextToSpeech();
            tts.setVoice("upmc-pierre-hsmm");
            tts.speak(convertBuildingName(explorationBuilding), 2.0f, false, true);
        }
    }

    boolean setRouteConfirm (){
        if ( !(mobileEndPointToDraw==null) && !(mobileStartPointToDraw==null) ){
            TextToSpeech tts = new TextToSpeech();
            tts.setVoice("upmc-pierre-hsmm");
            tts.speak(steps.getInstructions().get(0).toString(), 2.0f, false, true);
            this.routeConfirmed = true;
            return true;
        } else{
            return false;
        }
    }

    void resetRouteConfirm (){
        this.routeConfirmed = false;
        instructionNumber = 1;
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::                          NAVIGATION                            :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

    int closestPathPoint(GPSPoint curPoint){
        double min = 1200;                                  //used the max distance on map
        int pointNumber = -1;

        for (int i=0; i<coordinates.size();i++){
            GPSPoint aux = coordinates.get(i);
            double distance = curPoint.distanceGPSPoint(aux);
            if (distance <= min){
                min = distance;
                pointNumber = i;
            }
        }
        return pointNumber;
    }

    String instructionToSay (GPSPoint curPoint,int closestPoint){
        String instruction = "";

        if (curPoint.distanceGPSPoint(coordinates.get(closestPoint)) >= 20){
            instruction = "Faites demi-tour";
        } else if (closestPoint == coordinates.size()-1  ){                    //if it is the last point
            instruction = "Vous êtes arrivé";
            resetRouteConfirm();
        } else {
            //we find out if the point is the first of an instruction
            List<Instruction> instruList = steps.getInstructions();
            ListIterator<Instruction> it = instruList.listIterator();
            int instruNum = -1;
            boolean trouve = false;
            Instruction aux;
            while(it.hasNext() && !trouve){
                aux = it.next();
                if (aux.getWayPoints().get(0) == closestPoint){
                    instruNum = instruList.indexOf(aux);
                    trouve = true;
            }
            }

            if(trouve && instruNum == instructionNumber){
                instructionNumber ++;
                instruction = instruList.get(instruNum).toString();
                if (instruction == null){
                    instruction = "";
                    //TODO
                }
            }
        }
        return instruction;
    }

    //FUNCTION TO CHANGE THE IMAGE ON THE MAP WINDOW (MAP OR BLACK)
    void setImage(String imagePath){ this.imagePath = imagePath; }

    //FUNCTION TO CHANGE THE NAME OF A BUILDING SO IT WILL BE PRONOUNCED RIGHT BY MARY TTS
    String convertBuildingName (String name){
        String convName = name;
        if(!name.equals("IRIT")) {
            convName="";
            String[] words = name.split(" ");
            for(int i=0;i<=words.length-1;i++){
                String curWord = words[i];
                //if the word only has capital letters,numbers and parentheses then
                if(words[i].matches("^[\\(]?[A-Z0-9]*[\\)]?$")){
                    //adds spaces in between
                    curWord = curWord.replaceAll(".(?=.)", "$0 ");
                }
                convName = convName.concat(" "+curWord);
            }
        }
        return convName;
    }

    //FUNCTIONS TO SET THE SELECTED POINTS (in the instructions list
    void addAllWayPointsToDraw(List<Integer> points) {
        wayPointsToDraw.addAll(points);
    }

    void clearWayPointsToDraw() {
        wayPointsToDraw.clear();
    }

    //Methods that have to be implemented with this interface
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public GPSPoint getGpsDownLeft() {return gpsDownLeft; }
    public GPSPoint getGpsDownRight() { return gpsDownRight; }
    public GPSPoint getGpsUpLeft() { return gpsUpLeft; }

    Path getSteps() { return steps; }

    int getDuration() { return duration; }

    int getDistance(){ return distance;}

    private double getScaleFactorToFit(Dimension original, Dimension toFit) {
        double dScale = 1d;

        if (original != null && toFit != null) {

            double dScaleWidth = getScaleFactor(original.width, toFit.width);
            double dScaleHeight = getScaleFactor(original.height, toFit.height);

            dScale = Math.min(dScaleHeight, dScaleWidth);
        }

        return dScale;
    }

    private double getScaleFactor(int iMasterSize, int iTargetSize) {
        double dScale;
        if (iMasterSize > iTargetSize) {
            dScale = (double)iTargetSize / (double)iMasterSize;
        } else {
            dScale = (double)iTargetSize / (double)iMasterSize;
        }

        return dScale;
    }

    public int getScaleWidth() {
        return scaleWidth;
    }
}

