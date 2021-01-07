import java.util.concurrent.Semaphore;

int N = 4;
permissionToGetOn = [new Semaphore(0), new Semaphore(0)]; 
Semaphore occupancy = new Semaphore (0);
Semaphore permissionToGetOff = new Semaphore (0);

Thread.start { //Elevator 
    int floor =0;
    while (true) {
        // COMPLETE: protocol for getting on
        N.times{
            permissionToGetOn[floor].release();
        }
       
        N.times{
             occupancy.acquire(); 
        }
        //println(occupancy);
        // travel to next floor
        floor = floor +1 % 2;
        // COMPLETE: protocol for getting off
        N.times{
            permissionToGetOff.release();
        }
        N.times{
             occupancy.acquire(); 
        }
    } 
}

Thread.start { // Worker
    Random rnd = new Random ();
    int floor = rnd.nextInt(1); 
    permissionToGetOn[floor].acquire();
     // getting on... 
     occupancy.release();
    // in transit
    permissionToGetOff.acquire();
    // getting off ...
    occupancy.release(); 
}

return;