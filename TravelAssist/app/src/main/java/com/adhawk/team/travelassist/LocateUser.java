package com.adhawk.team.travelassist;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AJ on 12/29/2016.
 */

public class LocateUser
{
    static ArrayList<Pair<Integer, Integer>> beacon_pos = new ArrayList<Pair<Integer, Integer>>();
    static ArrayList<Pair<Integer, Integer>> room_pos = new ArrayList<Pair<Integer, Integer>>();
    static ArrayList<Pair<Integer, Integer>> dest_pos = new ArrayList<Pair<Integer, Integer>>();
    static ArrayList<Pair<Integer, Integer>> obstacle_pos = new ArrayList<Pair<Integer, Integer>>();

    static int x_min;
    static int x_max;
    static int y_min;
    static int y_max;


    public LocateUser()
    {
        this.beacon_pos.add(new Pair<Integer, Integer>(4,3));
        this.beacon_pos.add(new Pair<Integer, Integer>(0,1));
        this.beacon_pos.add(new Pair<Integer, Integer>(0,6));

        this.room_pos.add(new Pair<Integer, Integer>(0,6));
        this.room_pos.add(new Pair<Integer, Integer>(4,6));
        this.room_pos.add(new Pair<Integer, Integer>(4,0));
        this.room_pos.add(new Pair<Integer, Integer>(0,0));


        this.dest_pos.add(new Pair<Integer, Integer>(0,1));
        this.dest_pos.add(new Pair<Integer, Integer>(4,3));

        x_min=room_pos.get(0).getFirst();
        for(int i=0; i<4; i++)
        {
            if(room_pos.get(i).getFirst() < x_min)
            {
                x_min = room_pos.get(i).getFirst();
            }
        }
        y_min=room_pos.get(0).getSecond();
        for(int i=0; i<4; i++)
        {
            if(room_pos.get(i).getSecond() < y_min)
            {
                y_min = room_pos.get(i).getSecond();
            }
        }
        x_max=room_pos.get(0).getFirst();
        for(int i=0; i<4; i++)
        {
            if(room_pos.get(i).getFirst() > x_max)
            {
                x_max = room_pos.get(i).getFirst();
            }
        }
        y_max=room_pos.get(0).getSecond();
        for(int i=0; i<4; i++)
        {
            if(room_pos.get(i).getSecond() > y_max)
            {
                y_max = room_pos.get(i).getFirst();
            }
        }
    }

    public static int getPos(String s)
    {
        switch (s)
        {
            case "11111": return 0;
            case "22222": return 1;
            case "33333": return 2;
            default: return 2;
        }
    }

    public static String getMajorFromName(String poi)
    {
        switch(poi)
        {
            case "Delicious Restaurant": return "11111";
            case "Gift Shop": return "22222";
            case "Security Check": return "33333";
            case "Check-in Counter":
            case "Utility Store":
            case "Boarding Counter": return "33333";
        }
        return "33333";
    }

    public static Pair<Integer, Integer> getLocation(String poi)
    {
        String major = getMajorFromName(poi);
        int index = getPos(major);
        Pair<Integer, Integer> p = beacon_pos.get(index);
        return p;
    }

    static String getNearbyPoi(String major)
    {
        switch (major)
        {
            case "11111": return "Delicious Restaurant";
            case "22222": return "Gift Shop";
            case "33333": return "Security Check";
            case "44444": return "Check-in Counter";
            default: return "55555";
        }
    }

    public static Pair<Integer, Integer> getUserLocation(ArrayList<Double> beacon_dist, ArrayList<String> major)  //List of user distance from every beacon
    {
        //return new Pair<Integer, Integer>((int)(Math.random()*24),(int)(Math.random()*24));
        double fac1 = 1.5,fac2 = 1.5, fac3 = 1.5, fac4 = 1;
        Pair<Integer, Integer> ans;
        //Uncomment this code for working beacons
        double p1, p2, q1, q2; //Point of intersection of two circles
        double d, l, h, x1 = -1, x2 = -1, y1=-1, y2= -1, x3 = -1, y3=-1;
        int found = 0;
        for(int i=0; i<3; i++)
        {
            if(beacon_dist.get(i) != -1)
                found++;
        }
        if(found < 2)
        {
            ans =  new Pair<Integer, Integer>(-1000, -1000);
        }
        else
        {
            for(int i=0; i<beacon_dist.size(); i++)
            {
                switch(major.get(i)) {
                    case "11111":
                        beacon_dist.set(i, beacon_dist.get(i) * 1.2);
                        break;

                    case "22222":
                        beacon_dist.set(i, beacon_dist.get(i) * 1.2);
                        break;

                    case "33333":
                        beacon_dist.set(i, beacon_dist.get(i) * 1.2);
                        break;

                }
                int j =getPos(major.get(i));
                if(j == -1)
                {
                    continue;
                }
                if(beacon_dist.get(i) != -1) {
                    if (x1 == -1) {
                        x1 = beacon_pos.get(getPos(major.get(i))).getFirst();
                        y1 = beacon_pos.get(getPos(major.get(i))).getSecond();
                    } else if (x2 == -1) {
                        x2 = beacon_pos.get(getPos(major.get(i))).getFirst();
                        y2 = beacon_pos.get(getPos(major.get(i))).getSecond();
                    } else {
                        x3 = beacon_pos.get(j).getFirst();
                        y3 = beacon_pos.get(j).getSecond();
                    }
                }
            }
            d = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
            l = (Math.pow(beacon_dist.get(0),2) - Math.pow(beacon_dist.get(1),2) + d*d)/(2*d);
            h = Math.sqrt(Math.pow(beacon_dist.get(0),2) - l*l);

            p1 = (l/d)*(x2-x1) + (h/d)*(y2-y1) + x1;
            p2 = (l/d)*(x2-x1) - (h/d)*(y2-y1) + x1;
            q1 = (l/d)*(y2-y1) - (h/d)*(x2-x1) + y1;
            q2 = (l/d)*(y2-y1) + (h/d)*(x2-x1) + y1;

            if(found == 2)
            {
                int x = (int)(p1+p2/2), y = (int)((q1+q2)/2);
                ans =  new Pair<Integer, Integer>(x, y);
            }
            else
            {
                double d1, d2;
                d1 = Math.sqrt(Math.pow((p1-x3),2) + Math.pow((q1-y3),2));
                d2 = Math.sqrt(Math.pow((p2-x3),2) + Math.pow((q2-y3),2));
                if(Math.abs(beacon_dist.get(2) - d1) > Math.abs(beacon_dist.get(2) - d2))
                {
                    ans = new Pair<Integer, Integer>((int)p2, (int)q2);
                }
                else
                {
                    ans =  new Pair<Integer, Integer>((int)p1, (int)q1);
                }
            }


        }
        int min_i = 0;
        if(ans.getFirst() == 0 && ans.getSecond() == 0) //Invalid User Location
        {

            double min_dist = beacon_dist.get(0);
            for(int i=0; i<3; i++)
            {
                if(beacon_dist.get(i) < min_dist)
                {
                    min_i = i;
                }
            }

            double add_value = beacon_dist.get(min_i)/1.414;

            double updated_x = beacon_pos.get(min_i).getFirst()+add_value;
            double updated_y = beacon_pos.get(min_i).getSecond()+add_value;
            if(updated_x < x_min || updated_x > x_max)
            {
                updated_x -= 2*add_value;
            }
            if(updated_y < y_min || updated_y > y_max)
            {
                updated_y -= 2*add_value;
            }
            //System.out.println("\nDewesh Deo Singh");
            ans = new Pair<Integer, Integer>((int)updated_x, (int)updated_y);
        }
        return ans;
    }

    private boolean isEqual(double a, double b)
    {
        if(Math.abs(a-b) < 0.1)
            return true;
        else return false;
    }

    public Pair<Double, ArrayList<String>> getDestinationInfo(Pair<Integer, Integer> user_loc, Pair<Integer, Integer> destination)
    {
        double slope;
        String instruction;
        ArrayList<String> directions = new ArrayList<String>();
        int x1 = user_loc.getFirst(), x2 = destination.getFirst(), y1 = user_loc.getSecond(), y2 = destination.getSecond();
        double dist = Math.sqrt(Math.pow((x2-x1),2) - Math.pow((y2-y1),2));
        if(user_loc.getFirst()  == destination.getFirst())
        {
            slope = -1000;
            if(y2 < y1)
            {
                instruction = "Move 45 Degrees in South West Direction";
                //instruction = "Move in East Direction";
                //move in 45* south west
            }
            else
            {
                instruction = "Move 45 degrees in North East Direction";
                //instruction = "Move in West Direction";
                //move in 45* north east
            }
        }
        else
        {
            slope = (destination.getSecond() - user_loc.getSecond())/ (destination.getFirst() - user_loc.getFirst());
            double angle = Math.toDegrees(Math.atan(slope));
            if(slope < 0)
            {
                if(y2 > y1 && x2 < x1)
                {
                    //instruction = "Move " + Math.abs(angle) + " degrees in North East Direction ";
                    angle = Math.abs(angle);
                    if(isEqual(angle, 45) == true)
                    {
                        instruction = "Move in East Direction"; //South
                    }
                    else if(angle > 45)
                    {
                        angle-=45;
                        instruction = "Move " + Math.abs(45-angle)+" degrees in North East Direction";
                    }
                    else
                    {
                        instruction = "Move " + Math.abs(angle+45) + " degrees in South East Direction";
                    }
                    //deg = Math.abs(angle)   if(deg==45)  south
                    //if(deg>45){ deg-=45
                    //      move deg in south west direction
                    // }
                    // else{  move 45-deg  in south east direction
                    //}

                }
                else
                {
                    //instruction = "Move " + Math.abs(angle) + " degrees in South West Direction";
                    angle = Math.abs(angle);
                    if(isEqual(angle, 45) == true)
                    {
                        instruction = "Move in West Direction";
                    }
                    else if(angle > 45)
                    {
                        angle-=45;
                        instruction = "Move " + Math.abs(90-angle)+" degrees in South West Direction";
                    }
                    else
                    {
                        instruction = "Move " + Math.abs(45+angle) + " degrees in North West Direction";
                    }
                    //deg = Math.abs(angle)   if(deg==45)  north
                    //if(deg>45){ deg-=45
                    //      move deg in north east direction
                    // }
                    // else{  move 45-deg  north west direction
                    //}
                }
            }
            else if(slope > 0)
            {
                if(y2 > y1 && x2 > x1)
                {
                    //instruction = "Move " + Math.abs(angle) + " degrees in South East Direction";

                    angle = Math.abs(angle);
                    if(isEqual(angle, 45) == true)
                    {
                        instruction = "Move in North Direction";
                    }
                    else if(angle > 45)
                    {
                        angle-=45;
                        instruction = "Move " + Math.abs(angle)+" degrees in North East Direction";
                    }
                    else
                    {
                        instruction = "Move " + Math.abs(45-angle) + " degrees in North West Direction";
                    }

                    //deg = Math.abs(angle)   if(deg==45)  west
                    //if(deg>45){ deg-=45
                    //      move 90-deg in south west direction
                    // }
                    // else{  move 45+deg   north west direction
                    //}
                }
                else
                {
                    //instruction = "Move " + Math.abs(angle) + " degrees in North West Direction";

                    angle = Math.abs(angle);
                    if(isEqual(angle, 45) == true)
                    {
                        instruction = "Move in South Direction";
                    }
                    else if(angle > 45)
                    {
                        angle-=45;
                        instruction = "Move " + Math.abs(angle)+" degrees in South West Direction";
                    }
                    else
                    {
                        instruction = "Move " + Math.abs(45-angle) + " degrees in South East Direction";
                    }

                    //deg = Math.abs(angle)   if(deg==45)  east
                    //if(deg>45){ deg-=45
                    //      move 90-deg in north east direction
                    // }
                    // else{  move 45+deg   south east direction
                    //}
                }
            }
            else
            {
                if(x2 < x1)
                {
                    instruction = "Move 45 degrees in South East Direction";
                    //instruction = "Move in North Direction";
                    //45* south east
                }
                else
                {
                    instruction = "Move 45 degrees in North West Direction";
                    //instruction = "Move in South Direction";
                    //45* north west
                }
            }
        }

        ArrayList<Pair<Integer, Integer>> poi = new ArrayList<Pair<Integer,Integer>>();


        directions.add(instruction);



        return new Pair<Double, ArrayList<String>>(dist/4, directions); //Units to metre
    }
}