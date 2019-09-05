package air.kanna.spider.novel.util;

public class Timer {

    public static final int BASE_WAIT_TIME = 8000;
    public static final int RANDOM_WAIT_TIME = 7000;
    
    private static int baseTime = BASE_WAIT_TIME;
    private static int randomTime = RANDOM_WAIT_TIME;
    
    public static int setBaseTime(int time) {
        if(time <= 0) {
            time = BASE_WAIT_TIME;
        }
        
        baseTime = time;
        return time;
    }
    
    public static int setRandomTime(int time) {
        if(time <= 0) {
            time = RANDOM_WAIT_TIME;
        }
        
        randomTime = time;
        return time;
    }
    
    
    public static int getWaitingTime() {
        return (int)(Math.random() * baseTime) + randomTime;
    }
}
