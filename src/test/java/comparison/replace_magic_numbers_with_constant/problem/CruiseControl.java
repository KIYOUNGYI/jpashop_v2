package comparison.replace_magic_numbers_with_constant.problem;


public class CruiseControl {

    private double targetSpeedKmh;

    void setPreset(int speedPreset) {
        if (speedPreset == 2) {
            setTargetSpeedKmh(16944);
        } else if (speedPreset == 1) {
            setTargetSpeedKmh(7667);
        } else if (speedPreset == 0) {
            setTargetSpeedKmh(0);
        }
    }

    void setTargetSpeedKmh(double speed) {
        targetSpeedKmh = speed;
    }

    double getTargetSpeedKmh(){
        return targetSpeedKmh;
    }
}

class Main {
    static void usage() {
        CruiseControl cruiseControl = new CruiseControl();
        cruiseControl.setPreset(1);
        cruiseControl.setPreset(2);
        cruiseControl.setPreset(-1);
        System.out.println(cruiseControl.getTargetSpeedKmh());
    }

    public static void main(String[] args) {
        usage();
    }
}
